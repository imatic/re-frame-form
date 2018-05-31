(ns imatic.re-frame.form.queries
  (:require
   [imatic.re-frame.form.model :as model]))

(defn field-values [db form-id]
  (get-in db [::model/form form-id :data]))
