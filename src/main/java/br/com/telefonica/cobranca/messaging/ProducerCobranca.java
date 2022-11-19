package br.com.telefonica.cobranca.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.telefonica.cobranca.model.CobrancaMongoDB;


@Service
public class ProducerCobranca {

	private static final Logger logger = LoggerFactory.getLogger(ProducerCobranca.class);
	
    @Value("${topic.name.producer}")
    private String topicName;

    private KafkaTemplate<String, CobrancaMongoDB> kafkaTemplate = null;
    
    public ProducerCobranca(@Value("${topic.name.producer}") String topic,  KafkaTemplate<String, CobrancaMongoDB> kafkaTemplate) {
        this.topicName = topic;
        this.kafkaTemplate = kafkaTemplate;
	}

    public void send(CobrancaMongoDB billing){
        kafkaTemplate.send(topicName, billing).addCallback(
                success -> logger.info("Messagem send: " + success.getProducerRecord().value()),
                failure -> logger.info("Message failure: " + failure.getMessage())
        );
    }
}
