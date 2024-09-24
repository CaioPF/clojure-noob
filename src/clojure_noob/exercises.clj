(ns clojure-noob.exercises)

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

; 5. Create a function that’s similar to symmetrize-body-parts except that it has to work with weird space aliens with
; radial symmetry. Instead of two eyes, arms, legs, and so on, they have five.

; 6. Create a function that generalizes symmetrize-body-parts and the function you created in Exercise 5. The new
; function should take a collection of body parts and the number of matching body parts to add. If you’re completely new
; to Lisp languages and functional programming, it probably won’t be obvious how to do this. If you get stuck, just move
; on to the next chapter and revisit the problem later.