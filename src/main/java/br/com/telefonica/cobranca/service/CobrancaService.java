package br.com.telefonica.cobranca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.telefonica.cobranca.model.CobrancaMongoDB;
import br.com.telefonica.cobranca.repository.CobrancaMongoRepository;

@Service
public interface CobrancaService {
	public CobrancaMongoDB save(CobrancaMongoDB billingMongoDB);
}
