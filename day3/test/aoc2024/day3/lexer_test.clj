(ns aoc2024.day3.lexer-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc2024.day3.lexer :as sut]))

(deftest lex-test
  (testing "Simple multiplication"
    (is (= [{:type :identifier :value "mul"}
            {:type :leftParen}
            {:type :number :value 2}
            {:type :comma}
            {:type :number :value 4}
            {:type :rightParen}]
           (sut/lex "mul(2,4)"))))

  (testing "Identifiers"
    (is (= [{:type :identifier :value "do_not_mul"}]
           (sut/lex "do_not_mul")))
    (is (= [{:type :identifier :value "x123"}]
           (sut/lex "x123"))))

  (testing "Numbers"
    (is (= [{:type :number :value 123}]
           (sut/lex "123")))
    (is (= [{:type :number :value 4}]
           (sut/lex "4"))))

  (testing "Mixed content"
    (is (= [{:type :identifier :value "mul"}
            {:type :leftParen}
            {:type :number :value 123}
            {:type :comma}
            {:type :number :value 456}
            {:type :rightParen}]
           (sut/lex "mul(123,456)")))

    (is (= [{:type :identifier :value "mul"}
            {:type :leftBracket}  ; note: different bracket type
            {:type :number :value 2}
            {:type :comma}
            {:type :number :value 4}
            {:type :rightBracket}]
           (sut/lex "mul[2,4]")))))

(def example-input "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")

(deftest example-test
  (testing "Lexing example input"
    (is (= [{:type :identifier :value "xmul"}
            {:type :leftParen}
            {:type :number :value 2}
            {:type :comma}
            {:type :number :value 4}
            {:type :rightParen}
            {:type :identifier :value "mul"}
            {:type :leftBracket}
            {:type :number :value 3}
            {:type :comma}
            {:type :number :value 7}
            {:type :rightBracket}
            {:type :identifier :value "do_not_mul"}
            {:type :leftParen}
            {:type :number :value 5}
            {:type :comma}
            {:type :number :value 5}
            {:type :rightParen}
            {:type :identifier :value "mul"}
            {:type :leftParen}
            {:type :number :value 32}
            {:type :comma}
            {:type :number :value 64}
            {:type :rightBracket}
            {:type :identifier :value "then"}
            {:type :leftParen}
            {:type :identifier :value "mul"}
            {:type :leftParen}
            {:type :number :value 11}
            {:type :comma}
            {:type :number :value 8}
            {:type :rightParen}
            {:type :identifier :value "mul"}
            {:type :leftParen}
            {:type :number :value 8}
            {:type :comma}
            {:type :number :value 5}
            {:type :rightParen}
            {:type :rightParen}]
           (sut/lex example-input))))) 
