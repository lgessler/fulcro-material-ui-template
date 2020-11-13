(ns fmuit.client.ui.admin-settings.core
  (:require [com.fulcrologic.fulcro.components :as c :refer [defsc]]
            [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
            [com.fulcrologic.fulcro.routing.dynamic-routing :as dr :refer [defrouter]]
            [fmuit.models.session :as sn]
            [fmuit.client.router :as r]
            [fmuit.client.ui.common.core :refer [loader]]
            [fmuit.client.ui.material-ui :as mui]
            [fmuit.client.ui.admin-settings.user-management :refer [UserManagement]]
            [fmuit.client.ui.admin-settings.project-management :refer [ProjectManagement]]
            [fmuit.client.ui.admin-settings.project-settings :refer [ProjectSettings]]
            [com.fulcrologic.fulcro.data-fetch :as df]
            [com.fulcrologic.fulcro.dom :as dom]
            [fmuit.client.ui.material-ui-icon :as muic]))

(def ident [:component/id :admin-home])

(def md-card-media (mui/styled-card-media {:height "120px"}))
(defn link-card [route text icon]
  (mui/card {}
    (mui/card-action-area {:onClick #(r/route-to! route)
                           :style   {:textAlign "center"}}
      (md-card-media {}
        (icon {:color "secondary"
               :style {:fontSize  120
                       :marginTop "10px"}}))
      (mui/card-content {}
        (mui/typography {:variant "h5"} text)))))

(defsc AdminHome [this props]
  {:ident         (fn [_] ident)
   :query         [sn/session-join]
   :initial-state (fn [_] sn/session-ident (c/get-initial-state sn/Session {}))
   :route-segment (r/last-route-segment :admin-home)}
  (when (and (sn/valid-session? props) (sn/admin? props))
    (mui/container {:maxWidth "lg"}
      (mui/page-title {:style {:marginBottom "0.7em"}} "Admin Settings")

      (mui/grid {:container true :spacing 3}
        (mui/grid {:item true :xs 3}
          (link-card :user-management "Manage Users" muic/supervised-user-circle-sharp))
        (mui/grid {:item true :xs 3}
          (link-card :project-management "Manage Projects" muic/work-outline-two-tone))))))

(defrouter ProjectAdminRouter
  [this {:keys [current-state route-factory route-props pending-path-segment]}]
  {:route-segment       (r/router-segment :project-admin-router)
   :router-targets      [ProjectManagement ProjectSettings]
   :always-render-body? false}
  (loader))

(defrouter AdminRouter
  [this {:keys [current-state route-factory route-props pending-path-segment]}]
  {:route-segment       (r/router-segment :admin-router)
   :router-targets      [AdminHome UserManagement ProjectAdminRouter]
   :always-render-body? false}
  (loader))
