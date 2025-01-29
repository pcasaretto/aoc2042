(ns aoc2024.day6.core
  (:require [clojure.string :as str]))

(def direction-chars
  {\^ :up
   \> :right
   \v :down
   \< :left})

(def next-direction
  {:up :right
   :right :down
   :down :left
   :left :up})

(defn next-coord [{:keys [x y dir]}]
  (case dir
    :up    [x (dec y)]
    :right [(inc x) y]
    :down  [x (inc y)]
    :left  [(dec x) y]))

(defn out-of-bounds? [{:keys [width height]} [x y]]
  (or (< x 0) (>= x width)
      (< y 0) (>= y height)))

(defn blocked? [{:keys [obstacles]} [x y]]
  (contains? obstacles [x y]))

(defn next-position [{:keys [guard] :as state}]
  (let [next-pos (next-coord guard)]
    (cond
      (out-of-bounds? state next-pos) nil
      (blocked? state next-pos) {:guard (assoc guard :dir (next-direction (:dir guard)))
                                 :hit next-pos}
      :else {:guard (assoc guard
                           :x (first next-pos)
                           :y (second next-pos))})))

(defn parse-input [input]
  (let [lines (str/split-lines input)
        width (count (first lines))
        height (count lines)
        initial-state {:width width
                       :height height
                       :guard nil
                       :initial-guard nil
                       :obstacles #{}}]
    (reduce-kv
     (fn [state y line]
       (reduce-kv
        (fn [state x ch]
          (cond
            (= ch \#) (update state :obstacles conj [x y])
            (direction-chars ch) (let [guard {:x x :y y :dir (direction-chars ch)}]
                                   (assoc state
                                          :guard guard
                                          :initial-guard guard))
            :else state))
        state
        (vec line)))
     initial-state
     lines)))

(defn simulate
  ([state]
   (simulate state (fn [_ _] nil)))
  ([state check-for-loop]
   (loop [current-state state
          visited #{[(:x (:guard state)) (:y (:guard state))]}
          loop-positions #{}]
     (if-let [{:keys [guard]} (next-position current-state)]
       (let [next-pos [(:x guard) (:y guard)]
             found-loop? (check-for-loop current-state next-pos)]
         (recur (assoc current-state :guard guard)
                (conj visited next-pos)
                (if found-loop? (conj loop-positions next-pos) loop-positions)))
       {:visited visited :loop-positions loop-positions}))))

(defn simulate-with-loop-detection [state]
  (loop [current-state state
         visited #{(:guard state)}]
    (if-let [{:keys [guard]} (next-position current-state)]
      (if (contains? visited guard)
        {:loop true :visited visited}
        (recur (assoc current-state :guard guard)
               (conj visited guard)))
      {:loop false :visited visited})))

(defn valid-obstacle-position? [{:keys [initial-guard] :as state} [x y]]
  (and (not (out-of-bounds? state [x y]))
       (not (blocked? state [x y]))
       (not (and (= x (:x initial-guard))
                 (= y (:y initial-guard))))))

(defn check-loop-opportunity [state pos]
  (when (valid-obstacle-position? state pos)
    (let [state-with-obstacle (update state :obstacles conj pos)
          reset-state (assoc state-with-obstacle :guard (:initial-guard state-with-obstacle))]
          ;; turned-state (update-in state-with-obstacle [:guard :dir] next-direction)]
      (:loop (simulate-with-loop-detection reset-state)))))

(defn part1 [input]
  (-> input
      parse-input
      simulate
      :visited
      count))

(defn part2 [input]
  (-> input
      parse-input
      (simulate check-loop-opportunity)
      :loop-positions
      count))
