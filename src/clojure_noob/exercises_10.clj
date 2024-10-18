(ns clojure-noob.exercises-10
;;  (:require [clojure.core.async :as async]
;;            [clojure.string :as str])
  )

(def sock-varieties
  #{"darned" "argyle" "wool" "horsehair" "mulleted"
   "passive-aggressive" "striped" "polka-dotted"
   "athletic" "business" "power" "invisible" "gollumed"})

(defn sock-count
  [sock-variety count]
  {:variety sock-variety
   :count count})

(defn generate-sock-gnome
  "Create an initial sock gnome state with no socks"
  [name]
  {:name name
   :socks #{}})

(def sock-gnome (ref (generate-sock-gnome "Barumpharumph")))
(def dryer (ref {:name "LG 1337"
                 :socks (set (map #(sock-count % 2) sock-varieties))}))

(defn steal-sock
  [gnome dryer]
  (dosync
    (when-let [pair (some #(if (= (:count %) 2) %) (:socks @dryer))]
      (let [updated-count (sock-count (:variety pair) 1)]
        (alter gnome update-in [:socks] conj updated-count)
        (alter dryer update-in [:socks] disj pair)
        (alter dryer update-in [:socks] conj updated-count)))))

(defn similar-socks
  [target-sock sock-set]
  (filter #(= (:variety %) (:variety target-sock)) sock-set))

;-----------------------------------------------
;; Creating and binding dynamic vars
(def ^:dynamic *notification-address* "dobby@elf.org")

;; Dynamic var can be changed using binding, try this at REPL:
;; (binding [*notification-address* "test@elf.org"] *notification-address*)

;; They can be stacked as well
;;(binding [*notification-address* "tester-1@elf.org"]
;;  (println *notification-address*)
;;  (binding [*notification-address* "tester-2@elf.org"]
;;    (println *notification-address*))
;;  (println *notification-address*))

(defn notify
  [message]
  (str "TO: " *notification-address* "\n"
       "MESSAGE: " message))

;; REPL this: (notify "I fell.")
;; To test the fn and not spam a user we could use this:
;; (binding [*notification-address* "test@elf.org"] (notify "test!"))

;-----------------------------------------------
;; Exercises

;; 1. Create an atom with the initial value 0, use swap! to increment it a couple of times,
;; and then dereference it.

(def my-atom (atom 0))

;;(swap! my-atom inc)
;;(swap! my-atom inc)
;;
;;@my-atom

;; 2. Create a function that uses futures to parallelize the task of downloading random
;; quotes from http://www.braveclojure.com/random-quote using (slurp "http://www.braveclojure.com/random-quote").
;; The futures should update an atom that refers to a total word count for all quotes. The
;; function will take the number of quotes to download as an argument and return the atom’s
;; final value. Keep in mind that you’ll need to ensure that all futures have finished before
;; returning the atom’s final value. Here’s how you would call it and an example result:
;;(quote-word-count 5)
;;; => {"ochre" 8, "smoothie" 2}

; The website is returning a 404 error.

;;(defn quote-word-count
;;  [number-of-quotes]
;;  (let [word-count (atom {})
;;        download-quote (fn [_]
;;                         (let [quote (slurp "http://www.braveclojure.com/random-quote")]
;;                           (doseq [word (str/split quote #"\s+")]
;;                             (swap! word-count update word inc))))
;;        futures (map #(future (download-quote %)) (range number-of-quotes))]
;;    (doseq [f futures]
;;      (future-call f))
;;    (doseq [f futures]
;;      (future-cancel f))
;;    @word-count))

;; 3. Create representations of two characters in a game. The first character has 15 hit
;; points out of a total of 40. The second character has a healing potion in his inventory.
;; Use refs and transactions to model the consumption of the healing potion and the first
;; character healing.

(def first-character
  (ref
    {:max-hp 40 :current-hp 15 :inventory {}}))

(def second-character
  (ref
    {:max-hp 40 :current-hp 40 :inventory {:healing-potion 1}}))

(defn healing-validator
  [{:keys [current-hp max-hp]}]
  (if (> (+ current-hp 30) max-hp)
    max-hp
    (+ current-hp 30)))

(defn potion-validator
  [{:keys [inventory]}]
  (if (and (contains? inventory :healing-potion)
           (pos? (get inventory :healing-potion)))
    true
    false))

(defn potion-drinking
  [drinker origin-of-potion]
  (dosync
    (when (potion-validator @origin-of-potion)
      (alter origin-of-potion update-in [:inventory :healing-potion] dec)
      (alter drinker update :current-hp
             (fn [current-hp max-hp]
               (healing-validator {:current-hp current-hp :max-hp max-hp}))
             (:max-hp @drinker)))))
