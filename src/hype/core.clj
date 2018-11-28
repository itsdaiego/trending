(ns hype.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.client :as http]
            [org.httpkit.server :as server]
            [cheshire.core :as cheshire]))

;; Fetching and parsing Github top repositories ever!

(defn fetch-repos
  []
  @(http/get "https://api.github.com/search/repositories?q=stars:%3E1&per_page=2"))

(defn get-items
  [content]
  (let [body (cheshire/decode (get content :body))]
    (get body "items")))

(defn parse-data
  [items]
  (map 
    (fn 
      [item] 
      (hash-map :name (get item "name"))) 
    items))

(defn get-top-repos
  [& args]
  (-> (fetch-repos)
      (get-items)
      (parse-data)
      (cheshire/generate-string)))


;; Server setup

(defn handler
  [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (get-top-repos)})

(defn create-server
  []
  (server/run-server handler {:port 8080}))

(defn stop-server
  [server]
  (server :timeout 1000))

(defn -main
  [& args]
  (create-server))
