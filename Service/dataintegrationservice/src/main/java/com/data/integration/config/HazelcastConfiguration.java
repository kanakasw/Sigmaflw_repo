package com.data.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.QueueConfig;

@Configuration
public class HazelcastConfiguration {

    public static final String EXECUTEINTEGRATIONPROCESSQUEUE = "executeIntegrationProcessQueue";

    // When Spring Boot find a com.hazelcast.config.Config Automatically
    // instantiate a HazelcastInstance
    @Bean
    public Config configureHazalcast() {
        
        Config config = new Config();
        
        // Hazalcast only for single server
        config.setProperty("hazelcast.shutdownhook.enabled", "true");
        config.setProperty("hazelcast.io.thread.count", "1");
        config.setProperty("hazelcast.operation.thread.count", "1");
        config.setProperty("hazelcast.operation.generic.thread.count", "1");
        config.setProperty("hazelcast.event.thread.count", "1");
        config.setProperty("hazelcast.health.monitoring.level", "OFF");
        
        NetworkConfig network = config.getNetworkConfig();
        config.getPartitionGroupConfig().setEnabled(false);
        network.getJoin().getTcpIpConfig().setEnabled(false);
        network.getJoin().getMulticastConfig().setEnabled(false);
        
        config.addQueueConfig(new QueueConfig()
                .setName(EXECUTEINTEGRATIONPROCESSQUEUE).setBackupCount(1)
                .setMaxSize(100).setStatisticsEnabled(true));

        return config;
    }

}
