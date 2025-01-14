(ns aoc2024.day3.eval-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc2024.day3.lexer :as lexer]
            [aoc2024.day3.parser :as parser]
            [aoc2024.day3.eval :as eval]))

(deftest parsing
  (testing "simple example"
    (is (= [10]
           (eval/eval [{:type :function-call :name "mul" :args [2 5]}])))))

(def example-input "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")

(deftest example-test
  (testing "Parsing example input"
    (is (= [8 25 88 40]
           (eval/eval (parser/parse (lexer/lex example-input)))))))
