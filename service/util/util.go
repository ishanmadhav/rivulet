package util

import (
	"crypto/rand"
	"encoding/hex"
	"fmt"
)

// GenerateUUID generates a random string of the specified length
func GenerateUUID(length int) (string, error) {
	// Create a byte slice of half the length, since hex encoding will double the length
	bytes := make([]byte, length/2)

	// Read random bytes into the slice
	_, err := rand.Read(bytes)
	if err != nil {
		return "", fmt.Errorf("error generating random bytes: %v", err)
	}

	// Convert the random bytes to a hexadecimal string
	uuid := hex.EncodeToString(bytes)

	return uuid, nil
}
