(ns testapp.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]))

;; -------------------------
;; Views


;; Input-Component
(defn input-component [arg toggleFlag] ;; Accepting Two Arguments
  [:input {:type "text"
           :value @arg ;; Binding Input Value
           :placeholder "Enter number"
           :on-change #(reset! arg (-> % .-target .-value)) ;; Binding Input Value On Change
           :on-focus #(reset! toggleFlag false) ;; Condition To Toggle View Of Sum-Component
           }])


;; Sum-Component
(defn sum-component [sum sumStatement]
  (fn []
    (if (js/Number.isNaN @sum) ;; Checking Condition For Valid Sum
      [:div.res ;; Valid Sum Cond true
       {:style {:color "red"}}
       "Input is invalid! Please enter number."]
      [:div.res ;; Valid Sum Cond false
       {:style
        {:color (if (> @sum 100) "green" "blue")}} ;; Switching Statement Color If Sum > 100
       (str @sumStatement " = ") [:b (str @sum)] ;; Converting Integer Value to String
       ])))


;; Form Component
(defn form-component []
  (let [inputOne (r/atom nil) ;; Declaring Atom For Input-One Value
        inputTwo (r/atom nil) ;; Declaring Atom For Input-Two Value
        sum (r/atom nil) ;; Declaring Atom For Sum Of Two Inputs
        sumStatement (r/atom "") ;; Declaring Atom For Sum Of Two Inputs Statements
        toggleFlag (r/atom false)] ;; Declaring Atom For Sum Of Two Inputs Statements
    (fn []
      [:form
       {:on-submit (fn [e]   ;; Form Submit Handler
                     (.preventDefault e)
                     (reset! sum (+ (js/parseInt @inputOne) (js/parseInt @inputTwo))) ;; Storing Inputs Sum
                     (reset! sumStatement (str "Sum of number " (js/parseInt @inputOne) " + " (js/parseInt @inputTwo))) ;; Storing Inputs Sum Statement
                     (reset! toggleFlag true) ;; Setting Toggle View Of Sum-Component To true
                     (reset! inputOne nil) ;; Reset Value Of InputOne
                     (reset! inputTwo nil))} ;; Reset Value Of InputTwo
       [:div.inp
        [input-component inputOne toggleFlag] ;; Calling Input-Component & Passing Input Parameters
        [:div.plus "+"]
        [input-component inputTwo toggleFlag] ;; Calling Input-Component & Passing Input Parameters
        [:div.clear]]
       [:div.btn [:button {:type "submit"} "Submit"]] ;; On-Submit Button
       (if @toggleFlag ;; Checking Condition For Show/Hide Sum-Component
         [sum-component sum sumStatement] ;; Condition True Calling Sum-Component & Passing Input Parameters
         nil) ;; Condition False Hiding Sum-Component
       ])))


;; Home-page Component
(defn home-page []
  [:div
   [:h2 "ClojureScript With Reagent framework"]
   [:div.frm
    [:div.ttl "Add Two Numbers"]
    [form-component] ;; Calling form-component
    ]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))