(ns thewagner.cljssss.snake)

(defn self-collision? [{:keys [you]}]
  (let [head (you :head)
        body (you :body)]
    (contains? (into #{} body) head)))

(defn snake-collision? [{:keys [you board]}]
  (let [head (you :head)
        id (you :id)
        snakes (filter #(not= id (:id %)) (board :snakes))]
    (contains? (into #{} (apply concat (map :body snakes)))
               head)))

(defn wall-collision? [state]
  (let [width (get-in state [:board :width])
        height (get-in state [:board :height])
        head (get-in state [:you :head])
        max-x (dec width)
        max-y (dec height)]
    (not (and (<= 0 (head :x) max-x)
              (<= 0 (head :y) max-y)))))

(defn food? [{:keys [board you]}]
  (some #{(you :head)} (:food board)))

(defn actions
  "Given the game-state return the set of legal moves (new head positions)"
  [state]
  (let [head (get-in state [:you :head])]
    #{(update head :x inc)    ; right
      (update head :x dec)    ; left
      (update head :y inc)    ; up
      (update head :y dec)})) ; down

(defn result
  "Transition model: return the result of a move"
  [state action]
  (assoc-in state [:you :head] action))

(defn utility [state]
  (cond
    (self-collision? state)  -1
    (wall-collision? state)  -1
    (snake-collision? state) -1
    (food? state)            10
    :else (rand 5)))

(defn cutoff? [state depth]
  (>= depth 0))

(defn max-value
  ([state]
   (max-value state 0))
  ([state depth]
   (if (cutoff? state depth)
     (utility state)
     (->> (actions state)
          (map #(result state %))
          (map #(max-value % (inc depth)))
          (apply max)))))

(defn minimax-decision  ; will use α-β pruning later
  ([state]
   (->> (actions state)
        (map #(result state %))
        (apply max-key max-value))))

(defn direction [p1 p2]
  (if (= (:y p1) (:y p2))
    (if (< (:x p1) (:x p2))
      "right"
      "left")
    (if (< (:y p1) (:y p2))
      "up"
      "down")))

(defn move
  "Given a game board return the next move"
  [{:keys [you] :as state}]
  (let [head (:head you)
        s (minimax-decision state)
        new-head (get-in s [:you :head])
        dir (direction head new-head)]
   {:move dir}))

(comment
  (def example-state {:game {}
                      :board {:width 11
                              :height 11
                              :snakes [{:id 2 :body [{:x 3 :y 4}]}]}
                      :turn 0
                      :you {:id 1
                            :head {:x 4 :y 4}
                            :body [{:x 1 :y 0}]}})
  (actions example-state)
  (result example-state {:x 10 :y 10})
  (wall-collision? example-state)
  (snake-collision? example-state)
  (utility example-state)
  (move example-state))
