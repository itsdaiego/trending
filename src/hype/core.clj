(ns hype.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.client :as http]
            [cheshire.core :as json]))

(defn fetch-repos
  []
  @(http/get "https://api.github.com/search/repositories?q=stars:%3E1&per_page=2"))

(defn get-items
  [content]
  (let [body (json/decode (get content :body))]
    (get body "items")))

(defn parse-data
  [items]
  (map 
    (fn [item] (hash-map :name (get item "name"))) items))

(defn -main
  [& args]
  (-> (fetch-repos)
      (get-items)
      (parse-data)
      (println)))

