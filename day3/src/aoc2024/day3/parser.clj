(ns aoc2024.day3.parser
  (:require
   [aoc2024.day3.lexer :as lexer]))


;; (defn expect
;;   [match modify]
;;   (fn [lexeme acc]
;;     (when (match lexeme)
;;       (modify acc lexeme))))

;; (defn exhaust-expectations
;;   [lexemes expectations initial-value]
;;   (loop [lexemes lexemes
;;          expectations expectations
;;          acc initial-value]
;;     (when-let [[lexeme & lexemes] lexemes]
;;       (if-let [[expectation & expectations] expectations]
;;         (when-let [new-acc (expectation lexeme acc)]
;;           (recur lexemes expectations new-acc))
;;         ; exhausted expectations
;;         [lexemes acc]))))

;; (def function-call-expectations
;;   )

;; (defn expect-function-call
;;   [lexemes output]
;;   (if-let [[rest output] (exhaust-expectations lexemes [] output)]
;;     [expect-function-call rest output]
;;     nil))

(declare expect-function-call)
(declare expect-number)

(defn expect-comma-or-right-paren
  [lexemes output acc]
  (println "expect-comma")
  (when-let [[first & rest] lexemes]
    (cond
      (= (:type first) :comma) (expect-number rest output acc)
      (= (:type first) :rightParen) [expect-function-call rest (conj output acc)]
      :else [expect-function-call rest output])))

(defn expect-number
  [lexemes output acc]
  (println "expect-number")
  (when-let [[first & rest] lexemes]
    (if (= (:type first) :number)
      (expect-comma-or-right-paren rest output (update acc :args conj (:value first)))
      [expect-function-call rest output])))

(defn expect-left-paren
  [lexemes output acc]
  (println "expect-left-paren")
  (when-let [[first & rest] lexemes]
    (if (= (:type first) :leftParen)
      (expect-number rest output acc)
      [expect-function-call rest output])))

(defn expect-function-call
  [lexemes output]
  (println "expect-function-call")
  (when-let [[first & rest] lexemes]
    (if (= (:type first) :identifier)
      (expect-left-paren rest output {:type :function-call :name (:value first) :args []})
      [expect-function-call rest output])))

(defn parse
  "Convert input lexemes into a sequence of instructions"
  [lexemes]
  (loop [lexemes lexemes
         output []
         next expect-function-call]
    (if-let [[next lexemes output] (next lexemes output)]
      (recur lexemes output next)
      output)))
