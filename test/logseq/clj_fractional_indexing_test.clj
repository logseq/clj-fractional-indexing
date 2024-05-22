(ns logseq.clj-fractional-indexing-test
  (:require [clojure.test :refer [is deftest are]]
            [logseq.clj-fractional-indexing :as index]
            [clojure.string :as string]))

(deftest increment-integer-test
  (are [x y]
       (= (index/increment-integer x index/base-62-digits) y)
    "a0" "a1"
    "r3333333333333333zz" "r333333333333333400"))

(deftest generate-key-between-test
  (are [x y]
       (= (index/generate-key-between x nil) y)
    "a0" "a1"
    "rzzzzzzzzzzzzzzzzzz" "s0000000000000000000"))

(deftest generate-n-keys-between-test
  (are [x y]
       (= (index/generate-n-keys-between (first x) (second x) 20) y)
    ["ZxV" "Zy7"]
    ["ZxX" "ZxZ" "Zxd" "Zxf" "Zxh" "Zxl" "Zxn" "Zxp" "Zxt" "Zxx" "Zy" "Zy0V" "Zy1" "Zy2" "Zy3" "Zy4" "Zy4V" "Zy5" "Zy6" "Zy6V"]

    ["Zy7" "axV"]
    ["ZyB" "ZyE" "ZyL" "ZyP" "ZyS" "ZyZ" "Zyd" "Zyg" "Zyn" "Zyu" "Zz" "Zz8" "ZzG" "ZzV" "Zzl" "a0" "a0G" "a0V" "a1" "a2"]

    [nil "c0a3"]
    ["aG"
     "aH"
     "aI"
     "aJ"
     "aK"
     "aL"
     "aM"
     "aN"
     "aO"
     "aP"
     "aQ"
     "aR"
     "aS"
     "aT"
     "aU"
     "aV"
     "aW"
     "b0X"
     "bY1"
     "b2Z"]

    ["c0a3" nil]
    ["c0a4"
     "c0a5"
     "c0a6"
     "c0a7"
     "c0a8"
     "c0a9"
     "c0aA"
     "c0aB"
     "c0aC"
     "c0aD"
     "c0aE"
     "c0aF"
     "c0aG"
     "c0aH"
     "c0aI"
     "c0aJ"
     "c0aK"
     "c0aL"
     "c0aM"
     "c0aN"]

    [nil nil]
    ["a0"
     "a1"
     "a2"
     "a3"
     "a4"
     "a5"
     "a6"
     "a7"
     "a8"
     "a9"
     "aA"
     "aB"
     "aC"
     "aD"
     "aE"
     "aF"
     "aG"
     "aH"
     "aI"
     "aJ"]))

(defn test-generate-key-between [a b exp]
  (let [act (try
              (index/generate-key-between a b)
              (catch Exception e (.getMessage e)))]
    (is (= exp act) (str exp " == " act))))

(deftest test-generate-key-between-cases
  (test-generate-key-between nil nil "a0")
  (test-generate-key-between nil "a0" "Zz")
  (test-generate-key-between nil "Zz" "Zy")
  (test-generate-key-between "a0" nil "a1")
  (test-generate-key-between "a1" nil "a2")
  (test-generate-key-between "a0" "a1" "a0V")
  (test-generate-key-between "a1" "a2" "a1V")
  (test-generate-key-between "a0V" "a1" "a0l")
  (test-generate-key-between "Zz" "a0" "ZzV")
  (test-generate-key-between "Zz" "a1" "a0")
  (test-generate-key-between nil "Y00" "Xzzz")
  (test-generate-key-between "bzz" nil "c000")
  (test-generate-key-between "a0" "a0V" "a0G")
  (test-generate-key-between "a0" "a0G" "a08")
  (test-generate-key-between "b125" "b129" "b127")
  (test-generate-key-between "a0" "a1V" "a1")
  (test-generate-key-between "Zz" "a01" "a0")
  (test-generate-key-between nil "a0V" "a0")
  (test-generate-key-between nil "b999" "b99")
  (test-generate-key-between nil "A00000000000000000000000000" "invalid order key: A00000000000000000000000000")
  (test-generate-key-between nil "A000000000000000000000000001" "A000000000000000000000000000V")
  (test-generate-key-between "zzzzzzzzzzzzzzzzzzzzzzzzzzy" nil "zzzzzzzzzzzzzzzzzzzzzzzzzzz")
  (test-generate-key-between "zzzzzzzzzzzzzzzzzzzzzzzzzzz" nil "zzzzzzzzzzzzzzzzzzzzzzzzzzzV")
  (test-generate-key-between "a00" nil "invalid order key: a00")
  (test-generate-key-between "a00" "a1" "invalid order key: a00")
  (test-generate-key-between "0" "1" "invalid order key head: 0")
  (test-generate-key-between "a1" "a0" "a1 >= a0"))

(defn test-generate-n-keys-between [a b n exp]
  (let [base-10-digits "0123456789"
        act (try
              (string/join " " (index/generate-n-keys-between a b n {:digits base-10-digits}))
              (catch Exception e (.getMessage e)))]
    (is (= exp act) (str exp " == " act))))

(deftest test-generate-n-keys-between-cases
  (test-generate-n-keys-between nil nil 5 "a0 a1 a2 a3 a4")
  (test-generate-n-keys-between "a4" nil 10 "a5 a6 a7 a8 a9 b00 b01 b02 b03 b04")
  (test-generate-n-keys-between nil "a0" 5 "Z5 Z6 Z7 Z8 Z9")
  (test-generate-n-keys-between "a0" "a2" 20 "a01 a02 a03 a035 a04 a05 a06 a07 a08 a09 a1 a11 a12 a13 a14 a15 a16 a17 a18 a19"))

(defn test-generate-key-between-base95 [a b exp]
  (let [base-95-digits " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
        act (try
              (index/generate-key-between a b {:digits base-95-digits})
              (catch Exception e (.getMessage e)))]
    (is (= exp act) (str exp " == " act))))

(deftest test-generate-key-between-base95-cases
  (test-generate-key-between-base95 "a00" "a01" "a00P")
  (test-generate-key-between-base95 "a0/" "a00" "a0/P")
  (test-generate-key-between-base95 nil nil "a ")
  (test-generate-key-between-base95 "a " nil "a!")
  (test-generate-key-between-base95 nil "a " "Z~")
  (test-generate-key-between-base95 "a0 " "a0!" "invalid order key: a0 ")
  (test-generate-key-between-base95 nil "A                          0" "A                          (")
  (test-generate-key-between-base95 "a~" nil "b  ")
  (test-generate-key-between-base95 "Z~" nil "a ")
  (test-generate-key-between-base95 "b   " nil "invalid order key: b   ")
  (test-generate-key-between-base95 "a0" "a0V" "a0;")
  (test-generate-key-between-base95 "a  1" "a  2" "a  1P")
  (test-generate-key-between-base95 nil "A                          " "invalid order key: A                          "))

(comment
  (clojure.test/run-tests 'logseq.clj-fractional-indexing-test))
