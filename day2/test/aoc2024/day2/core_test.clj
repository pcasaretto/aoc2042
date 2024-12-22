(ns aoc2024.day2.core-test
  (:require [clojure.test :refer :all]
            [aoc2024.day2.core :as sut]))

(def example-input
  "7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9")

(deftest parse-input-test
  (testing "Basic input parsing"
    (is (= [[7 6 4 2 1]
            [1 2 7 8 9]
            [9 7 6 2 1]
            [1 3 2 4 5]
            [8 6 4 4 1]
            [1 3 6 7 9]]
           (sut/parse-input example-input)))))

(deftest valid-sequence?-test
  (testing "Valid sequences"
    (is (true? (sut/valid-sequence? [7 6 4 2 1])) "All decreasing by 1-3")
    (is (true? (sut/valid-sequence? [1 3 6 7 9])) "All increasing by 1-3"))
  
  (testing "Invalid sequences"
    (is (false? (sut/valid-sequence? [1 2 7 8 9])) "Increase by more than 3")
    (is (false? (sut/valid-sequence? [9 7 6 2 1])) "Decrease by more than 3")
    (is (false? (sut/valid-sequence? [1 3 2 4 5])) "Changes direction")
    (is (false? (sut/valid-sequence? [8 6 4 4 1])) "No change between numbers")))

(deftest valid-with-dampener?-test
  (testing "Already valid sequences"
    (is (true? (sut/valid-with-dampener? [7 6 4 2 1])) "All decreasing by 1-3")
    (is (true? (sut/valid-with-dampener? [1 3 6 7 9])) "All increasing by 1-3"))
  
  (testing "Sequences that become valid after removing one number"
    (is (true? (sut/valid-with-dampener? [1 3 2 4 5])) "Valid after removing 3")
    (is (true? (sut/valid-with-dampener? [8 6 4 4 1])) "Valid after removing middle 4"))
  
  (testing "Sequences that remain invalid"
    (is (false? (sut/valid-with-dampener? [1 2 7 8 9])) "Can't fix big jump")
    (is (false? (sut/valid-with-dampener? [9 7 6 2 1])) "Can't fix big drop")))

(deftest part1-test
  (testing "Example case"
    (is (= 2 (sut/part1 example-input)))))

(deftest part2-test
  (testing "Example case"
    (is (= 4 (sut/part2 example-input))))) 