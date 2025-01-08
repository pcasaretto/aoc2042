(ns aoc2024.day3.parser-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc2024.day3.parser :as sut]))

(deftest parsing
  (testing "simple example"
    (is (=
         [{:type :function-call :name "mul" :args [2 5]}]
         (sut/parse [{:type :identifier :value "mul"}
                     {:type :leftParen}
                     {:type :number :value 2}
                     {:type :comma}
                     {:type :number :value 5}
                     {:type :rightParen}])))))
