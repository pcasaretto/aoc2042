(ns aoc2024.day4.core
  (:require [clojure.string :as str]))

(def directions
  [[0 1]   ; right
   [0 -1]  ; left
   [1 0]   ; down
   [-1 0]  ; up
   [1 1]   ; down-right
   [1 -1]  ; down-left
   [-1 1]  ; up-right
   [-1 -1]]) ; up-left

(defn in-bounds? [grid row col]
  (and (>= row 0)
       (>= col 0)
       (< row (count grid))
       (< col (count (first grid)))))

(defn check-direction [grid [row col] [drow dcol]]
  (when (= \X (get-in grid [row col]))
    (let [positions (for [i (range 4)]
                      [(+ row (* i drow))
                       (+ col (* i dcol))])
          chars (when (every? (fn [[r c]] (in-bounds? grid r c)) positions)
                  (mapv (fn [[r c]] (get-in grid [r c])) positions))]
      (= chars [\X \M \A \S]))))

(defn count-xmas [grid]
  (let [height (count grid)
        width (count (first grid))]
    (->> (for [row (range height)
               col (range width)
               dir directions
               :when (check-direction grid [row col] dir)]
           1)
         (reduce + 0))))

(defn parse-grid [input]
  (->> input
       (str/split-lines)
       (mapv vec)))

(defn part1 [input]
  (-> input
      parse-grid
      count-xmas))

(defn part2 [input]
  nil) 