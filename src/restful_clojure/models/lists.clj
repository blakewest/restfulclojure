(ns restful-clojure.models.lists
  (use korma.core)
  (require [restful-clojure.entities :as e]))

(declare add-product)

(defn find-by [field value]
  (first
    (select e/lists
      (with e/products)
      (where {field value})
      (limit 1))))

(defn find-all-by [field value]
  (select e/lists
    (with e/products)
    (where {field value})))

(defn find-by-id [id]
  (find-by :id id))

(defn create [listItem]
  (let [newlist (insert e/lists
                  (values (dissoc listItem :products)))]
    (doseq [product (:products listItem)]
      (add-product newlist (:id product)))
    (assoc newlist :products (into [] (:products listItem)))))

(defn find-all []
  (select e/lists))

(defn delete-list [listItem]
  (delete e/lists
    (where {:id (:id listItem)})))

(defn update-list [listItem]
  (update e/lists
    (set-fields (dissoc listItem :id))
    (where {:id {listItem :id}})))

(defn add-product
  "Add a product to a list with an optional status arg"
  ([listdata product-id]
    (add-product listdata product-id "incomplete"))
  ([listdata product-id status]
    (let [sql (str "INSERT INTO lists_products ("
                   "list_id, product_id, status)"
                   "VALUES (?, ?, ?::item_status)")]
      (exec-raw [sql [(:id listdata) product-id status] :results])
      (find-by-id (:id listdata)))))

(defn remove-product [listdata product_id]
  ; "Remove a product from a list"
  (delete "lists_products"
    (where {:list_id (:id listdata)
            :product_id product_id})
    (update-in listdata [:products]
      (fn [products] (remove #(= product_id (:id %)) products)))))

