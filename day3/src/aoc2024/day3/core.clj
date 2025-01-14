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

(defn part2 [input]
  (->> input
       lexer/lex
       parser/parse
       (filter (fn [instruction] (or (str/ends-with? (:name instruction) "mul")
                                     (str/ends-with? (:name instruction) "do")
                                     (str/ends-with? (:name instruction) "don't"))))
       (map (fn [instruction] (cond
                                (str/ends-with? (:name instruction) "mul") (assoc instruction :name "mul")
                                (str/ends-with? (:name instruction) "do") (assoc instruction :name "do")
                                (str/ends-with? (:name instruction) "don't") (assoc instruction :name "dont")
                                :else instruction)))
       (filter (fn [instruction] (if (str/ends-with? (:name instruction) "mul")
                                   (= 2 (count (:args instruction)))
                                   true)))
       ((fn [instructions] (eval/eval2 instructions
                                       {"mul" (fn [args context] (if (:do context) (update context :result (fn [x] (+ x (apply * args)))) context))
                                        "do" (fn [_ context] (assoc context :do true))
                                        "dont" (fn [_ context] (assoc context :do false))}
                                       {:result 0 :do true})))
       :result))

