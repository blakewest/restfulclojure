(ns restful-clojure.lists-test
  (:use clojure.test
        clj-factory.core
        restful-clojure.test-core
        restful-clojure.factories.users)
  (:require [environ.core :refer [env]]
            [restful-clojure.models.users :as users]
            [restful-clojure.models.lists :as lists]
            [restful-clojure.models.products :as products]))

(use-fixtures :each with-rollback)

(deftest add-products
  (let [user (users/create (factory :user {:name "Test user" :email "me@mytest.com"}))
        my-list (lists/create {:user_id (:id user) :title "My list"})
        pintos (products/create {:title "Pinto Beans"
                                 :description "Yummy beans for burritos"})]

    (testing "Adds product to existing list"
      (let [modified-list (lists/add-product my-list (:id pintos))]
        (is (= [pintos] (:products modified-list)))))

    (testing "Creates new list with products"
      (let [listdata (lists/create {:user_id (:id user)
                                    :title "New List"
                                    :products [pintos]})]
        (is (= [pintos] (:products listdata)))))))
