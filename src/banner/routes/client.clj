(ns banner.routes.client
  (:require [compojure.core :refer :all]
            [banner.layout :as layout]
            [banner.models.fin_move-model :as fin_move]
            [banner.validators.fin_move-validator :as v]
            [noir.util.route :refer [restricted]]
            [taoensso.timbre :as timbre]))

(defn render-recently-added-html
  "Simply renders the recently added page with the given context."
  [ctx]
  (println ctx)
  (layout/render "client/recently-added.html" ctx))

(defn recently-added-page
  "Renders the recently-added page."
  [username]
  (println "RECENT: " username)
  (render-recently-added-html {:client (fin_move/get-client-ca-by-username {:username username})})
)

(defn render-bnk-sttment-html
  "Simply renders the recently added page with the given context."
  [ctx]
  (println ctx)
  (layout/render "client/bnk-sttment.html" ctx))

(defn bnk-sttment-page
  "Renders the bnk-sttment page."
  []
  (println "EXTRATO: ")
  (render-bnk-sttment-html {:client (fin_move/get-fin-moves-by-username {:username "banner"})})
)

(defn render-get-money-html
  "Simply renders the recently added page with the given context."
  [ctx]
  (println ctx)
  (layout/render "client/get-money.html" ctx))

(defn render-bnk-dpsit-html
  "Simply renders the recently added page with the given context."
  [ctx]
  (println ctx)
  (layout/render "client/bnk-dpsit.html" ctx))

(defn bnk-dpsit-submit
    "Handles the bank deposit form on the bnk-dpsit page.
     In the case of validation errors or other unexpected errors,
     the :new key in the context will be set to the fin move
     information submitted by the user."
    [bank_deposit]
    (println "BANK DEPOSIT SUBMIT: " bank_deposit)
    ;(println [:bank_deposit bank_deposit])
    ;(println {:client {:bank_deposit {:fin_release fin_release
    ;                                  :release_date release_date}}})
    ;(println (merge {:form form}  {:client client} ))
      )

(defn recently-added-submit
  "Handles the add-fin_move form on the recently-added page.
   In the case of validation errors or other unexpected errors,
   the :new key in the context will be set to the fin move
   information submitted by the user."
  [client_par]
  (println "SUBMIT: " client_par)
  (let [form-ctx (try
                     (fin_move/ret-cli client_par)
                     (catch Exception e
                       (timbre/error e)
                     )
                 )
        ctx (merge {:form form-ctx} {:client (fin_move/get-client-by-username)})]
    (render-recently-added-html ctx)))

(defn client-fin_moves-page
  "Renders the fin_moves for a given client."
  [client]
  (println "CLIENT-FIN-MOVES-PAGE: " client)
  (layout/render "fin_moves/client-fin_moves.html"
                 {:client client
                  :fin_moves (fin_move/get-by-client {:client client})}))

(defroutes client-routes
  (GET "/client/recently-added/:username" [username] (restricted (recently-added-page username)))
  (POST "/client/recently-added" [& client-form] (restricted (recently-added-submit client-form)))
  (GET "/client/bnk-sttment" [] (bnk-sttment-page))
  (GET "/client/bnk-dpsit" [& client-form] (restricted (render-bnk-dpsit-html client-form)))

  (GET "/client/bnk-dpsit/submit" [& client-form] (restricted (bnk-dpsit-submit client-form)))
  ; 31.10, 11h30: incluí &

  (GET "/client/get-money" [& client-form] (restricted (render-get-money-html client-form)))
  (GET "/client/:client" [client] (client-fin_moves-page client))
)

;(GET "/client/bnk-sttment" [] (bnk-sttment-page))
