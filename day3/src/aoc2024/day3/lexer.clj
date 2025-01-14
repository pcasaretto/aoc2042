(ns aoc2024.day3.lexer)

(declare lex-start)  ; Forward declaration since functions refer to each other

(defn lex-identifier
  "Try to lex an identifier"
  ([[first & rest :as input] output]
   (if (Character/isLetter first)
     (lex-identifier rest output [first])
     [lex-start input output]))
  ([[first & rest :as input] output currentIdentifier]
   (cond
     (nil? first) [lex-start input (conj output {:type :identifier :value (apply str currentIdentifier)})]
     (Character/isLetterOrDigit first) (recur rest output (conj currentIdentifier first))
     (= \_ first) (recur rest output (conj currentIdentifier first))
     (= \' first) (recur rest output (conj currentIdentifier first))
     :else [lex-start input (conj output {:type :identifier :value (apply str currentIdentifier)})])))

(defn lex-number
  "Try to lex an number"
  ([[first & rest :as input] output]
   (if (Character/isDigit first)
     (lex-number rest output [first])
     [lex-start input output]))
  ([[first & rest :as input] output currentIdentifier]
   (cond
     (nil? first) [lex-start input (conj output {:type :number :value (Integer/parseInt (apply str currentIdentifier))})]
     (Character/isDigit first) (recur rest output (conj currentIdentifier first))
     :else [lex-start input (conj output {:type :number :value (Integer/parseInt (apply str currentIdentifier))})])))

(defn lex-start
  "Entry point for lexing"
  [input output]
  (when-let [[first & rest] input]
    (cond
      (Character/isWhitespace first) [lex-start rest output]
      (= \( first) [lex-start rest (conj output {:type :leftParen})]
      (= \) first) [lex-start rest (conj output {:type :rightParen})]
      (= \[ first) [lex-start rest (conj output {:type :leftSquareBracket})]
      (= \] first) [lex-start rest (conj output {:type :rightSquareBracket})]
      (= \; first) [lex-start rest (conj output {:type :semicolon})]
      (= \{ first) [lex-start rest (conj output {:type :leftCurlyBracket})]
      (= \} first) [lex-start rest (conj output {:type :rightCurlyBracket})]
      (= \, first) [lex-start rest (conj output {:type :comma})]
      (Character/isDigit first) [lex-number input output]
      (Character/isLetter first) [lex-identifier input output]
      :else (recur rest output))))

(defn lex
  "Convert input string into a sequence of tokens"
  [input]
  (loop [input input
         output []
         next lex-start]
    (if-let [[next input output] (next input output)]
      (recur input output next)
      output)))