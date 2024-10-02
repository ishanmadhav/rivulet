package types

type ServiceClient struct {
	Type string //CONSUMER or PRODUCER `json:"type"`
	ID   string `json:"id"`
}
