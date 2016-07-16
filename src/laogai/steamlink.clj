(ns laogai.steamlink
  (:require [laogai.config :refer [config]]
            [clj-http.client :as http]
            [clojure.string :as string]
            [clojure.data.json :as json])
  (:import [java.net InetAddress]))

(def steam (-> config :steam))

(defn on?
  []
  (try
    (.isReachable (InetAddress/getByName (:addr steam)) 1500)
    (catch Exception e false)))

(defn player
  [steamid]
  (-> (http/get (str "http://"
                     (:url steam)
                     "/ISteamUser/GetPlayerSummaries/v0002")
                {:query-params {:key (:key steam)
                                :steamids steamid
                                :format "json"}
                 :retry-handler
                 (fn [ex try-count http-context]
                   (println "Steam Error:" ex)
                   (if (> try-count 4) false true))})
      :body
      (json/read-str :key-fn keyword)
      :response :players first))

(defn playing?
  []
  (and (on?) (:gameid (player (:steamid steam)))))
