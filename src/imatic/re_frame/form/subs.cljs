(ns imatic.re-frame.form.subs
  "Subscriptions to extract form data.

  Note that all subscriptions should be dependent on `::form` subscription as form
  data is removed from db once the subscription is no longer used."
  (:require
   [reagent.ratom]
   [re-frame.core :as re-frame]
   [imatic.re-frame.form.model :as model]
   [imatic.re-frame.form.events :as events]))

(re-frame/reg-sub-raw
 ::form
 (fn [db [_ id]]
   (reagent.ratom/make-reaction
    #(get-in @db [::model/form id])
    :on-dispose #(re-frame/dispatch [::events/clear-form id]))))

(re-frame/reg-sub
 ::field-error
 (fn [[_ id]]
   (re-frame/subscribe [::form id]))
 (fn [form [_ id path]]
   (get-in form (flatten [:errors path]))))

(re-frame/reg-sub
 ::field-value
 (fn [[_ id]]
   (re-frame/subscribe [::form id]))
 (fn [form [_ id path]]
   (get-in form (flatten [:data path]))))

(re-frame/reg-sub
 ::field-values
 (fn [[_ id]]
   (re-frame/subscribe [::form id]))
 (fn [form _]
   (:data form)))

(re-frame/reg-sub
 ::initialized?
 (fn [[_ id]]
   (re-frame/subscribe [::form id]))
 (fn [form _]
   (not (nil? form))))
