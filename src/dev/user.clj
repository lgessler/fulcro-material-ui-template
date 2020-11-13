(ns user
  (:require
    [clojure.spec.alpha :as s]
    [clojure.tools.namespace.repl :as tools-ns]
    [expound.alpha :as expound]
    [mount.core :as mount]
    [shadow.cljs.devtools.api :as shadow]
    ;; this is the top-level dependent component...mount will find the rest via ns requires
    [fmuit.server.server :refer [http-server]]
    [fmuit.crux.easy :as gce]
    [fmuit.crux.user :as usr]
    [fmuit.crux.project :as prj]))

;; ==================== SERVER ====================
(tools-ns/set-refresh-dirs "src/main" "src/dev" "src/test")

;; Change the default output of spec to be more readable
(alter-var-root #'s/*explain-out* (constantly expound/printer))

(comment
  ;; For shadow: start the shadow-cljs server using ./scripts/start-dev.sh
  ;; start a cljs repl once shadow is listening (port 9000, default)
  ;; then you can start the watch processes here (ensure the cljs repl has focus in cursive):
  ;;
  (shadow/repl :main)
  (shadow/help)

  ;; You have to start the node server first.
  ;; in terminal, run: node target/node-server.js
  (shadow/repl :node-server)
  ;; now you can send cljs forms to the repl but you'll need to open another cljs repl
  ;; as the server has captured output.
  )

(defn start "Start the web server + services" [] (mount/start)
  (def node fmuit.server.crux/crux-node)
  (def user0 (:user/id (usr/get-by-email node "a@b.com")))
  (def user1 (:user/id (usr/get-by-email node "b@b.com")))
  (def user2 (:user/id (usr/get-by-email node "c@c.com")))
  (def prj1 (:project/id (prj/get-by-name node "Project 1")))
  (def prj2 (:project/id (prj/get-by-name node "Project 2")))
  )
(defn stop "Stop the web server + services" [] (mount/stop))
(defn restart
  "Stop, reload code, and restart the server. If there is a compile error, use:
  ```
  (tools-ns/refresh)
  ```
  to recompile, and then use `start` once things are good."
  []
  (stop)
  (tools-ns/refresh :after 'user/start))

(comment
  (tools-ns/refresh :after 'user/start)
  (shadow/repl :main)
  (stop)
  (restart))



;; crux stuff
(defn init-db []
  (let [node fmuit.server.crux/crux-node]
    (let [admin-id (usr/create
                     node
                     {:user/password-hash "100$12$argon2id$v13$u6JYj16Ize35J1uuTN6KwQ$SblXBBHdyMZ5K52RwCcO41/SNL6XqoY1JBouP/V01uQ$$$"
                      :user/name          "admin"
                      :user/email         "a@b.com"})
          user1 (usr/create
                  node
                  {:user/password-hash "100$12$argon2id$v13$u6JYj16Ize35J1uuTN6KwQ$SblXBBHdyMZ5K52RwCcO41/SNL6XqoY1JBouP/V01uQ$$$"
                   :user/name          "user"
                   :user/email         "b@b.com"})
          user2 (usr/create
                  node
                  {:user/password-hash "100$12$argon2id$v13$u6JYj16Ize35J1uuTN6KwQ$SblXBBHdyMZ5K52RwCcO41/SNL6XqoY1JBouP/V01uQ$$$"
                   :user/name          "user2"
                   :user/email         "c@c.com"})

          project1 (prj/create node {:project/name "Project 1"})
          project2 (prj/create node {:project/name "Project 2"})
          project3 (prj/create node {:project/name "Project 3"})
          project4 (prj/create node {:project/name "Project 4"})]

      (prj/add-writer node project1 admin-id)
      (prj/add-writer node project2 admin-id)

      (prj/add-writer node project1 user1)
      (prj/add-reader node project2 user1)
      (prj/add-reader node project1 user1)

      (prj/add-writer node project2 user2)
      (prj/add-writer node project3 user2)
      )))

