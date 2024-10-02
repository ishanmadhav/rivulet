package store

import (
	"errors"

	"github.com/ishanmadhav/rivulet/service/types"
)

func (s *Store) AddAgent(agent types.Agent) error {
	avail := s.isNodePortUrlAvailable(agent)
	if !avail {
		return errors.New("node Port and URL already in use")
	}
	s.agentList = append(s.agentList, agent)
	return nil
}

func (s *Store) RemoveAgent(agent types.Agent) {
	// Create a new list without the agent to be removed
	newAgentList := make([]types.Agent, 0)

	// Iterate over the existing agent list and copy over all agents except the one to be removed
	for _, a := range s.agentList {
		// Assuming types.Agent has an ID field or some unique identifier to compare
		if a.ID != agent.ID {
			newAgentList = append(newAgentList, a)
		}
	}

	// Replace the old agentList with the new one that excludes the removed agent
	s.agentList = newAgentList
}

func (s *Store) CountOfAgents() int {
	return len(s.agentList)
}

func (s *Store) isNodePortUrlAvailable(agent types.Agent) bool {
	return true
}

func (s *Store) FindAgentByID(id string) (types.Agent, error) {
	for i := 0; i < len(s.agentList); i++ {
		if s.agentList[i].ID == id {
			return s.agentList[i], nil
		}
	}

	return types.Agent{}, errors.New("Agent ID does not exist")
}

func (s *Store) GetAgentList() []types.Agent {
	return s.agentList
}
