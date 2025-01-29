(ns aoc2024.day7.core
  (:require [clojure.string :as str]))

(defn parse-line [line]
  (let [[target numbers] (str/split line #":")
        numbers (map parse-long (str/split (str/trim numbers) #"\s+"))]
    {:target (parse-long target)
     :numbers (vec numbers)}))

(defn parse-input [input]
  (map parse-line (str/split-lines input)))

(defn evaluate [numbers ops]
  (reduce
   (fn [result [n op]]
     (if (= op :+)
       (+ result n)
       (* result n)))
   (first numbers)
   (map vector (rest numbers) ops)))

(defn can-make-target? [target numbers]
  (if (= 1 (count numbers))
    (= target (first numbers))
    (let [n-ops (dec (count numbers))
          ops-combinations (for [i (range (bit-shift-left 1 n-ops))]
                             (map #(bit-test i %) (range n-ops)))]
      (some #(= target (evaluate numbers (map (fn [b] (if b :+ :*)) %)))
            ops-combinations))))

(defn part1 [input]
  nil)

(defn part2 [input]
  nil) 