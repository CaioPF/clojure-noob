(ns clojure-noob.exercises-3)

; 1. Use the str, vector, list, hash-map, and hash-set functions.
(let [nome "Caio"]
  (str nome " utilizando a fn str"))

(vector "banana" 123 {:a 1 :b 2})

(list "banana" 123 {:a 1 :b 2})

(hash-map :nome "Caio" :idade 33 :residencia "Caraguatatuba")

(hash-set "Caio" 33 "Caraguatatuba" 33)

; 2. Write a function that takes a number and adds 100 to it.
(defn add-100
  [number]
  (+ number 100))

(add-100 10)

; 3. Write a function, dec-maker, that works exactly like the function inc-maker except with subtraction:
;(def dec9 (dec-maker 9))
;(dec9 10)
; => 1
(defn dec-maker
  [step]
  #(- % step))

(def dec5 (dec-maker 5))

(dec5 10)

; 4. Write a function, mapset, that works like map except the return value is a set:
;(mapset inc [1 1 2 2])
; => #{2 3}
(defn mapset
  [function & [numbers]]
  (into #{} (map function numbers)))

(mapset inc [1 1 2 2])

; 5. Create a function that’s similar to symmetrize-body-parts except that it has to work with weird space aliens with
; radial symmetry. Instead of two eyes, arms, legs, and so on, they have five.

(def asym-radial-alien-body-parts [{:name "head" :size 3}
                                   {:name "first-eye" :size 1}
                                   {:name "first-ear" :size 1}
                                   {:name "mouth" :size 1}
                                   {:name "nose" :size 1}
                                   {:name "neck" :size 2}
                                   {:name "first-shoulder" :size 3}
                                   {:name "first-upper-arm" :size 3}
                                   {:name "chest" :size 10}
                                   {:name "back" :size 10}
                                   {:name "first-forearm" :size 3}
                                   {:name "abdomen" :size 6}
                                   {:name "first-kidney" :size 1}
                                   {:name "first-hand" :size 2}
                                   {:name "first-knee" :size 2}
                                   {:name "first-thigh" :size 4}
                                   {:name "first-lower-leg" :size 3}
                                   {:name "first-achilles" :size 1}
                                   {:name "first-foot" :size 2}])

(def ordinals ["first" "second" "third" "fourth" "fifth"])

(defn matching-parts
  [part]
  (if (clojure.string/starts-with? (:name part) "first-")
    (let [part-name (subs (:name part) 5) ; Removing the "first" from the part :name
          part-size (:size part)]
        (for [idx (range 5)]
          {:name (str (ordinals idx) part-name)
           :size part-size}))
    [part]))

(defn symetrize-radial-aliens
  "Expects a seq of maps that have :name and :size"
  [asym-radial-alien-body-parts]
  (mapcat matching-parts asym-radial-alien-body-parts))

; 6. Create a function that generalizes symmetrize-body-parts and the function you created in Exercise 5. The new
; function should take a collection of body parts and the number of matching body parts to add. If you’re completely new
; to Lisp languages and functional programming, it probably won’t be obvious how to do this. If you get stuck, just move
; on to the next chapter and revisit the problem later.

(def asym-body-parts [{:name "head" :size 3}
                      {:name "first-eye" :size 1}
                      {:name "first-ear" :size 1}
                      {:name "nose" :size 1}
                      {:name "mouth" :size 1}
                      {:name "neck" :size 2}
                      {:name "first-shoulder" :size 3}
                      {:name "first-upper-arm" :size 3}
                      {:name "chest" :size 10}
                      {:name "back" :size 10}
                      {:name "first-forearm" :size 3}
                      {:name "abdomen" :size 6}
                      {:name "first-kidney" :size 1}
                      {:name "first-hand" :size 2}
                      {:name "first-knee" :size 2}
                      {:name "first-thigh" :size 4}
                      {:name "first-lower-leg" :size 3}
                      {:name "first-achilles" :size 1}
                      {:name "first-foot" :size 2}])

(defn matching-parts-any-number
  [part number-of-parts]
  (if (clojure.string/starts-with? (:name part) "first-")
    (let [part-name (subs (:name part) 6) ; Removing the "first-" from the part :name
          part-size (:size part)]
        (for [idx (range 1 (inc number-of-parts))]
          {:name (str part-name "-" idx)
           :size part-size}))
    [part]))

(defn symetrize-beings
  "Expects a seq of maps that have :name and :size"
  [asym-body-parts number-of-parts]
  (mapcat #(matching-parts-any-number % number-of-parts) asym-body-parts))
