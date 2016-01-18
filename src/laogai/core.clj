(ns laogai.core
  (:require [laogai
             [tv :as tv]
             [lights :as lights]]
            [overtone.at-at :as at-at]))

(defonce time-pool (at-at/mk-pool))

(defn -main
  []
  (prn "The Earth King invites you to Lake Laogai")
  (at-at/every 1000
               #(do
                  (if (:reachable (lights/state))
                    (when (not (tv/on?))
                      (tv/on!))
                    (when (and (tv/on?)
                               (not (tv/watching?)))
                      (tv/off!)))
                  (if (tv/watching?)
                    (do (when (and (= (tv/state) :paused)
                                   (or (not= 1 (:bri (lights/state)))
                                       (not (lights/on?))))
                          (lights/set! {:on true
                                        :bri 1}))
                        (when (and (= (tv/state) :playing)
                                   (lights/on?))
                          (lights/set! {:on false
                                        :bri 1})))
                    (when (or (not= 254 (:bri (lights/state)))
                              (not (lights/on?)))
                      (when (not (lights/on?))
                        (lights/set! {:on true
                                      :bri 1}))
                      (lights/set! {:on true
                                    :bri 254
                                    :transitiontime 300}))))
               time-pool
               :desc "Timing pool"))
