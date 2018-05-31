(defproject example "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [devcards "0.2.4" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [re-frame "0.10.5"]
                 [reagent "0.8.1"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.16"]]
  :clean-targets ^{:protect false} ["resources/public/js" "target"]
  :figwheel {:http-server-root "public"
             :nrepl-port 7000
             :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl
                                cider.nrepl/cider-middleware
                                refactor-nrepl.middleware/wrap-refactor]}
  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.2"]
                                  [day8.re-frame/tracing "0.5.1"]
                                  [day8.re-frame/re-frame-10x "0.3.3-react16"]
                                  [binaryage/devtools "0.9.10"]]}
             :prod {:dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}}
  :cljsbuild {:builds [{:id "dev"
                        :figwheel {:devcards true}
                        :source-paths ["src/" "../../src/"]
                        :compiler {:main example.core
                                   :output-to "resources/public/js/example.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true
                                   :preloads [devtools.preload
                                              day8.re-frame-10x.preload]
                                   :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true
                                                     "day8.re_frame.tracing.trace_enabled_QMARK_"  true}
                                   :external-config {:devtools/config {:features-to-install :all}}}}
                       {:id "min"
                        :source-paths ["src/" "../../src/"]
                        :compiler {:main example.core
                                   :devcards true
                                   :output-to "resources/public/js/example.js"
                                   :optimizations :advanced
                                   :closure-defines {goog.DEBUG false}}}]})

