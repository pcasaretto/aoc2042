(ns aoc2024.day3.eval)

(defn eval
  [instructions]
  (map (fn [instruction] (apply * (:args instruction))) instructions))

(defn eval2
  [instructions fns context]
  (reduce (fn [context instruction]
            (if-let [fn (get fns (:name instruction))]
              (fn (:args instruction) context)
              (throw (Exception. (str "Function not found: " instruction)))))
          context instructions))
