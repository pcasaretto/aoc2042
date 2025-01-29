(ns aoc2024.day7.benchmark-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc2024.day7.exhaustive :as exhaustive]
            [aoc2024.day7.recursive :as recursive]))

(def sample-input "190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20")

(defn benchmark [f input n-times]
  (let [start (System/nanoTime)
        _ (dotimes [_ n-times] (f input))
        end (System/nanoTime)
        total-ms (/ (- end start) 1000000.0)]
    (/ total-ms n-times)))

(deftest benchmark-test
  (testing "comparing implementations"
    (let [n-times 10
          exhaustive-time (benchmark exhaustive/part1 sample-input n-times)
          recursive-time (binding [recursive/*memoize* false]
                           (benchmark recursive/part1 sample-input n-times))
          recursive-memo-time (binding [recursive/*memoize* true]
                                (benchmark recursive/part1 sample-input n-times))]
      (println "\nBenchmark results (average over" n-times "runs):")
      (println "Exhaustive implementation:" exhaustive-time "ms")
      (println "Recursive implementation:" recursive-time "ms")
      (println "Recursive with memoization:" recursive-memo-time "ms")
      (is true)))) 