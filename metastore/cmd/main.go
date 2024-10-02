package main

import (
	"fmt"
	"log"

	"github.com/ishanmadhav/rivulet/metastore/pkg/store"
)

func main() {
	s, err := store.NewStore()
	if err != nil {
		log.Fatal(err)
	}

	s.Add("apple", "basket")

	str, err := s.Get("apple")
	fmt.Println(str)
}
