package main 

import (
	"fmt"
	"log"
	"net/http"
	"io"
)


type Hello struct{}

func (h Hello) ServeHTTP(
	w http.ResponseWriter,
	r *http.Request) {
	fmt.Fprint(w, "Hello!")
}

func hello_main() {
	var h Hello
	err := http.ListenAndServe("localhost:4000", h)
	if err != nil {
		log.Fatal(err)
	}
}

// hello world, the web server
func HelloServerFunc(w http.ResponseWriter, req *http.Request) {
	io.WriteString(w, "hello, world!\n")
}

type String string
func (s String) ServeHTTP(
	w http.ResponseWriter,
	r *http.Request) {
	fmt.Fprint(w, s);
}

func main() {
	http.HandleFunc("/hello", HelloServerFunc)
	http.Handle("/about", String("Version 0.3"))
	err := http.ListenAndServe(":4000", nil)
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}
