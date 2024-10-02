package store

type MetadataStore interface {
	Add(metadataKey string, metadata string) error
	Get(metadataKey string) (string, error)
	Update(metadataKey string, metadata string) error
	Delete(metadataKey string) error
	List() error
}
