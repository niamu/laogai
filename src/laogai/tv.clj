(ns laogai.tv
  (:require [laogai.config :refer [config]]
            [clojure.string :as string]
            [me.raynes.conch :refer [with-programs]]))

(def rpi
  (-> config :rpi))

(defonce power-state
  (atom {:on nil}))

(defn state
  []
  (with-programs [ssh]
    (-> (ssh (:addr rpi) "./tv.sh state")
        (string/split #":")
        last
        (string/trim)
        keyword)))

(defn init
  []
  (swap! power-state assoc :on (= :on (state))))

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
