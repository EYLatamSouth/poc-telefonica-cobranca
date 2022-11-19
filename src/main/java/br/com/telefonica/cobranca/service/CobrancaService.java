package br.com.telefonica.cobranca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.telefonica.cobranca.model.CobrancaMongoDB;
import br.com.telefonica.cobranca.repository.CobrancaMongoRepository;

@Service
public class CobrancaService {

	private static final String BILLING_EM_USO = "O Billing de código %b não pode ser removido, pois está em uso";
	
	@Autowired
	CobrancaMongoRepository billingMongoRepository; 
	
	public CobrancaMongoDB save(CobrancaMongoDB billingMongoDB) {
		CobrancaMongoDB billingSaved = billingMongoRepository.save(billingMongoDB);
		return billingSaved;
	}
	
	// public BillingMongoDB getAllPendingToSend(){

	// }
}
