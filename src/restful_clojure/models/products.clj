(ns restful-clojure.models.products
  (use korma.core)
  (require [restful-clojure.entities :as e]))

(defn create [product]
  (insert e/products
    (values product)))

(defn find-all []
  (select e/products))

(defn find-by [field value]
  (first
    (select e/products
      (where {field value})
      (limit 1))))

(defn update-product [product]
  (update e/products
    (set-fields (dissoc product :id))
    (where {:id (:id product)})))

(defn delete-product [product]
  (delete e/products
    (where {:id (:id product)})))
