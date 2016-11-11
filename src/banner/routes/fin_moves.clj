(ns banner.routes.fin_moves
  (:require [compojure.core :refer :all]
            [banner.layout :as layout]
            [banner.models.fin_move-model :as fin_move]
            [banner.validators.fin_move-validator :as v]
            [noir.util.route :refer [restricted]]
            [taoensso.timbre :as timbre]))

(defn render-recently-added-html
  "Simply renders the recently added page with the given context."
  [ctx]
  (layout/render "fin_moves/recently-added.html" ctx))

(defn recently-added-page
  "Renders the recently-added page."
  []
  (render-recently-added-html {:fin_moves (fin_move/get-recently-added)}))

(defn recently-added-submit
  "Handles the add-fin_move form on the recently-added page.
   In the case of validation errors or other unexpected errors,
   the :new key in the context will be set to the fin move
   information submitted by the user."
  [fin_move]
  (let [errors (v/validate-new-fin_move fin_move)
        form-ctx (if (not-empty errors)
                   {:validation-errors errors :new fin_move}
                   (try
                     (fin_move/add-fin_move! fin_move)
                     {:new {} :success true}
                     (catch Exception e
                       (timbre/error e)
                       {:new fin_move
                        :error "Lancamento perdido. Tentar novamente?"})))
        ctx (merge {:form form-ctx} {:fin_moves (fin_move/get-recently-added)})]
    (render-recently-added-html ctx)))

(defn client-fin_moves-page
  "Renders the fin_moves for a given client."
  [client]
  (layout/render "fin_moves/client-fin_moves.html"
                 {:client client
                  :fin_moves (fin_move/get-by-client {:client client})}))

(defroutes fin_move-routes
  (GET "/fin_moves/recently-added" [] (restricted (recently-added-page)))
  (POST "/fin_moves/recently-added" [& fin_move-form] (restricted (recently-added-submit fin_move-form)))
  (GET "/fin_moves/:client" [client] (client-fin_moves-page client)))
