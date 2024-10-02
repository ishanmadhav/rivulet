package client

import (
	"fmt"

	"github.com/go-resty/resty/v2"
)

type Client struct {
	cli        *resty.Client
	clusterUrl string
}

func NewClient(clusterUrl string) *Client {
	httpCli := resty.New()

	newCli := Client{
		cli:        httpCli,
		clusterUrl: clusterUrl,
	}
	return &newCli
}

// ConsumeMessage calls the API to consume a message from a specific topic, partition, and offset
func (c *Client) ConsumeMessage(topic, partition string, offset int) (interface{}, error) {
	resp, err := c.cli.R().
		SetQueryParams(map[string]string{
			"topic":     topic,
			"partition": partition,
			"offset":    fmt.Sprintf("%d", offset),
		}).
		SetHeader("Content-Type", "application/json").
		SetResult(map[string]interface{}{}).
		Get(c.clusterUrl + "/api/consume")

	if err != nil {
		return nil, err
	}

	if resp.IsError() {
		return nil, fmt.Errorf("API request failed with status code: %d", resp.StatusCode())
	}

	return resp.Result(), nil
}

// ProduceMessage calls the API to produce a message to a specific topic and partition
func (c *Client) ProduceMessage(topic, partition, key, value string) (interface{}, error) {
	reqBody := map[string]interface{}{
		"topicPartition": map[string]string{
			"topic":     topic,
			"partition": partition,
		},
		"key":   key,
		"value": value,
	}

	resp, err := c.cli.R().
		SetHeader("Content-Type", "application/json").
		SetBody(reqBody).
		SetResult(map[string]interface{}{}).
		Post(c.clusterUrl + "/api/produce")

	if err != nil {
		return nil, err
	}

	if resp.IsError() {
		return nil, fmt.Errorf("API request failed with status code: %d", resp.StatusCode())
	}

	return resp.Result(), nil
}

func ExampleUsage() {
	client := NewClient("http://localhost:8080")

	// Consume message example
	consumeResult, err := client.ConsumeMessage("demotopicx", "demopartx", 2)
	if err != nil {
		fmt.Printf("Consume Error: %v\n", err)
	} else {
		fmt.Printf("Consume Response: %v\n", consumeResult)
	}

	// Produce message example
	produceResult, err := client.ProduceMessage("demotopicx", "demopartx", "key2", "val2")
	if err != nil {
		fmt.Printf("Produce Error: %v\n", err)
	} else {
		fmt.Printf("Produce Response: %v\n", produceResult)
	}
}
