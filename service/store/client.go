package store

import "github.com/ishanmadhav/rivulet/service/types"

func (s *Store) AddClient(client types.ServiceClient) {
	s.clientList = append(s.clientList, client)
}

func (s *Store) RemoveClient(client types.ServiceClient) {
	newClientList := make([]types.ServiceClient, 0)
	for _, c := range s.clientList {
		if c.ID != client.ID {
			newClientList = append(newClientList, c)
		}
	}

	s.clientList = newClientList
}

func (s *Store) CountOfClients() int {
	return len(s.clientList)
}
