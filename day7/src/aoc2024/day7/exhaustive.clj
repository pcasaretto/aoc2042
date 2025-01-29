(ns aoc2024.day7.exhaustive
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
     (case op
       :+ (+ result n)
       :* (* result n)
       :|| (parse-long (str result n))))
   (first numbers)
   (map vector (rest numbers) ops)))

(defn ops-combinations [n]
  (for [i (range (Math/pow 3 n))]
    (loop [num i
           ops []]
      (if (= (count ops) n)
        ops
        (recur (quot num 3)
               (conj ops (case (mod num 3)
                           0 :+
                           1 :*
                           2 :||)))))))

(defn can-make-target? [target numbers]
  (if (= 1 (count numbers))
    (= target (first numbers))
    (let [n-ops (dec (count numbers))]
      (some #(= target (evaluate numbers %))
            (ops-combinations n-ops)))))

(defn part1 [input]
  (->> input
       parse-input
       (filter (fn [{:keys [target numbers]}]
                 (can-make-target? target numbers)))
       (map :target)
       (reduce +)))

(defn part2 [input]
  (->> input
       parse-input
       (filter (fn [{:keys [target numbers]}]
                 (can-make-target? target numbers)))
       (map :target)
       (reduce +))) 