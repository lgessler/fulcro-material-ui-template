(ns fmuit.client.ui.project.project-detail
  (:require [com.fulcrologic.fulcro.components :as c :refer [defsc]]
            [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
            [com.fulcrologic.fulcro.data-fetch :as df]
            [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.ui-state-machines :as sm]
            [fmuit.client.router :as r]
            [fmuit.models.session :as session :refer [Session]]
            [taoensso.timbre :as log]
            [com.fulcrologic.fulcro.dom :as dom]))


(defsc ProjectDetail
  [this {:project/keys [id name] :as props}]
  {:query         [:project/id :project/name session/session-ident]
   :ident         :project/id
   :initial-state {}
   :route-segment (r/last-route-segment :project)
   :will-enter    (fn [app {:keys [id] :as route-params}]
                    (log/info "Entering: " (pr-str route-params))
                    (when (uuid id)
                      (dr/route-deferred
                        [:project/id (uuid id)]
                        #(df/load! app [:project/id (uuid id)] ProjectDetail
                                   {:post-mutation        `dr/target-ready
                                    :post-mutation-params {:target [:project/id (uuid id)]}}))))}
  (dom/div
    (dom/div "props" (pr-str props))
    (dom/div "id: " (pr-str id))
    (dom/div "name: " (pr-str name))
    (r/link :projects "Projects")))

(def ui-project-detail (c/factory ProjectDetail))

