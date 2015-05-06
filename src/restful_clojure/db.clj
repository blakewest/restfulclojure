(ns restful-clojure.db
  (:use korma.db)
  (:require [environ.core :refer [env]]))

(defdb db (postgres {:db (get env :restful-db (str "restful_clj" env))
                     :user (get env :restful-db-user (str "restful_clj" env))
                     :password (get env :restful-db-pass "")
                     :host (get env :restful-db-host "localhost")
                     :port (get env :restful-db-port 5432)}))
