package br.com.telefonica.cobranca.service;

import br.com.telefonica.cobranca.model.CobrancaMongoDB;
import br.com.telefonica.cobranca.repository.CobrancaMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CobrancaServiceImpl implements CobrancaService {
	private static final String BILLING_EM_USO = "O Billing de código %b não pode ser removido, pois está em uso";

	@Autowired
	CobrancaMongoRepository billingMongoRepository;

	public CobrancaMongoDB save(CobrancaMongoDB billingMongoDB) {
		CobrancaMongoDB billingSaved = billingMongoRepository.save(billingMongoDB);
		return billingSaved;
	}
}
