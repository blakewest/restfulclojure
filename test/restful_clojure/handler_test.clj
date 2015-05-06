(ns restful-clojure.handler-test
  (:use clojure.test
        clj-factory.core
        ring.mock.request
        restful-clojure.test-core
        restful-clojure.handler
        restful-clojure.factories.users)
  (:require [restful-clojure.models.users :as users]
            [restful-clojure.auth :as auth]
            [cheshire.core :as json]
            [restful-clojure.models.products :as products]
            [restful-clojure.models.lists :as lists]))

(def ^{:dynamic true} *session-id* nil)

(defn with-session [test]
  (let [user (users/create (factory :user))
        session-id (auth/make-token! (:id user))]
    (with-bindings {#'*session-id* session-id} (test))
    (users/delete-user user)))

(use-fixtures :each with-rollback)
(use-fixtures :once with-session)

(defn with-auth-header [req]
  (header req "Authorization" (str "Token " *session-id*)))

(deftest users-test
  (testing "users endpoint"
    (repeatedly 4 (users/create (factory :user)))
    (let [response (app (with-auth-header (request :get "/users")))
          response-data (json/parse-string (:body response) true)]
      (println (:email (first (:results response-data))))
      (is (= (:status response) 200))
      (is (= (get-in response [:headers "Content-Type"]) "application/json; charset=utf-8"))
      (is (substring? "@example.com" (:email (first (:results response-data)))))))
  (testing "creating a user"
    (let [user-count (users/count-users)
          response (app (-> (request :post "/users")
                            with-auth-header
                            (body (json/generate-string (factory :user)))
                            (content-type "application/json")
                            (header "Accept" "application/json")))]
      (is (= (:status response) 201))
      (is (= (inc user-count) (users/count-users)))))
  (testing "getting one user"
    (let [user (users/create (factory :user))
          response (app (with-auth-header (request :get (str "/users/" (:id user)))))
          response-data (json/parse-string (:body response) true)]
      (is (= (:name response-data) (:name user))))))

; (deftest not-found-route
;   (testing "not found route"
;     (let [response (app (request :get "/BADR_oUte"))]
;     (is (= (:status response) 404)))))
