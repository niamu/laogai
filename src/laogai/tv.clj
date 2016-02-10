(ns laogai.tv
  (:require [laogai.config :refer [config]]
            [me.raynes.conch :refer [with-programs]]))

(def rpi
  (-> config :rpi))

(defonce power-state
  (atom {:on false}))

(defn on?
  "Returns boolean value of current TV power state"
  []
  (:on @power-state))

(defn on!
  "Turn the TV on via SSH command to the Raspberry Pi"
  []
  (when (false? (on?))
    (prn "Turning TV on...")
    (swap! power-state assoc :on true)
    (with-programs [ssh]
      (ssh (:addr rpi) "./tv.sh on"))))

(defn off!
  "Turn the TV off via SSH command to the Raspberry Pi"
  []
  (when (true? (on?))
    (prn "Turning TV off...")
    (swap! power-state assoc :on false)
    (with-programs [ssh]
      (ssh (:addr rpi) "./tv.sh off"))))
