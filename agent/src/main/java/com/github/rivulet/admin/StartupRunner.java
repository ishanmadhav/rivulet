package com.github.rivulet.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private Environment env;  // To get port and other environment properties

    @Override
    public void run(String... args) throws Exception {
        // Get the nodeurl (host IP) and nodeport (application port)
        String nodeurl = getHostAddress();
        String nodeport = getServerPort();

        // Prepare the request body
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("nodeurl", nodeurl);
        reqBody.put("nodeport", nodeport);

        // Use WebClient to send the POST request
        WebClient webClient = WebClient.create("http://localhost:3000");
        String response = webClient.post()
                .uri("/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(reqBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Print the response
        System.out.println("Response: " + response);
    }

    // Helper method to get the IP address (nodeurl)
    private String getHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    // Helper method to get the port (nodeport)
    private String getServerPort() {
        // Fetch the configured server port or the default port 8080
        return env.getProperty("server.port", "8080");
    }
}
