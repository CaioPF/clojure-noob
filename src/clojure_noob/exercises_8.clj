(ns clojure-noob.exercises-8)

(def order-details-error
  {:name "Mitchard Blimmons"
   :email "mitchard.blimmonsgmail.com"})

(def order-details-fine
  {:name "Mitchard Blimmons"
   :email "mitchard.blimmons@gmail.com"})

(def order-details-validations
  {:name
   ["Please enter a name" not-empty]

   :email
   ["Please enter an e-mail address" not-empty

    "Your e-mail address doesn't look like an e-mail address"
    #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))

(defn validate
  "Returns a map with a vector of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))

;; The following code would be used to validate, but the macro bellow does it better.
;; (let [errors (validate order-details order-details-validations)]
;;  (if (empty? errors)
;;    (println :success)
;;    (println :failure errors)))

(defmacro if-valid
  "Handle validation more concisely"
  [to-validate validations errors-name & then-else]
  `(let [~errors-name (validate ~to-validate ~validations)]
     (if (empty? ~errors-name)
       ~@then-else)))

;;(macroexpand
;; '(if-valid order-details order-details-validations my-error-name
;;            (println :success)
;;            (println :failure my-error-name)))

;;(let*
;; [my-error-name (validate order-details-error order-details-validations)]
;; (if (clojure.core/empty? my-error-name)
;;   (println :success)
;;   (println :failure my-error-name)))

;; 1. Write the macro when-valid so that it behaves similarly to when. Here is an
;; example of calling it:
;; (when-valid order-details order-details-validations
;;  (println "It's a success!")
;;  (render :success))
;; When the data is valid, the println and render forms should be evaluated, and
;; when-valid should return nil if the data is invalid.
(defmacro when-valid
  [to-validate validations & actions]
  `(if-valid ~to-validate ~validations ~'errors-name
             (do ~@actions)
             nil))

;;(when-valid order-details-fine order-details-validations
;;            (println "It's a success!")
;;            (println :success))

;;(when-valid order-details-error order-details-validations
;;            (println "It's a success!")
;;            (println :success))

;; 2. You saw that "and" is implemented as a macro. Implement "or" as a macro.
(defmacro my-or
  ([] true)
  ([x] x)
  ([x & next]
   `(let [element# ~x]
      (if element# true (my-or ~@next)))))

;; 3. In Chapter 5 you created a series of functions (c-int, c-str, c-dex) to read an
;; RPG character’s attributes. Write a macro that defines an arbitrary number of
;; attribute-retrieving functions using one macro call. Here’s how you would call it:
;; (defattrs c-int :intelligence
;;           c-str :strength
;;           c-dex :dexterity)

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(defmacro defattrs
  [& functions]
  `(do
     ~@(for [[name attr] (partition 2 functions)]
         `(def ~name (comp ~attr :attributes)))))
