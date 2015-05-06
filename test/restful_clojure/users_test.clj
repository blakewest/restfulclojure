(ns restful-clojure.users-test
  (:use clojure.test
        clj-factory.core
        restful-clojure.test-core
        restful-clojure.factories.users)
  (:require [restful-clojure.models.users :as users]
            [environ.core :refer [env]]))

(use-fixtures :each with-rollback)

(deftest create-read-users
  (testing "Create a user"
    (let [orig-count (users/count-users)]
      (users/create (factory :user))
      (is (= (inc orig-count) (users/count-users)))))
  (testing "Retrieve a user"
    (let [orig-user (users/create (factory :user {:name "Existing" :email "existing@example.com"}))
          found-user (users/find-by-id (:id orig-user))]
      (is (= (:name found-user) "Existing"))
      (is (= (:email found-user) "existing@example.com")))))


(deftest update-user
  (testing "Update a user"
    (let [orig-user (users/create (factory :user))
          user-id (:id orig-user)]
      (users/update-user {:name "Not Joe", :id (:id orig-user)})
      (is (= (:name (users/find-by-id (:id orig-user))) "Not Joe")))))

(deftest authorize-user
  (let [user (users/create (factory :user {:password "s3cr3t"}))
        user-id (:id user)]
    (testing "correct passwords"
      (is (users/password-matches? user-id "s3cr3t")))
    (testing "wrong passwords"
      (is (not (users/password-matches? user-id "wrong password"))))))
