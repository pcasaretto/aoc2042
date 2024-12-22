(ns aoc2024.day1.core
  (:require [clojure.string :as str]))

(defn parse-input
  "Parse input string into two vectors of numbers"
  [input]
  (if (str/blank? input)
    [[] []]
    (let [lines (str/split-lines input)
          parse-line (fn [line]
                       (let [[left right] (str/split line #"\s+")]
                         [(parse-long left) (parse-long right)]))]
      (reduce (fn [[lefts rights] line]
                (let [[l r] (parse-line line)]
                  [(conj lefts l) (conj rights r)]))
              [[] []]
              lines))))

(defn calculate-distance
  "Calculate total distance between sorted pairs"
  [left-nums right-nums]
  (->> (map (fn [l r] (Math/abs (- l r)))
            (sort left-nums)
            (sort right-nums))
       (reduce + 0)))

(defn calculate-similarity
  "Calculate similarity score by multiplying each left number by its frequency in right list"
  [left-nums right-nums]
  (let [right-freqs (frequencies right-nums)]
    (->> left-nums
         (map #(* % (get right-freqs % 0)))
         (reduce + 0))))

(defn part1
  "Solve part 1 of the challenge"
  [input]
  (let [[lefts rights] (parse-input input)]
    (calculate-distance lefts rights)))

(defn part2
  "Solve part 2 of the challenge"
  [input]
  (let [[lefts rights] (parse-input input)]
    (calculate-similarity lefts rights)))