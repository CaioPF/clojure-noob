(ns clojure-noob.exercises-4)

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

; (glitter-filter 3 (mapify (parse (slurp filename))))

; 1. Turn the result of your glitter filter into a list of names.
(defn suspects-names-only
  [minimum-glitter records]
  (let
   [filtro (filter #(>= (:glitter-index %) minimum-glitter) records)]
    (map :name filtro)))
; (suspects-names-only 3 (mapify (parse (slurp filename))))

; 3. Write a function, validate, which will check that :name and :glitter-index are present when you append. The
; validate function should accept two arguments: a map of keywords to validating functions, similar to conversions, and
; the record to be validated.
(defn validate
  [validators record]
  (every? #(let [key (first %)
                 validator (second %)]
           (if (contains? record key)
             (validator (record key))
             false))
          validators))

(def validators {:name #(not-empty %)
                 :glitter-index #(and (number? %)
                                 (>= % 0))})

; 2. Write a function, append, which will append a new suspect to your list of suspects.
(defn append
  [name glitter-index]
  (let [data (mapify (parse (slurp filename)))
        vector (vec data)
        new-suspect {:name name :glitter-index glitter-index}
        valid? (validate validators new-suspect)]
    (if valid?
      (let [updated-vector (conj vector new-suspect)
            updated-list (into [] updated-vector)]
        updated-list)
      (println "Invalid record."))))

; 4. Write a function that will take your list of maps and convert it back to a CSV string. You’ll need to use the
; clojure.string/join function.

(defn to-csv
  [list-of-suspects]
  (reduce
    (fn [csv-string {name :name glitter-index :glitter-index}]
      (str csv-string (clojure.string/join "," [name (str glitter-index "\n")])))
    ""
    list-of-suspects))
