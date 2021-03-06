(ns banner.handler
  (:require [compojure.core :refer [defroutes]]
            [banner.models.connection :refer [db-spec]]
            [banner.routes.access :as access]
            [banner.routes.fin_moves :refer [fin_move-routes]]
            [banner.routes.client :refer [client-routes]]
            [banner.routes.home :refer [home-routes]]
            [banner.routes.test-routes :refer [test-routes]]
            [banner.middleware :refer [load-middleware]]
            [banner.session-manager :as session-manager]
            [migratus.core :as migratus]
            [noir.response :refer [redirect]]
            [noir.util.middleware :refer [app-handler]]
            [ring.middleware.defaults :refer [site-defaults]]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rolling :as rolling]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [cronj.core :as cronj]))

(def migratus-config
  {:store :database
   :migration-dir "migrations"
   :migration-table-name "_migrations"
   :db db-spec})

(defroutes base-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn migrate-db []
  (timbre/info "checking migrations")
  (try
    (migratus/migrate migratus-config)
    (catch Exception e
      (timbre/error e)))
  (timbre/info "finished migrations"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
   [:appenders :rolling]
   (rolling/make-rolling-appender {:min-level :info}))

  (timbre/set-config!
   [:shared-appender-config :rolling :path] "logs/banner.log")

  (if (env :dev) (parser/cache-off!))
  ;;start the expired session cleanup job
  (migrate-db)
  (cronj/start! session-manager/cleanup-job)
  (timbre/info "\n-=[ banner started successfully"
               (when (env :dev) "using the development profile") "]=-"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "banner is shutting down...")
  (cronj/shutdown! session-manager/cleanup-job)
  (timbre/info "shutdown complete!"))

;; timeout sessions after 30 minutes
(def session-defaults
  {:timeout (* 60 30)
   :timeout-response (redirect "/")})

(defn- mk-defaults
       "set to true to enable XSS protection"
       [xss-protection?]
       (-> site-defaults
           (update-in [:session] merge session-defaults)
           (assoc-in [:security :anti-forgery] xss-protection?)))

(def app (app-handler
           ;; add your application routes here
           ;;[home-routes fin_move-routes test-routes base-routes]
           [home-routes client-routes test-routes base-routes]
           ;; add custom middleware here
           :middleware (load-middleware)
           :ring-defaults (mk-defaults false)
           ;; add access rules here
           :access-rules access/rules
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn :transit-json]))
