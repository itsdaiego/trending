(ns hype.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.client :as http]
            [cheshire.core :refer :all]))

(defn fetchRepos
  []
  @(http/get "https://api.github.com/search/repositories?q=stars:%3E1&per_page=2"))

(defn parse
  [content]
  (let [body (parse-string (get content :body))]
    (println (type body))
    (println (get body :total_count))))

(defn -main
  [& args]
  (-> (fetchRepos)
      (parse)))

