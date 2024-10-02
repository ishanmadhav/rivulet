package types

type Agent struct {
	NodeUrl          string          `json:"nodeurl"`
	NodePort         string          `json:"nodeport"`
	ID               string          `json:"id"`
	ConnectedClients []ServiceClient `json:"connectedClients"`
}

type RegisterAgentReqBody struct {
	NodeUrl  string `json:"nodeurl"`
	NodePort string `json:"nodeport"`
}

type DeregisterAgentReqBody struct {
	ID string `json:"id"`
}
