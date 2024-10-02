package store

import (
	"context"
	"fmt"
	"log"
	"time"

	"github.com/ishanmadhav/rivulet/service/types"
	"github.com/redis/go-redis/v9"
)

var ctx = context.Background()

// DragonflyDB backend metadata store specific to service discovery
type Store struct {
	dragondb   *redis.Client
	agentList  []types.Agent
	clientList []types.ServiceClient
}

// NewStore initializes and returns a new Store instance
func NewStore() *Store {
	dragondb := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "", // no password set
		DB:       0,  // use default DB
	})
	return &Store{
		dragondb:   dragondb,
		agentList:  make([]types.Agent, 0),
		clientList: make([]types.ServiceClient, 0),
	}
}

// SetKeyValue sets a key-value pair in Redis
func (s *Store) SetKeyValue(key, value string) error {
	err := s.dragondb.Set(ctx, key, value, 0).Err()
	if err != nil {
		log.Printf("Error setting key: %v", err)
		return err
	}
	fmt.Printf("Set key: %s, value: %s\n", key, value)
	return nil
}

// GetKeyValue retrieves a value by key from Redis
func (s *Store) GetKeyValue(key string) (string, error) {
	value, err := s.dragondb.Get(ctx, key).Result()
	if err == redis.Nil {
		fmt.Printf("Key %s does not exist\n", key)
		return "", nil
	} else if err != nil {
		log.Printf("Error getting key: %v", err)
		return "", err
	}
	fmt.Printf("Got key: %s, value: %s\n", key, value)
	return value, nil
}

// DeleteKey deletes a key from Redis
func (s *Store) DeleteKey(key string) error {
	err := s.dragondb.Del(ctx, key).Err()
	if err != nil {
		log.Printf("Error deleting key: %v", err)
		return err
	}
	fmt.Printf("Deleted key: %s\n", key)
	return nil
}

// IncrementKey increments a key by a given value
func (s *Store) IncrementKey(key string, increment int64) (int64, error) {
	newValue, err := s.dragondb.IncrBy(ctx, key, increment).Result()
	if err != nil {
		log.Printf("Error incrementing key: %v", err)
		return 0, err
	}
	fmt.Printf("Incremented key: %s, new value: %d\n", key, newValue)
	return newValue, nil
}

// List operations: Push and Pop

// PushToList pushes a value to a Redis list
func (s *Store) PushToList(listName, value string) error {
	err := s.dragondb.LPush(ctx, listName, value).Err()
	if err != nil {
		log.Printf("Error pushing to list: %v", err)
		return err
	}
	fmt.Printf("Pushed to list: %s, value: %s\n", listName, value)
	return nil
}

// PopFromList pops a value from a Redis list
func (s *Store) PopFromList(listName string) (string, error) {
	value, err := s.dragondb.LPop(ctx, listName).Result()
	if err == redis.Nil {
		fmt.Printf("List %s is empty\n", listName)
		return "", nil
	} else if err != nil {
		log.Printf("Error popping from list: %v", err)
		return "", err
	}
	fmt.Printf("Popped from list: %s, value: %s\n", listName, value)
	return value, nil
}

// Set operations: Add and Remove

// AddToSet adds a value to a Redis set
func (s *Store) AddToSet(setName, value string) error {
	err := s.dragondb.SAdd(ctx, setName, value).Err()
	if err != nil {
		log.Printf("Error adding to set: %v", err)
		return err
	}
	fmt.Printf("Added to set: %s, value: %s\n", setName, value)
	return nil
}

// RemoveFromSet removes a value from a Redis set
func (s *Store) RemoveFromSet(setName, value string) error {
	err := s.dragondb.SRem(ctx, setName, value).Err()
	if err != nil {
		log.Printf("Error removing from set: %v", err)
		return err
	}
	fmt.Printf("Removed from set: %s, value: %s\n", setName, value)
	return nil
}

// Hash operations: Set and Get fields in a hash

// SetHashField sets a field in a Redis hash
func (s *Store) SetHashField(hashName, field, value string) error {
	err := s.dragondb.HSet(ctx, hashName, field, value).Err()
	if err != nil {
		log.Printf("Error setting hash field: %v", err)
		return err
	}
	fmt.Printf("Set hash: %s, field: %s, value: %s\n", hashName, field, value)
	return nil
}

// GetHashField retrieves a field from a Redis hash
func (s *Store) GetHashField(hashName, field string) (string, error) {
	value, err := s.dragondb.HGet(ctx, hashName, field).Result()
	if err == redis.Nil {
		fmt.Printf("Field %s does not exist in hash %s\n", field, hashName)
		return "", nil
	} else if err != nil {
		log.Printf("Error getting hash field: %v", err)
		return "", err
	}
	fmt.Printf("Got hash: %s, field: %s, value: %s\n", hashName, field, value)
	return value, nil
}

// Expiring key operations

// SetKeyWithExpiry sets a key-value pair with an expiration time
func (s *Store) SetKeyWithExpiry(key, value string, expiration time.Duration) error {
	err := s.dragondb.Set(ctx, key, value, expiration).Err()
	if err != nil {
		log.Printf("Error setting key with expiration: %v", err)
		return err
	}
	fmt.Printf("Set key: %s, value: %s, expires in: %v\n", key, value, expiration)
	return nil
}

// GetKeysWithPattern returns keys matching a pattern
func (s *Store) GetKeysWithPattern(pattern string) ([]string, error) {
	keys, err := s.dragondb.Keys(ctx, pattern).Result()
	if err != nil {
		log.Printf("Error getting keys with pattern: %v", err)
		return nil, err
	}
	fmt.Printf("Keys matching pattern %s: %v\n", pattern, keys)
	return keys, nil
}

// Check if a key exists
func (s *Store) KeyExists(key string) (bool, error) {
	exists, err := s.dragondb.Exists(ctx, key).Result()
	if err != nil {
		log.Printf("Error checking key existence: %v", err)
		return false, err
	}
	return exists > 0, nil
}
