package client

import (
	"errors"
	"fmt"

	"github.com/go-resty/resty/v2"
)

type Client struct {
	cli        *resty.Client
	clusterUrl string
	agentUrl   string
	agentId    string
}

type Agent struct {
	NodeUrl  string `json:"nodeurl"`
	NodePort string `json:"nodeport"`
	Id       string `json:"id"`
}

func NewClient(clusterUrl string) (*Client, error) {
	httpCli := resty.New()
	respAgent := Agent{}
	_, err := httpCli.R().SetHeader("Content-Type", "application/json").SetResult(&respAgent).Get(clusterUrl + "/lookup")
	if err != nil {
		return nil, err
	}
	// Check if the respAgent contains valid data by checking key fields
	if respAgent.Id == "" && respAgent.NodeUrl == "" {
		return nil, errors.New("Null agent found")
	}
	newCli := Client{
		cli:        httpCli,
		clusterUrl: clusterUrl,
		agentUrl:   "http://" + respAgent.NodeUrl + ":" + respAgent.NodePort,
		agentId:    respAgent.Id,
	}

	return &newCli, nil
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
		Get(c.agentUrl + "/api/consume")

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
		Post(c.agentUrl + "/api/produce")

	if err != nil {
		return nil, err
	}

	if resp.IsError() {
		return nil, fmt.Errorf("API request failed with status code: %d", resp.StatusCode())
	}

	return resp.Result(), nil
}
