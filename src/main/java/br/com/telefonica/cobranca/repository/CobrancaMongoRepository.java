package br.com.telefonica.cobranca.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.telefonica.cobranca.model.CobrancaMongoDB;

import java.util.List;

@Repository
public interface CobrancaMongoRepository extends MongoRepository<CobrancaMongoDB, Integer>{

    List<CobrancaMongoDB> findByBilling_status(String status);

}
