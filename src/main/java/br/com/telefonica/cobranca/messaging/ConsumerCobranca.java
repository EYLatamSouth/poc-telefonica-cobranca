package br.com.telefonica.cobranca.messaging;

import br.com.telefonica.cobranca.service.ProcessBillings;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.telefonica.cobranca.service.CobrancaService;
import br.com.telefonica.cobranca.messaging.ProducerCobranca;
import br.com.telefonica.cobranca.model.CobrancaMongoDB;

@Service
public class ConsumerCobranca {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerCobranca.class);
	
	@Autowired
	private CobrancaService billingService;
	
	@Autowired
	private ProducerCobranca producer;

	@Autowired
	private ProcessBillings processBillings;

    @Value("${topic.name.consumer}")
    private String topicName;
    
    @KafkaListener(topics = "${topic.name.consumer}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> payload){
    	logger.info("Tópico: {}", topicName);
    	logger.info("key: {}", payload.key());
    	logger.info("Headers: {}", payload.headers());
    	logger.info("Partion: {}", payload.partition());
    	logger.info("Order: {}", payload.value());
    	
    	String dados = payload.value();
    	
    	ObjectMapper mapper = new ObjectMapper();
    	CobrancaMongoDB billing;
    	
    	try {
			billing = mapper.readValue(dados, CobrancaMongoDB.class);
		} catch (JsonProcessingException ex) {
			logger.error("Falha ao converter os dados recebidos pelo Consumer [dados={}]", dados, ex);
			return;
		}
    	
    	logger.info("Evento Recebido = {}", billing);
    	
    	
    	CobrancaMongoDB billingSaved = billingService.save(billing);
    	
    	// producer.send(billingSaved);
    	// logger.info("Finalizou com sucesso o recebimento e envio de evento do Kafka Confluent!!!");
    }
}
