(ns aoc2024.day3.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc2024.day3.core :as sut]))

(def example-input "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")

(deftest example-test
  (testing "example"
    (is (= 161 (sut/part1 example-input)))))