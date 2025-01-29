(ns aoc2024.day7.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc2024.day7.core :as sut]))

(def sample-input "190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20")

(deftest parse-input-test
  (testing "parses input correctly"
    (let [parsed (sut/parse-input sample-input)]
      (is (= {:target 190 :numbers [10 19]} (first parsed)))
      (is (= {:target 3267 :numbers [81 40 27]} (second parsed))))))

(deftest can-make-target?-test
  (testing "simple cases"
    (is (sut/can-make-target? 190 [10 19]))
    (is (sut/can-make-target? 3267 [81 40 27]))
    (is (not (sut/can-make-target? 83 [17 5])))
    (is (sut/can-make-target? 292 [11 6 16 20]))))

(deftest part1-test
  (testing "part1"
    (is (nil? (sut/part1 nil)))))

(deftest part2-test
  (testing "part2"
    (is (nil? (sut/part2 nil))))) 