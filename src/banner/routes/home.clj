(ns banner.routes.home
  (:require [compojure.core :refer :all]
            [banner.cookies :as cookies]
            [banner.layout :as layout]
            [banner.models.user-model :as u]
            [banner.util :as util]
            [banner.validators.user-validator :as v]
            [ring.util.response :as response]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defn foo-response [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body  (str "<html><body><dt>Almocar?</dt>"
              "<dd>" (:Almocar? request) "</dd></body></html>")})

(defn signup-page []
  (layout/render "signup.html"))

(defn signup-page-submit [user]
  (let [errors (v/validate-signup user)]
    (if (empty? errors)
      (do
        (u/add-user! user)
        (response/redirect "/signup-success"))
      (layout/render "signup.html" (assoc user :errors errors)))))

;(defn login-page
;  "Renders the login form."
;  ([]
;  (layout/render "login.html" {:username (cookies/remember-me)}))
;  ([credentials]
;    (if (apply u/auth-user (map credentials [:username :password]))
;      (do (if (:remember-me credentials)
;            (cookies/remember-me (:username credentials))
;            (cookies/remember-me ""))
;        (response/redirect "/fin_moves/recently-added"))
;      (layout/render "login.html" {:invalid-credentials? true}))))

(defn login-page
  "Renders the login form."
  ([]
  (layout/render "login.html" {:username (cookies/remember-me)}))
  ([credentials]
    (println credentials)
    (if (apply u/auth-user (map credentials [:username :password]))
      (do (if (:remember-me credentials)
            (cookies/remember-me (:username credentials))
            (cookies/remember-me ""))
        (response/redirect (str "/client/recently-added/" (:username credentials))))
      (layout/render "login.html" {:invalid-credentials? true}))))

(defn logout
  "Log out usuario da sessao."
  []
  (u/invalidate-auth)
  (response/redirect "/"))

(defroutes home-routes
  (GET "/" [] (home-page))
  ;;
  (GET "/home" [] (home-page))
  ;;(GET "/about" request (foo-response request))
  (GET "/about" [] (about-page))
  (ANY "/req" request (str request))
  (GET "/login" [] (login-page))
  (POST "/login" [& login-form] (login-page login-form))
  (ANY "/logout" [] (logout))
  (GET "/signup" [] (signup-page))
  ;(GET "/signup" [] "SIGNUP PAGE...")
  (POST "/signup" [& form] (signup-page-submit form))
  (GET "/signup-success" [] "Success!"))
