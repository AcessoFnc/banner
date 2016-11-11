(ns banner.validators.fin_move-validator
  (:require [validateur.validation :refer :all]
            [noir.validation :as v]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def client-name-validations
  "Returns a validation set, ensuring an client name is valid."
  (validation-set
   (length-of :client_name :within (range 1 256)
              :message-fn(fn [type m attributes & args]
                           (if (= type :blank)
                             "Client name is required."
                             "Client name must be less than or equal to 255 characters long.")))))

(def fin_move-name-validations
  (validation-set
   (length-of :fin_move_name :within (range 1 256)
              :message-fn (fn [type m attributes & args]
                            (if (= type :blank)
                              "Financial move is required."
                              "Financial move must be less than 255 characters long.")))))

(def release-date-format-message
  "The release date's format is incorrect. Must be yyyy-mm-dd.")

(def release-date-invalid-message
  "The release date is not a valid date.")

(def release-date-format-validator
  "Returns a validator function which ensures the format of the
  date-string is correct."
  (format-of :release_date
             :format #"\d{4}-\d{2}-\d{2}$"
             :blank-message release-date-format-message
             :message release-date-format-message))

(def release-date-formatter
  (f/formatter "yyyy-mm-dd"))

(defn parse-date
  "Retruns a date/time object if the provided date-string is valid;
  otherwise nil."
  [date]
  (try
    (f/parse release-date-formatter date)
    (catch Exception e)))

(def release-date-validator
  "Returns a validator function which ensures the provided
  date--string is a validate date."
  (validate-when #(valid? (validation-set release-date-format-validator) %)
                 (validate-with-predicate :release_date
                                          #(v/not-nil? (parse-date (:release_date %)))
                                          :message release-date-invalid-message)))

(def release-date-validations
  "Returns a validator which, when the format of the date-string
  is correct, ensures the date itself is valid."
  (validation-set release-date-format-validator release-date-validator))

(def validate-new-fin_move
  "Returns a validator that knows how to validate all the fields for a new fin_move."
  (compose-sets client-name-validations fin_move-name-validations release-date-validations))
