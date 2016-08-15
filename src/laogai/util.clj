(ns laogai.util
  (:import [java.net InetAddress]))

(defn on?
  [addr]
  (try
    (.isReachable (InetAddress/getByName addr) 200)
    (catch Exception e false)))
