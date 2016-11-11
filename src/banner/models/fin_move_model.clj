(ns banner.models.fin_move-model
  (:require [clojure.java.jdbc :as jdbc]
            [yesql.core :refer [defqueries]]
            [banner.models.connection :refer [db-spec]]))

(defqueries "banner/models/fin_moves.sql" {:connection db-spec})
(defqueries "banner/models/clients.sql" {:connection db-spec})

(defn add-fin_move!
  "Adds a new fin_move to the database."
  ([fin_move]
   (jdbc/with-db-transaction [tx db-spec]
     (add-fin_move! fin_move tx)))
  ([fin_move tx]
     (let [client-info {:client_name (:client_name fin_move)}
           txn {:connection tx}
           ;fetch or insert the client record
           client (or (first (get-clients-by-name client-info txn))
                      (insert-client<! client-info txn))
           fin_move-info (assoc fin_move :client_id (:client_id client))]
       (or (first (get-fin_moves-by-name fin_move-info txn))
           (insert-fin_move<! fin_move-info txn)))))

(defn ins-fin-move!
             "Adds a new fin_move to the database."
             ([fin_move]
              (jdbc/with-db-transaction [tx db-spec]
                (ins-fin-move! fin_move tx)))
             ([fin_move tx]
                (let [;client-info {:client_name (:client_name fin_move)}
                      txn {:connection tx}
                      ;insert the fin move record
                      ;client (or (first (get-clients-by-name client-info txn))
                                 ;(insert-client<! client-info txn))
                      ;fin_move-info (assoc fin_move :client_id (:client_id client))
                     ]
                  ;(or (first (get-fin_moves-by-name fin_move-info txn))
                      ;(insert-fin_move<! fin_move-info txn)
                      (ins-fin-move-by-username<! fin_move txn)
                  ;)    ;;;; or...
                )))

(defn ret-cli
  "Returns an client from the database."
  ([client]
  (jdbc/with-db-transaction [tx db-spec]
    (ret-cli client tx)))
  ([client tx]
    (let [client-info {:username (:username client)}
          txn {:connection tx}
          ;fetch the client record
          cli_info (first (get-client-by-username client-info txn)
                   )
         ]
     ;(println client-info)
     (println "CLI_INFO" cli_info)
    )
  )
)

          ;ctx-client-info (assoc client :client_id (:client_id cli_info))]
