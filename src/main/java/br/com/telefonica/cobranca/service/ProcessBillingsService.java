package br.com.telefonica.cobranca.service;

import br.com.telefonica.cobranca.model.CobrancaMongoDB;
import br.com.telefonica.cobranca.repository.CobrancaMongoRepository;
import br.com.telefonica.cobranca.util.Functions;
import br.com.telefonica.cobranca.util.SftpClient;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component("processBillings")
public interface ProcessBillingsService  {
    void readBillings();

    void sendBillings();

    void uploadBillings(CobrancaMongoDB billing);

    void searchStatusThanUpload();

    void decryptAndUpdateMongo() throws Exception;

    void processesServiceOk();

    void processesServiceNOk();

}
