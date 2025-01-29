(ns aoc2024.day6.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc2024.day6.core :as sut]))

(def sample-input "....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...")

(deftest parse-input-test
  (testing "parses board dimensions and positions"
    (let [{:keys [width height guard obstacles]} (sut/parse-input sample-input)]
      (testing "board dimensions"
        (is (= 10 width))
        (is (= 10 height)))

      (testing "guard position and direction"
        (is (= {:x 4 :y 6 :dir :up} guard)))

      (testing "obstacle positions"
        (is (contains? obstacles [4 0]))  ; top row obstacle
        (is (contains? obstacles [9 1]))  ; second row obstacle
        (is (contains? obstacles [2 3]))  ; fourth row obstacle
        (is (= 8 (count obstacles)))))))

(deftest part1-test
  (testing "sample input"
    (is (= 41 (sut/part1 sample-input)))))

(deftest part2-test
  (testing "sample input"
    (is (= 6 (sut/part2 sample-input)))))

(deftest next-position-test
  (let [state {:width 10
               :height 10
               :obstacles #{[4 0] [9 1] [2 3]}
               :guard {:x 4 :y 6 :dir :up}}]

    (testing "turns right when hitting obstacle"
      (is (= {:guard {:x 4 :y 1 :dir :right}
              :hit [4 0]}
             (sut/next-position (assoc-in state [:guard] {:x 4 :y 1 :dir :up})))))

    (testing "moves forward when no obstacle"
      (is (= {:guard {:x 4 :y 5 :dir :up}}
             (sut/next-position state))))

    (testing "returns nil when going out of bounds"
      (is (nil? (sut/next-position (assoc-in state [:guard] {:x 9 :y 1 :dir :right}))))
      (is (nil? (sut/next-position (assoc-in state [:guard] {:x 0 :y 0 :dir :up})))))))

(def simple-loop
  ".#..
.>.#
#...
..#.")

(def simple-no-loop
  "....
.>.#
#...
..#.")

(deftest simulate-with-loop-detection-test
  (testing "detects a simple loop"
    (let [input simple-loop
          state (sut/parse-input input)
          result (sut/simulate-with-loop-detection state)]
      (is (:loop result))))

  (testing "detects when there is no loop"
    (let [input simple-no-loop
          state (sut/parse-input input)
          result (sut/simulate-with-loop-detection state)]
      (is (not (:loop result))))))

(deftest valid-obstacle-position-test
  (let [state {:width 4
               :height 4
               :guard {:x 1 :y 1 :dir :right}
               :initial-guard {:x 1 :y 0 :dir :right}
               :obstacles #{[0 0] [2 2]}}]

    (testing "valid positions"
      (is (sut/valid-obstacle-position? state [1 2]))
      (is (sut/valid-obstacle-position? state [3 3])))

    (testing "invalid positions"
      (is (not (sut/valid-obstacle-position? state [0 0]))) ; existing obstacle
      (is (sut/valid-obstacle-position? state [1 1])) ; current guard position
      (is (not (sut/valid-obstacle-position? state [1 0]))) ; initial guard position
      (is (not (sut/valid-obstacle-position? state [4 0]))) ; out of bounds
      (is (not (sut/valid-obstacle-position? state [-1 0]))))))

(def simple-loop-when-obstacle
  ".#..
..>.
#...
..#.")

(deftest check-loop-opportunity-test
  (testing "using simple-loop example"
    (let [state (sut/parse-input simple-loop-when-obstacle)
          guard (:guard state)
          next-pos (sut/next-coord guard)]
      (is (sut/check-loop-opportunity state next-pos))))

  (testing "using simple-no-loop example"
    (let [state (sut/parse-input simple-no-loop)
          guard (:guard state)
          next-pos (sut/next-coord guard)]
      (is (not (sut/check-loop-opportunity state next-pos))))))
