(ns aoc2024.day3.parser)

(declare expect-function-call)
(declare expect-number)

(defn expect-comma-or-right-paren
  [lexemes output acc]
  (when-let [[first & rest] lexemes]
    (cond
      (= (:type first) :comma) (expect-number rest output acc)
      (= (:type first) :rightParen) [expect-function-call rest (conj output acc)]
      :else [expect-function-call lexemes output])))

(defn expect-number-or-right-paren
  [lexemes output acc]
  (when-let [[first & rest] lexemes]
    (cond
      (= (:type first) :number) (expect-comma-or-right-paren rest output (update acc :args conj (:value first)))
      (= (:type first) :rightParen) [expect-function-call rest (conj output acc)]
      :else [expect-function-call lexemes output])))

(defn expect-number
  [lexemes output acc]
  (when-let [[first & rest] lexemes]
    (if (= (:type first) :number)
      (expect-comma-or-right-paren rest output (update acc :args conj (:value first)))
      [expect-function-call lexemes output])))

(defn expect-left-paren
  [lexemes output acc]
  (when-let [[first & rest] lexemes]
    (if (= (:type first) :leftParen)
      (expect-number-or-right-paren rest output acc)
      [expect-function-call lexemes output])))

(defn expect-function-call
  [lexemes output]
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
