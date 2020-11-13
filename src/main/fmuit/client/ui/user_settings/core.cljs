(ns fmuit.client.ui.user-settings.core
  (:require [com.fulcrologic.fulcro.components :refer [defsc]]
            [com.fulcrologic.fulcro.components :as c]
            [fmuit.models.session :as sn]
            [fmuit.client.router :as r]
            [fmuit.client.ui.user-settings.change-password :refer [ChangePasswordForm ui-change-password-form]]
            [fmuit.client.ui.user-settings.change-name :refer [ChangeNameForm ui-change-name-form]]
            [fmuit.client.ui.material-ui :as mui]))

(defsc UserSettings [this {:keys [change-name-form
                                  change-password-form] :as props}]
  {:ident         (fn [_] [:component/id :user-settings])
   :query         [sn/session-join
                   {:change-name-form (c/get-query ChangeNameForm)}
                   {:change-password-form (c/get-query ChangePasswordForm)}]
   :initial-state {:change-name-form     {}
                   :change-password-form {}}
   :route-segment (r/last-route-segment :user-settings)}
  (when (sn/valid-session? props)
    (mui/page-container
      (c/fragment
        (ui-change-name-form change-name-form)
        (ui-change-password-form change-password-form)))))
