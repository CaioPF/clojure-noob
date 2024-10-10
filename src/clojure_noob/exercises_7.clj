(ns clojure-noob.exercises-7)

;; 1. Use the list function, quoting, and read-string to create a list that, when evaluated,
;; prints your first name and your favorite sci-fi movie.
; (eval (list (read-string "println") '"Caio" '"Alien"))

;; 2. Create an infix function that takes a list like (1 + 3 * 4 - 5) and transforms it into
;; the lists that Clojure needs in order to correctly evaluate the expression using operator precedence rules.
(defn infix
  [[num1 plus num2 times num3 minus num4]]
  (list minus num4 (list plus num1 (list times num2 num3))))
