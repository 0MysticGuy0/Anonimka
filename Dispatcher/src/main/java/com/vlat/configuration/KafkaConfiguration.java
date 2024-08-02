package com.vlat.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic textMessageTopic(@Value("${kafka.topics.text-message.name}") String topicName,
                                     @Value("${kafka.topics.text-message.partitions}") Integer partitions,
                                     @Value("${kafka.topics.text-message.replication-factor}") Integer replicationFactor,
                                     @Value("${kafka.topics.text-message.min-insync-replicas}") String minInsync ){
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .configs(Map.of("min.insync.replicas",minInsync))
                .build();
    }

    @Bean
    public NewTopic commandMessageTopic(@Value("${kafka.topics.command-message.name}") String topicName,
                                     @Value("${kafka.topics.command-message.partitions}") Integer partitions,
                                     @Value("${kafka.topics.command-message.replication-factor}") Integer replicationFactor,
                                     @Value("${kafka.topics.command-message.min-insync-replicas}") String minInsync ){
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .configs(Map.of("min.insync.replicas",minInsync))
                .build();
    }

    @Bean
    public NewTopic answerMessageTopic(@Value("${kafka.topics.answer-message.name}") String topicName,
                                        @Value("${kafka.topics.answer-message.partitions}") Integer partitions,
                                        @Value("${kafka.topics.answer-message.replication-factor}") Integer replicationFactor,
                                        @Value("${kafka.topics.answer-message.min-insync-replicas}") String minInsync ){
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .configs(Map.of("min.insync.replicas",minInsync))
                .build();
    }

    @Bean
    public NewTopic fileMessageTopic(@Value("${kafka.topics.file-message.name}") String topicName,
                                     @Value("${kafka.topics.file-message.partitions}") Integer partitions,
                                     @Value("${kafka.topics.file-message.replication-factor}") Integer replicationFactor,
                                     @Value("${kafka.topics.file-message.min-insync-replicas}") String minInsync ){
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .configs(Map.of("min.insync.replicas",minInsync))
                .build();
    }

}
