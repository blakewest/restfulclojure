(defproject restful-clojure "0.1.0-SNAPSHOT"
  :description "Restful Clojure Tutorial"
  :url "http://localhost:3000"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [cheshire "5.3.1"]
                 [ring/ring-json "0.2.0"]
                 [korma "0.3.0-RC5"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [ragtime "0.3.4"]
                 [compojure "1.1.6"]
                 [environ "0.4.0"]
                 [buddy/buddy-hashers "0.4.0"]
                 [buddy/buddy-auth "0.4.0"]
                 [crypto-random "1.2.0"]
                 [clj-factory "0.2.1"]]
  :plugins[[lein-ring "0.8.10"]
           [ragtime/ragtime.lein "0.3.6"]
           [com.jakemccrary/lein-test-refresh "0.9.0"]
           [lein-environ "0.4.0"]]

  :ring {:handler restful-clojure.handler/app
         :nrepl {:start? true
                 :port 9998}}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]
         :env {:restful-db "restful_clj_dev"
               :restful-db-user "restful_clj_dev"
               :restful-db-pass nil}}
   :test {:ragtime {:database "jdbc:postgresql://localhost:5432/restful_clj_test?user=restful_clj_test"}
          :env {:restful-db "restful_clj_test"
                :restful-db-user "restful_clj_test"
                :restful-db-pass nil}}}


  :ragtime {:migrations ragtime.sql.files/migrations
            :database "jdbc:postgresql://localhost:5432/restful_clj_dev?user=restful_clj_dev"})
