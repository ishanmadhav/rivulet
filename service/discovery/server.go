package discovery

import (
	"github.com/gofiber/fiber/v3"
	"github.com/ishanmadhav/rivulet/service/loadbalancer"
	"github.com/ishanmadhav/rivulet/service/store"
)

type DiscoveryServer struct {
	app   *fiber.App
	store *store.Store
	lb    *loadbalancer.LoadBalancer
}

func NewDiscoveryServer() DiscoveryServer {
	app := fiber.New()
	return DiscoveryServer{
		app:   app,
		store: store.NewStore(),
		lb:    &loadbalancer.LoadBalancer{},
	}
}

func (s *DiscoveryServer) StartServer() {
	s.app.Get("/", func(c fiber.Ctx) error {
		return c.SendString("Hello")
	})
	s.registerRoutes()
	s.lookupRoutes()
	s.app.Listen(":3000")
}
