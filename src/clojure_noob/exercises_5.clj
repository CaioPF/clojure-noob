(ns clojure-noob.exercises-5)

; Recursive problem solving
(defn sum
  ([vals]
   (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))

; The comp function
(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))

; comp using a function that needs more than one argument
(def spell-slots-comp (comp int inc #(/ % 2) c-int))

(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

; 1. You used (comp :intelligence :attributes) to create a function that returns a character’s intelligence. Create a
; new function, attr, that you can call like (attr :intelligence) and that does the same thing.
(defn attr
  [map attribute]
  (get-in map [:attributes attribute]))

; The function call is a bit different from the exercise proposition, but it's better than inserting the map reference
; directly into the code itself.
(attr character :intelligence)

; 2. Implement the comp function.
(defn my-comp
  [& functions]
  (fn [& x]
    (reduce
     (fn [result function]
       (if (= result x)
         (apply function result)
         (function result)))
     x
     (reverse functions))))

(def square (fn [x] (* x x)))

; ((my-comp inc square +) 1 3)

; 3. Implement the assoc-in function. Hint: use the assoc function and define its parameters as [m [k & ks] v].
(defn my-assoc-in
  [m [k & ks] v]
  (if (pos? (count ks))
    (assoc m k (my-assoc-in (get m k) ks v))
    (assoc m k v)))

(def my-map {:a {:b {:c 1}}})
(my-assoc-in my-map [:a :d :e] 2)
(my-assoc-in my-map [:a :b :e] 3)
(my-assoc-in my-map [:a :b :c] 4)

; 4. Look up and use the update-in function.
(def some-map {:a 2 :b [1 0 3 5]})
(update-in some-map [:b 2] + 5)

; 5. Implement update-in.
(defn my-update-in
  [m [k & ks] f & args]
  (if (pos? (count ks))
    (assoc m k (apply my-update-in (get m k) ks f args))
    (assoc m k (apply f (get m k) args))))

(my-update-in {:age 100} [:age] / 2 5)
