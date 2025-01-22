(ns aoc2024.day5.core-test
  (:require [clojure.test :refer :all]
            [aoc2024.day5.core :as sut]))

(def sample-input "47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47")

(deftest parse-input-test
  (testing "parses rules and updates"
    (let [{:keys [rules updates]} (sut/parse-input sample-input)]
      (testing "rules are parsed as a map of sets"
        (is (contains? (get rules 47) 53))
        (is (contains? (get rules 97) 13))
        (is (contains? (get rules 75) 29)))

      (testing "updates are parsed as vectors of numbers"
        (is (= [75 47 61 53 29] (first updates)))
        (is (= [97 61 53 29 13] (second updates)))
        (is (= [75 29 13] (nth updates 2)))))))

(deftest valid-update?-test
  (let [{:keys [rules]} (sut/parse-input sample-input)]
    (testing "valid updates"
      (is (sut/valid-update? rules [75 47 61 53 29]))
      (is (sut/valid-update? rules [97 61 53 29 13]))
      (is (sut/valid-update? rules [75 29 13])))

    (testing "invalid updates"
      (is (not (sut/valid-update? rules [75 97 47 61 53])) "violates 97|75")
      (is (not (sut/valid-update? rules [61 13 29])) "violates 29|13")
      (is (not (sut/valid-update? rules [97 13 75 29 47])) "violates multiple rules"))))

(deftest sort-update-test
  (let [{:keys [rules]} (sut/parse-input sample-input)]
    (testing "sorts updates according to rules"
      (is (= [97 75 47 61 53] (sut/sort-update rules [75 97 47 61 53])))
      (is (= [61 29 13] (sut/sort-update rules [61 13 29])))
      (is (= [97 75 47 29 13] (sut/sort-update rules [97 13 75 29 47]))))))

(deftest median-test
  (testing "finds middle number in sequence"
    (is (= 61 (sut/median [75 47 61 53 29])))
    (is (= 53 (sut/median [97 61 53 29 13])))
    (is (= 29 (sut/median [75 29 13])))))

(deftest part1-test
  (testing "sample input"
    (is (= 143 (sut/part1 sample-input)))))

(deftest part2-test
  (testing "sample input"
    (is (= 123 (sut/part2 sample-input))))) 