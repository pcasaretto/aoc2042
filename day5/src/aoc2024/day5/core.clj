(ns aoc2024.day5.core
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-rule [rule]
  (let [[_ left right] (re-matches #"(\d+)\|(\d+)" rule)]
    [(parse-long left) (parse-long right)]))

(defn parse-update [update]
  (->> (str/split update #",")
       (mapv parse-long)))

(defn parse-input [input]
  (let [[rules-section updates-section] (str/split input #"\n\n")
        rules (->> (str/split-lines rules-section)
                   (map parse-rule)
                   (reduce (fn [acc [left right]]
                             (update acc left (fnil conj #{}) right))
                           {}))
        updates (->> (str/split-lines updates-section)
                     (mapv parse-update))]
    {:rules rules
     :updates updates}))

(defn valid-update? [rules update]
  (loop [seen #{}
         [current & rest] update]
    (if-let [must-follow (and current (get rules current #{}))]
      (when-not (not-empty (set/intersection must-follow seen))
        (recur (conj seen current) rest))
      true)))

;; (defn must-come-before? [rules a b]
;;   (or (contains? (get rules b #{}) a)
;;       (some #(and (contains? (get rules % #{}) a)
;;                   (must-come-before? rules a %))
;;             (get rules b #{}))))

(defn must-come-before? [rules a b]
  (contains? (get rules b #{}) a))

(defn sort-update [rules update]
  (sort (fn [a b]
          (cond
            (must-come-before? rules a b) 1
            (must-come-before? rules b a) -1
            :else 0))
        update))

(defn median [coll]
  (nth coll (quot (count coll) 2)))

(defn part1 [input]
  (let [{:keys [rules updates]} (parse-input input)]
    (->> updates
         (filter #(valid-update? rules %))
         (map median)
         (reduce +))))

(defn part2 [input]
  (let [{:keys [rules updates]} (parse-input input)]
    (->> updates
         (remove #(valid-update? rules %))
         (map #(sort-update rules %))
         (map median)
         (reduce +)))) 