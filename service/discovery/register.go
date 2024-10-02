package discovery

import (
	"github.com/gofiber/fiber/v3"
	"github.com/ishanmadhav/rivulet/service/types"
	"github.com/ishanmadhav/rivulet/service/util"
)

func (s *DiscoveryServer) registerRoutes() {
	s.app.Post("/register", s.registerAgent)
	s.app.Post("deregister", s.deregisterAgent)
}

func (s *DiscoveryServer) registerAgent(c fiber.Ctx) error {
	var reqBody types.RegisterAgentReqBody
	c.Bind().Body(&reqBody)
	uuid, err := util.GenerateUUID(10)
	if err != nil {
		errorMessage := types.ErrorMesssage{
			Message: "Internal Server error",
		}
		return c.Status(400).JSON(errorMessage)
	}
	newAgent := types.Agent{
		NodeUrl:          reqBody.NodeUrl,
		NodePort:         reqBody.NodePort,
		ID:               uuid,
		ConnectedClients: make([]types.ServiceClient, 0),
	}
	err = s.store.AddAgent(newAgent)
	if err != nil {
		return c.Status(400).JSON(types.ErrorMesssage{
			Message: err.Error(),
		})
	}
	return c.JSON(newAgent)
}

func (s *DiscoveryServer) deregisterAgent(c fiber.Ctx) error {
	var reqBody types.DeregisterAgentReqBody
	err := c.Bind().Body(&reqBody)
	if err != nil {
		return c.Status(400).JSON(types.ErrorMesssage{
			Message: err.Error(),
		})
	}
	agent, err := s.store.FindAgentByID(reqBody.ID)
	if err != nil {
		return c.Status(404).JSON(types.ErrorMesssage{
			Message: err.Error(),
		})
	}
	s.store.RemoveAgent(agent)
	return c.JSON(agent)
}
