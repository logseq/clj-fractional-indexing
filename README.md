# clj-fractional-indexing

This is a Clojure port of the original JavaScript implementation https://github.com/rocicorp/fractional-indexing.

## Usage

``` clojure
(require '[logseq.clj-fractional-indexing :as index])

;; Generate one key

(index/generate-key-between nil nil)
;; "a0"

(generate-key-between "a0" nil)
;; "a1"

(generate-key-between "a0" "a1")
;; "a0V"

;; Generate multiple keys
;; ["a0G" "a0V" "a0l"]
```

## License
Distributed under the Eclipse Public License version 1.0.
