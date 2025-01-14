(ns aoc2024.day3.core
  (:require [aoc2024.day3.lexer :as lexer]
            [aoc2024.day3.parser :as parser]
            [aoc2024.day3.eval :as eval]
            [clojure.string :as str]))

(defn part1 [input]
  (->> input
       lexer/lex
       parser/parse
       (filter (fn [instruction] (str/ends-with? (:name instruction) "mul")))
       (filter (fn [instruction] (= 2 (count (:args instruction)))))
       eval/eval
       (reduce +)))
