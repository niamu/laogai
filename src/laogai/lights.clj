(ns laogai.lights
  (:require [laogai.config :refer [config]]
            [clj-http.client :as http]))

(def base
  (str "http://"
       (-> config :hue :addr)
       "/api/"
       (-> config :hue :user)
       "/"))

(defn lights
  []
  (-> config :hue :lights))

(defn state
  [light]
  (:state (:body (http/get (str base "lights/" light) {:as :json}))))

(defn reachable?
  []
  (every? true?
          (map #(:reachable (state %))
               (-> config :hue :lights))))

(defn brightness
  []
  (:brightness (state (first (lights)))))

(defn on?
  []
  (every? true?
          (map #(:on (state %))
               (-> config :hue :lights))))

(defn set!
  [params]
  (doseq [light (-> config :hue :lights)]
    (http/put (str base
                   "lights/"
                   light
                   "/state")
              {:form-params params
               :content-type :json})))
