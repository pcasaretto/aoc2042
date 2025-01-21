(ns aoc2024.day4.core-test
  (:require [clojure.test :refer :all]
            [aoc2024.day4.core :as sut]))

(def sample-input "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX")

(def small-grid
  [[\X \M \A \S]
   [\M \X \M \A]
   [\A \M \X \S]
   [\S \A \M \X]])

(def diagonal-grid
  [[\X \A \M \S \M]
   [\S \M \A \M \S]
   [\M \A \A \S \A]
   [\S \M \M \S \S]
   [\M \S \A \X \X]])

(def x-mas-sample
  ".M.S......
..A..MSMS.
.M.S.MAA..
..A.ASMSM.
.M.S.M....
..........
S.S.S.S.S.
.A.A.A.A..
M.M.M.M.M.
..........")

(def mixed-pattern
  "M.M
.A.
S.S")

(deftest parse-grid-test
  (testing "parses input into grid"
    (let [grid (sut/parse-grid sample-input)]
      (is (= 10 (count grid)))
      (is (= 10 (count (first grid))))
      (is (= [\M \M \M \S \X \X \M \A \S \M] (first grid)))
      (is (= [\M \X \M \X \A \X \M \A \S \X] (last grid))))))

(deftest in-bounds?-test
  (testing "in-bounds checks"
    (is (true? (sut/in-bounds? small-grid 0 0)))
    (is (true? (sut/in-bounds? small-grid 3 3)))
    (is (false? (sut/in-bounds? small-grid -1 0)))
    (is (false? (sut/in-bounds? small-grid 0 4)))
    (is (false? (sut/in-bounds? small-grid 4 0)))))

(deftest check-direction-test
  (testing "finds XMAS in any direction (horizontal, vertical, diagonal, forwards or backwards)"
    ; right direction [0 1]
    (is (sut/check-direction small-grid [0 0] [0 1]))
    ; left direction [0 -1]
    (is (sut/check-direction small-grid [3 3] [0 -1]))
    ; down direction [1 0]
    (is (sut/check-direction small-grid [0 0] [1 0]))
    ; not XMAS cases
    (is (not (sut/check-direction small-grid [0 1] [0 1])))
    (is (not (sut/check-direction small-grid [1 1] [1 0])))
    ; out of bounds cases
    (is (not (sut/check-direction small-grid [0 1] [0 -1])))
    (is (not (sut/check-direction small-grid [3 0] [1 0])))
    ; diagonal cases (which should all be false in small-grid)
    (is (not (sut/check-direction small-grid [0 0] [1 1])))
    (is (not (sut/check-direction small-grid [0 3] [1 -1])))
    (is (not (sut/check-direction small-grid [3 0] [-1 1])))
    (is (not (sut/check-direction small-grid [3 3] [-1 -1]))))

  (testing "finds diagonal XMAS patterns"
    ; down-right diagonal [1 1]
    (is (sut/check-direction diagonal-grid [0 0] [1 1]))
    ; up-left diagonal [-1 -1] starting from bottom-right X
    (is (sut/check-direction diagonal-grid [4 3] [-1 -1]))))

(deftest count-xmas-test
  (testing "counts XMAS in small grid (in any direction)"
    (is (= 3 (sut/count-xmas small-grid))))
  (testing "counts XMAS in diagonal grid"
    (is (= 2 (sut/count-xmas diagonal-grid))))
  (testing "counts XMAS in sample input"
    (is (= 18 (sut/count-xmas (sut/parse-grid sample-input))))))

(deftest part1-test
  (testing "sample input"
    (is (= 18 (sut/part1 sample-input)))))

(deftest check-x-mas-test
  (testing "finds complete X-MAS patterns"
    (let [grid (sut/parse-grid x-mas-sample)]
      (is (sut/check-x-mas grid [1 2])))) ; complete X-MAS at position [1,2]

  (testing "should not match mixed MAS/SAM patterns"
    (let [grid (sut/parse-grid mixed-pattern)]
      ; This pattern has MAS in one diagonal and SAM in the other
      ; It should not be considered valid
      (is (not (sut/check-x-mas grid [1 2]))))))

(deftest part2-test
  (testing "counts all X-MAS patterns in the grid"
    (is (= 9 (sut/part2 x-mas-sample))))) 