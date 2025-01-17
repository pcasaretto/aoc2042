(ns aoc2024.day3.parser)

(defn expect-optional
  [expectation]
  (fn [lexemes output] (or (expectation lexemes output) [lexemes output])))

; consume-all consumes lexemes that fail the expectation
; while mutating the output with the output-transformation
; for lexemes that pass the expectation
(defn consume-all
  [expectation]
  (fn [lexemes output]
    (loop [lexemes lexemes
           output output]
      (if (empty? lexemes)
        output
        (if-let [[remaining-lexemes new-output] (expectation lexemes output)]
          (recur remaining-lexemes new-output)
          (recur (rest lexemes) output))))))

(defn expect-zero-or-more
  [expectation]
  (fn [lexemes output] (loop [lexemes lexemes
                              output output]
                         (if-let [[remaining-lexemes new-output] (expectation lexemes output)]
                           (recur remaining-lexemes new-output)
                           [lexemes output]))))

(defn pass-through-output [_ output] output)

(defn expect-all
  ([expectations] (expect-all expectations pass-through-output))
  ([expectations output-transformation]
   (fn [lexemes output]
     (loop [[expectation & rest] expectations
            lexemes lexemes
            output output]
       (if (nil? expectation)
         [lexemes (output-transformation lexemes output)]
         (when-let [[lexemes-remaining transformed-output] (expectation lexemes output)]
           (recur rest lexemes-remaining transformed-output)))))))

(defn expect-type
  ([type output-transformation]
   (fn [lexemes output]
     (when-let [[first & rest] lexemes]
       (when (= (:type first) type)
         [rest (output-transformation lexemes output)])))))

(defn expect-identifier
  ([] (expect-identifier pass-through-output))
  ([output-transformation] (expect-type :identifier output-transformation)))
(defn expect-comma
  ([] (expect-comma pass-through-output))
  ([output-transformation] (expect-type :comma output-transformation)))
(defn expect-number
  ([] (expect-number pass-through-output))
  ([output-transformation] (expect-type :number output-transformation)))
(defn expect-right-paren
  ([] (expect-right-paren pass-through-output))
  ([output-transformation] (expect-type :rightParen output-transformation)))
(defn expect-left-paren
  ([] (expect-left-paren pass-through-output))
  ([output-transformation] (expect-type :leftParen output-transformation)))

;a function call is almost like a regex
; identifier leftParen ( number (comma number)*)? rightParen
(defn expect-function-call
  [output-transformation]
  (fn [lexemes output]
    ((expect-all
      [(expect-identifier (fn [[lexeme] output] (assoc output :name (:value lexeme))))
       (expect-left-paren)
       (expect-optional
        (expect-all
         [(expect-number (fn [[lexeme] output] (update output :args (fn [args] (conj args (:value lexeme))))))
          (expect-zero-or-more
           (expect-all
            [(expect-comma)
             (expect-number (fn [[lexeme] output] (update output :args (fn [args] (conj args (:value lexeme))))))]))]))
       (expect-right-paren)]
      (fn [_ result] (output-transformation output result)))
     lexemes
     {:type :function-call :args []})))


(defn parse
  "Convert input lexemes into a sequence of instructions"
  [lexemes]
  ((consume-all (expect-function-call (fn [output result] (conj output result)))) lexemes []))
