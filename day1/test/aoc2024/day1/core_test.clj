(ns aoc2024.day1.core-test
  (:require [clojure.test :refer :all]
            [aoc2024.day1.core :as sut]))

(def example-input
  "3   4
4   3
2   5
1   3
3   9
3   3")

(deftest parse-input-test
  (testing "Basic input parsing"
    (is (= [[3 4 2 1 3 3] [4 3 5 3 9 3]]
           (sut/parse-input example-input))))

  (testing "Empty input"
    (is (= [[] []]
           (sut/parse-input ""))))

  (testing "Single line input"
    (is (= [[1] [2]]
           (sut/parse-input "1 2"))))

  (testing "Input with varying whitespace"
    (is (= [[1 2] [3 4]]
           (sut/parse-input "1    3\n2\t\t4"))))

  (testing "Input with negative numbers"
    (is (= [[-1 0] [1 -2]]
           (sut/parse-input "-1 1\n0 -2")))))

(deftest calculate-distance-test
  (testing "Basic distance calculation"
    (is (= 11
           (sut/calculate-distance [3 4 2 1 3 3] [4 3 5 3 9 3]))))

  (testing "Empty lists"
    (is (= 0
           (sut/calculate-distance [] []))))

  (testing "Single element lists"
    (is (= 5
           (sut/calculate-distance [1] [6]))))

  (testing "Lists with negative numbers"
    (is (= 2
           (sut/calculate-distance [-2 0] [0 -4]))))

  (testing "Lists with same numbers in different order"
    (is (= 0
           (sut/calculate-distance [1 2 3] [3 1 2]))))

  (testing "Lists with repeated numbers"
    (is (= 0
           (sut/calculate-distance [1 1 1] [1 1 1])))))

(deftest calculate-similarity-test
  (testing "Basic similarity calculation"
    (is (= 31
           (sut/calculate-similarity [3 4 2 1 3 3] [4 3 5 3 9 3]))))

  (testing "Empty lists"
    (is (= 0
           (sut/calculate-similarity [] []))))

  (testing "No matches"
    (is (= 0
           (sut/calculate-similarity [1 2 3] [4 5 6]))))

  (testing "All matches"
    (is (= 6
           (sut/calculate-similarity [1 2 3] [1 2 3])))))

(deftest part1-test
  (testing "Example case"
    (is (= 11 (sut/part1 example-input)))))

(deftest part2-test
  (testing "Example case"
    (is (= 31 (sut/part2 example-input)))))