(ns laogai.lights
  (:require [clj-http.client :as http]))

(def hue {:address "10.0.0.6"
          :user "797c7c2144e6f2a1c3d5b5c52163c18"})

(def base
  (str "http://"
       (:address hue)
       "/api/"
       (:user hue)
       "/"))

(defn state
  []
  (:state (:body (http/get (str base "lights/1") {:as :json}))))

(defn on?
  []
  (:on (state)))

(defn set!
  [params]
  (prn "Setting light state: "
       params)
  (http/put (str base "lights/1/state")
            {:form-params params
             :content-type :json}))
