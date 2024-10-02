package loadbalancer

import "sync"

// Load balancing logic. Primitive right now
type LoadBalancer struct {
	CurrentAgentIndex int
	Mu                sync.Mutex
}
