(ns laogai.core
  (:require [laogai
             [tv :as tv]
             [lights :as lights]]
            [overtone.at-at :as at-at]))

(defonce cec-pool (at-at/mk-pool))
(defonce light-pool (at-at/mk-pool))

(defn -main
  []
  (prn "The Earth King invites you to Lake Laogai")
  ;; Every 5 seconds, adjust the TV compared to lights
  (at-at/every 5000
               #(if (lights/reachable?)
                  (when (false? (tv/on?))
                    (tv/on!))
                  (when (and (true? (tv/on?))
                             (not (tv/watching?)))
                    (tv/off!)))
               cec-pool
               :desc "HDMI CEC timing pool")
  ;; Every second, check to see if TV is playing (via Plex) and adjust lights
  (at-at/every 1000
               #(when (tv/on?)
                  (if (tv/watching?)
                    (condp = (tv/state)
                      :paused (lights/set! {:on true
                                            :bri 1})
                      :playing (lights/set! {:on false
                                             :bri 1}))
                    (lights/set! {:on true
                                  :bri 254
                                  :transitiontime 300})))
               light-pool
               :desc "Light timing pool"))
