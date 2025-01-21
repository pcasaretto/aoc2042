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

(defn check-x-mas [grid [row col]]
  (when (= \A (get-in grid [row col]))
    (let [first-diagonal [[(- row 1) (- col 1)] [row col] [(+ row 1) (+ col 1)]]
          second-diagonal [[(- row 1) (+ col 1)] [row col] [(+ row 1) (- col 1)]]
          first-diagonal-chars (when (every? (fn [[r c]] (in-bounds? grid r c)) first-diagonal)
                                 (mapv (fn [[r c]] (get-in grid [r c])) first-diagonal))
          second-diagonal-chars (when (every? (fn [[r c]] (in-bounds? grid r c)) second-diagonal)
                                  (mapv (fn [[r c]] (get-in grid [r c])) second-diagonal))]
      (and
       (or (= first-diagonal-chars [\M \A \S])
           (= first-diagonal-chars [\S \A \M]))
       (or (= second-diagonal-chars [\M \A \S])
           (= second-diagonal-chars [\S \A \M]))))))

(defn count-x-mas [grid]
  (let [height (count grid)
        width (count (first grid))]
    (->> (for [row (range 1 height)
               col (range 1 width)
               :when (check-x-mas grid [row col])]
           1)
         (reduce + 0))))

(defn part2 [input]
  (-> input
      parse-grid
      count-x-mas)) 