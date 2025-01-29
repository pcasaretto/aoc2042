(ns aoc2024.day7.recursive
  (:require [clojure.string :as str]))

(def ^:dynamic *memoize* true)

(defn parse-line [line]
  (let [[target numbers] (str/split line #":")
        numbers (map parse-long (str/split (str/trim numbers) #"\s+"))]
    {:target (parse-long target)
     :numbers (vec numbers)}))

(defn parse-input [input]
  (map parse-line (str/split-lines input)))

(defn can-make-target? [target nums]
  (cond
    (zero? target) true
    (empty? nums) false
    :else
    (let [n (last nums)
          rest-nums (butlast nums)]
      (or
       ;; Try addition
       (can-make-target? (- target n) rest-nums)
       ;; Try multiplication
       (when (zero? (mod target n))
         (can-make-target? (quot target n) rest-nums))
       ;; Try concatenation
       (when-let [suffix-str (str n)]
         (when (.endsWith (str target) suffix-str)
           (let [prefix (parse-long (subs (str target) 0 (- (count (str target)) (count suffix-str))))]
             (when prefix
               (can-make-target? prefix rest-nums)))))))))

(def can-make-target-memo? (memoize can-make-target?))

(defn part1 [input]
  (->> input
       parse-input
       (filter (fn [{:keys [target numbers]}]
                 (if *memoize*
                   (can-make-target-memo? target numbers)
                   (can-make-target? target numbers))))
       (map :target)
       (reduce +)))

(defn part2 [input]
  (part1 input)) 
