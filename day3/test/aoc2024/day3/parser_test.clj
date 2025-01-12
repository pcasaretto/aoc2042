(ns aoc2024.day3.parser-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc2024.day3.lexer :as lexer]
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

(def example-input "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")

(deftest example-test
  (testing "Parsing example input"
    (is (= [{:type :function-call :name "xmul" :args [2 4]}
            {:type :function-call :name "do_not_mul" :args [5 5]}
            {:type :function-call :name "mul" :args [11 8]}
            {:type :function-call :name "mul" :args [8 5]}]
           (sut/parse (lexer/lex example-input))))))
