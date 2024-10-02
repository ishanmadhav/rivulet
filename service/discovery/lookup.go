package discovery

import (
	"github.com/gofiber/fiber/v3"
	"github.com/ishanmadhav/rivulet/service/types"
)

func (s *DiscoveryServer) lookupRoutes() {
	s.app.Get("/lookup", s.lookupAgent)
}

func (s *DiscoveryServer) lookupAgent(c fiber.Ctx) error {
	// Ensure thread-safe access to agent list
	s.lb.Mu.Lock()
	defer s.lb.Mu.Unlock()

	// If no agents are available, return an error
	if len(s.store.GetAgentList()) == 0 {
		return c.Status(fiber.StatusNotFound).JSON(types.ErrorMesssage{
			Message: "No agents are availabel at the moment",
		})
	}

	// Implement round-robin selection of agents
	agentList := s.store.GetAgentList()
	agent := agentList[s.lb.CurrentAgentIndex]

	// Update the index to the next agent, wrapping around if necessary
	s.lb.CurrentAgentIndex = (s.lb.CurrentAgentIndex + 1) % len(agentList)

	// Return the selected agent
	return c.JSON(agent)
}
