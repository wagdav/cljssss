(ns thewagner.cljssss.search
  (:require [clojure.core.async :as async]))

(defn iterative-deepening
  "Iterative depeening search  (See Russel & Norvig: Artificial Intelligence p. 89)"
  [depth-limited-search timeout-ms]
  (let [timeout (async/timeout timeout-ms)]
    (loop [result nil
           depth 0
           next-iteration (async/go (depth-limited-search depth))]
      (let [[v ch] (async/alts!! [timeout next-iteration])]
        (if (= ch timeout)
          result
          (recur v
                 (inc depth)
                 (async/go (depth-limited-search (inc depth)))))))))

(comment
  (time (iterative-deepening #(identity %) 50)))
