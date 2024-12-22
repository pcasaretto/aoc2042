(ns aoc2024.day2.core
  (:require [clojure.string :as str]))

(defn parse-line
  "Parse a single line into a vector of numbers"
  [line]
  (mapv parse-long (str/split line #"\s+")))

(defn parse-input
  "Parse input string into a vector of number vectors"
  [input]
  (mapv parse-line (str/split-lines input)))

(defn valid-sequence?
  "Check if a sequence is valid according to reactor safety rules:
   - All differences must be between 1 and 3 inclusive
   - All differences must be consistently positive or negative"
  [nums]
  (when (>= (count nums) 2)  ; need at least 2 numbers to check differences
    (let [diffs (map - (rest nums) nums)
          first-diff (first diffs)]
      (let [expected-sign (if (pos? first-diff) pos? neg?)]
        (every? #(and (expected-sign %)
                     (<= 1 (Math/abs %) 3))
               diffs)))))

(defn remove-nth
  "Remove the nth element from a vector"
  [v n]
  (vec (concat (subvec v 0 n) (subvec v (inc n)))))

(defn valid-with-dampener?
  "Check if a sequence is valid or can be made valid by removing one number"
  [nums]
  (boolean
    (if (valid-sequence? nums)
      true
      (when (>= (count nums) 3)  ; need at least 3 numbers to remove one and still have a sequence
        (some valid-sequence?
              (map #(remove-nth nums %)
                   (range (count nums))))))))

(defn part1
  "Count how many sequences are valid"
  [input]
  (->> (parse-input input)
       (filter valid-sequence?)
       count))

(defn part2
  "Count how many sequences are valid with the Problem Dampener"
  [input]
  (->> (parse-input input)
       (filter valid-with-dampener?)
       count)) 