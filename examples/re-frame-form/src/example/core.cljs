(ns example.core
  (:require
   [devcards.core :refer-macros [defcard]]
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [imatic.re-frame.form.events :as ievents]
   [imatic.re-frame.form.subs :as isubs]
   [imatic.re-frame.form.queries :refer [field-values]]))

(defn form-values->form-errors
  "Converts form values into form errors. Returns `nil` if there are no errors."
  [values]
  (when-not (clojure.string/includes? (:email values) "@")
    {:_error "This form contains some errors"
     :email "This value is not valid email address."}))

;; handler of the form
(re-frame/reg-event-fx
 ::submit
 (fn [{:keys [db]} [_ form-id]]
   (let [values (field-values db form-id)]
     (if-let [errors (form-values->form-errors values)]
       {:dispatch [::ievents/update-errors form-id errors]}
       {:dispatch [::ievents/update-errors form-id nil]
        :db (assoc-in db [:form form-id] values)}))))

;; data of the successfully submitted form
(re-frame/reg-sub
 ::form
 (fn [db [_ form-id]]
   (get-in db [:form form-id])))

(defn error
  "Component to render single form error."
  [text]
  [:p {:style {:color "red"}}
   text])

(defn registration-form
  "The actual form."
  []
  (reagent/with-let [form-id :registration
                     ;; initializes form with data
                     _ (re-frame/dispatch [::ievents/init-form {:id form-id
                                                                :initial-data {:email ""}}])
                     initialized? (re-frame/subscribe [::isubs/initialized? form-id])]
    ;; render form only after it is initialized
    ;; (otherwise there will be incorrect default data)
    (when @initialized?
      [:form

       ;; render global form error if any
       (when-let [form-error @(re-frame/subscribe [::isubs/field-error form-id :_error])]
         [error form-error])

       ;; render the email input
       [:label
        "Email "
        [:input {;; do not use `:value` as default value or some inputs might not be registered
                 :default-value @(re-frame/subscribe [::isubs/field-value form-id :email])
                 :type :text
                 ;; updates value of the input in `app-db`
                 :on-change #(re-frame/dispatch [::ievents/update-field
                                                 form-id
                                                 :email
                                                 (.-target.value %)])}]]
       ;; errors specific to the email input if any
       (when-let [email-error @(re-frame/subscribe [::isubs/field-error form-id :email])]
         [error email-error])

       ;; button that submits the form
       [:button {:type "button"
                 :on-click #(re-frame/dispatch [::submit :registration])}
        "Submit"]])))

(defn form-data [form-id]
  [:<>
   [:h3 "Current form data"]
   [:p (with-out-str (prn @(re-frame/subscribe [::isubs/form form-id])))]
   [:h3 "Last successfully submitted data"]
   [:p (with-out-str (prn @(re-frame/subscribe [::form form-id])))]])

(defcard example
  "## Example of form with validation and submit"
  (reagent/as-element [:<>
                       [registration-form]
                       [form-data :registration]]))
