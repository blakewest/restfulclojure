(ns restful-clojure.factories.users
  (:use [clj-factory.core :only [deffactory defseq factory fseq]]))

(defseq :name [n] (str "Justin TimberTest" n))
(defseq :email [n] (str "justin_timbertest" n "@example.com"))

(deffactory :user
  {:name  (fseq :name)
   :email (fseq :email)
   :password "s3cr3t"
   :level :restful-clojure.auth/admin})
