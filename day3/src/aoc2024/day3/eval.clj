(ns aoc2024.day3.eval)

(defn eval
  [instructions]
  (map (fn [instruction] (apply * (:args instruction))) instructions))
