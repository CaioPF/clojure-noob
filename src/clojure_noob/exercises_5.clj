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
                :strenght 4
                :dexterity 5}})

(def c-int (comp :intelligence :attributes))

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


; 3. Implement the assoc-in function. Hint: use the assoc function and define its parameters as [m [k & ks] v].
 (defn my-assoc-in
  []
  )

; 4. Look up and use the update-in function.

; 5. Implement update-in.
(defn my-update-in
  []
  )
