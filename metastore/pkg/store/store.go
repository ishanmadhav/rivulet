package store

import "github.com/cockroachdb/pebble"

type Store struct {
	kv *pebble.DB
}

func NewStore() (Store, error) {
	db, err := pebble.Open("demo", &pebble.Options{})
	if err != nil {
		return Store{}, err
	}
	newStore := Store{
		kv: db,
	}
	return newStore, nil
}

func (s *Store) Add(metaDataKey string, metadata string) error {
	key := []byte(metaDataKey)
	value := []byte(metadata)
	err := s.kv.Set(key, value, pebble.Sync)
	if err != nil {
		return err
	}
	return nil
}

func (s *Store) Get(metaDataKey string) (string, error) {
	key := []byte(*&metaDataKey)
	value, closer, err := s.kv.Get(key)
	if err != nil {
		return "", err
	}

	err = closer.Close()
	if err != nil {
		return "", err
	}

	return string(value), nil
}

func (s *Store) Update(metadataKey string, metadata string) error {
	return s.Add(metadataKey, metadata)

}

func (s *Store) Delete(metadataKey string) error {
	key := []byte(metadataKey)
	err := s.kv.Delete(key, &pebble.WriteOptions{})
	if err != nil {
		return err
	}
	return nil
}

func (s *Store) List() error {
	return nil
}
