(ns imatic.re-frame.form.events
  (:require
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]
   [imatic.re-frame.form.model :as model]))

(re-frame/reg-event-db
 ::init-form
 (fn-traced [db [_ {:keys [id initial-data]}]]
            (if (get-in db [::model/form id :data])
              db
              (assoc-in db [::model/form id :data] initial-data))))

(re-frame/reg-event-db
 ::clear-form
 (fn-traced [db [_ id]]
            (update db ::model/form dissoc id)))

(re-frame/reg-event-db
 ::clear-field
 (fn-traced [db [_ form-id path]]
            (let [normalized-path (flatten [path])
                  field-parent (concat [::model/form form-id :data]
                                       (rest normalized-path))
                  field-name (last normalized-path)]
              (update-in db
                         field-parent
                         dissoc
                         field-name))))

(re-frame/reg-event-db
 ::update-field
 (fn-traced [db [_ form-id path value]]
            (assoc-in db (flatten [::model/form form-id :data path]) value)))

(re-frame/reg-event-db
 ::update-errors
 (fn-traced [db [_ form-id errors]]
            (assoc-in db [::model/form form-id :errors] errors)))
