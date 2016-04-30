(ns laogai.lights
  (:require [laogai.config :refer [config]]
            [clj-http.client :as http]))

(def hue(-> config :hue))
(def lights (:lights hue))

(defonce last-set (atom {}))

(def base
  (str "http://" (:addr hue) "/api/" (:user hue) "/"))

(defn state
  "Gets the current state of a light by ID"
  [light]
  (:state (:body (http/get (str base "lights/" light) {:as :json}))))

(defn reachable?
  "Returns boolean value dependent on whether some lights are reachable."
  []
  (condp some (map #(:reachable (state %))
                   lights)
    true? true
    false? false
    nil? nil))

(defn on?
  "Returns boolean value dependent on whether all lights are on"
  []
  (every? true?
          (map #(:on (state %)) lights)))

(defn set!
  "For each light in the configuration, set the state from params"
  [params]
  (doseq [light lights]
    ;; Only set a new state if it doesn't match the previously set state
    ;; This will allow for manual changes to lights that won't be overridden
    (when-not (= params (@last-set light))
      (swap! last-set assoc light params)
      (prn "Setting light " light " to: " params)
      (http/put (str base "lights/" light "/state")
                {:form-params params
                 :content-type :json}))))

(defn create-user!
  "Create a new user on the Philips Hue bridge.
   You must press the button on the bridge before executing (30s time limit)"
  []
  (-> (http/post (str "http://" (:addr hue) "/api")
                 {:form-params {:devicetype "laogai#laogai"}
                  :content-type :json
                  :as :json})
      :body
      first))
