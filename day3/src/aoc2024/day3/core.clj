(ns aoc2024.day3.core
  (:require [clojure.string :as str]
            [aoc2024.day3.lexer :as lexer]))

(defn part1 [input]
  (let [tokens (lexer/lex input)]
    (println tokens)))
 