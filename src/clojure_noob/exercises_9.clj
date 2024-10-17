(ns clojure-noob.exercises-9
  (:require [clojure.string :as str])
  (:import (java.net URLEncoder)))

(def yak-butter-international
  {:store "Yak Butter International"
   :price 90
   :smoothness 90})
(def butter-than-nothing
  {:store "Butter Than Nothing"
   :price 150
   :smoothness 83})
; This is the butter that meets ou requirements
(def baby-got-yak
  {:store "Baby Got Yak"
   :price 94
   :smoothness 99})

(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)

(defn satisfactory?
  "If the butter meets our criteria, return the butter, else return false"
  [butter]
  (and (<= (:price butter) 100)
       (>= (:smoothness butter) 97)
       butter))

;; Checking the "sites" sinchronously
;; (time
;;   (some
;;     (comp satisfactory? mock-api-call)
;;     [yak-butter-international butter-than-nothing baby-got-yak]))
;;
;; Checking the "sites" with concurrency
;; (time
;;   (let [butter-promise (promise)]
;;     (doseq [butter [yak-butter-international butter-than-nothing baby-got-yak]]
;;       (future
;;         (if-let [satisfactory-butter (satisfactory? (mock-api-call butter))]
;;           (deliver butter-promise satisfactory-butter))))))

(defmacro wait
  "Sleep `timeout` seconds before evaluating body"
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))

;;(let [saying3 (promise)]
;;  (future (deliver saying3 (wait 100 "Cheerio!")))
;;  @(let [saying2 (promise)]
;;     (future (deliver saying2 (wait 400 "Pip pip!")))
;;     @(let [saying1 (promise)]
;;        (future (deliver saying1 (wait 200 "'Ello, gov'na!")))
;;        (println @saying1)
;;        saying1)
;;     (println @saying2)
;;     saying2)
;;  (println @saying3)
;;  saying3)
;;
;;(defmacro enqueue
;;  ([q concurrent-promise-name concurrent serialized]
;;    `(let [~concurrent-promise-name (promise)]
;;       (future (deliver ~concurrent-promise-name ~concurrent))
;;       (deref ~q)
;;       ~serialized
;;       ~concurrent-promise-name))
;;  ([concurrent-promise-name concurrent serialized]
;;    `(enqueue (future) ~concurrent-promise-name ~concurrent ~serialized)))
;;
;;(-> (enqueue saying (wait 200 "'Ello, gov'na!") (println @saying))
;;    (enqueue saying (wait 400 "Pip pip!") (println @saying))
;;    (enqueue saying (wait 100 "Cheerio!") (println @saying)))

;; 1. Write a function that takes a string as an argument and searches for it on
;; Bing and Google using the slurp function. Your function should return the HTML
;; of the first page returned by the search.

;; Not going to make a function for both search engines.
(defn google-search
  [query]
  (let [base-url "https://www.google.com/search?q="
        encoded-query (URLEncoder/encode query "UTF-8")
        search-url (str base-url encoded-query)]
    (try
      (slurp search-url)
      (catch Exception e
        (str "Failed to fetch Google results: " (.getMessage e))))))

;; 2. Update your function so it takes a second argument consisting of the search
;; engines to use.

(defn search-url
  [search-engine query]
  (let [encoded-query (URLEncoder/encode query "UTF-8")]
    (case search-engine
      :google (str "https://www.google.com/search?q=" encoded-query)
      :bing (str "https://www.bing.com/search?q=" encoded-query)
      (throw (IllegalArgumentException. (str "Unsupported search engine: " search-engine))))))

(defn search-engine
  [query search-engines]
  (into {}
        (for [engine search-engines]
          [engine
           (try
             (slurp (search-url engine query))
             (catch Exception e
               (str "Failed to fetch results from " (name engine) ": "
                    (.getMessage e))))])))

;; 3. Create a new function that takes a search term and search engines as arguments,
;; and returns a vector of the URLs from the first page of search results from each
;; search engine.
