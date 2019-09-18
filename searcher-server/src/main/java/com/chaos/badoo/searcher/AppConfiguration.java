package com.chaos.badoo.searcher;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class AppConfiguration {

    @Bean
    public Client client() {
        Settings settings = Settings.builder()
                .put("client.transport.sniff", true)
                .put("cluster.name", "docker-cluster")
                .build();
        Client client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(new InetSocketAddress("localhost", 9300)));


        return client;
    }
}
