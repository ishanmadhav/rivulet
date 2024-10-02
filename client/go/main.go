package main

import (
	"fmt"
	"math/rand"
	"time"

	"github.com/ishanmadhav/rivulet-client/client"
)

const letterBytes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

// Function to generate a random string of given length
func randomString(n int) string {
	rand.Seed(time.Now().UnixNano()) // Seed the random number generator
	b := make([]byte, n)
	for i := range b {
		b[i] = letterBytes[rand.Intn(len(letterBytes))]
	}
	return string(b)
}

func main() {
	client := client.NewClient("http://localhost:8080")
	// for i := 0; i < 500; i++ {
	// 	// Produce message example
	// 	produceResult, err := client.ProduceMessage("gotopic4", "gopart4", randomString(5), randomString(10))
	// 	if err != nil {
	// 		fmt.Printf("Produce Error: %v\n", err)
	// 	} else {
	// 		fmt.Printf("Produce Response: %v\n", produceResult)
	// 	}
	// 	// Add a 2-second delay between each iteration
	// 	time.Sleep(100 * time.Millisecond)
	// }
	// Produce message example
	// produceResult, err := client.ProduceMessage("gotopic2", "gopart2", randomString(5), randomString(10))
	// if err != nil {
	// 	fmt.Printf("Produce Error: %v\n", err)
	// } else {
	// 	fmt.Printf("Produce Response: %v\n", produceResult)
	// }
	// // Add a 2-second delay between each iteration
	// time.Sleep(100 * time.Millisecond)
	// // Consume Messages
	// // Consume message example
	// consumeResult, err := client.ConsumeMessage("gotopic2", "gopart2", 8)
	// if err != nil {
	// 	fmt.Printf("Consume Error: %v\n", err)
	// } else {
	// 	fmt.Printf("Consume Response: %v\n", consumeResult)
	// }
	// for i := 150; i <= 500; i++ {
	// 	// Consume Messages
	// 	// Consume message example
	// 	consumeResult, err := client.ConsumeMessage("gotopic4", "gopart4", i)
	// 	if err != nil {
	// 		fmt.Printf("Consume Error: %v\n", err)
	// 	} else {
	// 		fmt.Printf("Consume Response: %v\n", consumeResult)
	// 	}
	// }
	// Consume Messages
	// Consume message example
	consumeResult, err := client.ConsumeMessage("gotopic4", "gopart4", 701)
	if err != nil {
		fmt.Printf("Consume Error: %v\n", err)
	} else {
		fmt.Printf("Consume Response: %v\n", consumeResult)
	}
}
