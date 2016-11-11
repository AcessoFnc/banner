(ns banner.util
  (:require [noir.io :as io]
            [markdown.core :as md]))

(defn md->html
  "le um arq. markdown em public/md e retorna um string HTML"
  [filename]
  (md/md-to-html-string (io/slurp-resource filename)))
