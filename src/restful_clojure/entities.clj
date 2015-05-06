(ns restful-clojure.entities
  (:use korma.core))

(declare users lists products)

(defentity users
  (pk :id)
  (table :users)
  (has-many lists)
  (entity-fields :name :email :password_digest))

(defentity lists
  (pk :id)
  (table :lists)
  (belongs-to users {:fk :user_id})
  (many-to-many products :lists_products {:lfk :list_id
                                          :rfk :product_id})
  (entity-fields :title :products))

(defentity products
  (pk :id)
  (table :products)
  (many-to-many lists :lists_products {:lfk :product_id
                                       :rfk :list_id})
  (entity-fields :title :description))

(defentity tokens
  (pk :id)
  (table :auth_tokens)
  (belongs-to users)
  (entity-fields :id :created_at))

