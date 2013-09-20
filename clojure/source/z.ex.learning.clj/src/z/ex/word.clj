
;;;; This is a tutorial/library for NLP in Clojure.

;;; Copyright (c) 2008, Eric Rochester
;;; All rights reserved.
;;;
;;; Redistribution and use in source and binary forms, with or without
;;; modification, are permitted provided that the following conditions are met:
;;;
;;;    * Redistributions of source code must retain the above copyright notice,
;;;      this list of conditions and the following disclaimer.
;;;    * Redistributions in binary form must reproduce the above copyright
;;;      notice, this list of conditions and the following disclaimer in the
;;;      documentation and/or other materials provided with the distribution.
;;;    * Neither the name of the word-clj nor the names of its contributors
;;;      may be used to endorse or promote products derived from this
;;;      software without specific prior written permission.
;;;
;;; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
;;; AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
;;; IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
;;; PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
;;; CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
;;; EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
;;; PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
;;; PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
;;; LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
;;; NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
;;; SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

(ns z.ex.word)
; This code is from the Eric Rochester blogs:
; http://writingcoding.blogspot.com/2008/06/tokenization-part-3-functions.html

(defn to-lower-case [token-string]
  (.toLowerCase token-string))

(def token-regex #"\w+")

(def stop-words
  #{"a" "in" "that" "for" "was" "is"
    "it" "the" "of" "and" "to" "he"})

(defn old-tokenize-str 
  ([input-string]
    (map to-lower-case (re-seq token-regex input-string)))
  ([input-string stop-words?]
    (filter (complement stop-words?)
            (map to-lower-case (re-seq token-regex input-string)))))

(defn tokenize-str 
  ([input-string]
    (map to-lower-case (re-seq token-regex input-string)))
  ([input-string stop-words?]
    (filter (complement stop-words?)
            (tokenize-str input-string))))

(defn tokenize
  ([filename]
   (tokenize-str (slurp filename))) ; slurp only works on small files
  ([filename stop-word?]
   (tokenize-str (slurp filename) stop-word?)))

(defn filter-str [input-string]
  (filter (complement stop-words) 
          (tokenize-str input-string)))

(defn lower-case-test []
  (map to-lower-case '("This" "IS" "my" "name")))

(defn token-test []
  (tokenize-str "This is a LIST OF TOKENS."))

(defn filter-str-test []
  (filter-str "This is a LIST OF TOKENS."))

(defn filter-test []
  (tokenize-str "This is a LIST OF TOKENS." stop-words))

(defn tokenize-file-test []
  (tokenize "README.md" stop-words))
