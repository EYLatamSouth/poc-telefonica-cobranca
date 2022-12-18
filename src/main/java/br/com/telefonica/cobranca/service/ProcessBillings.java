package br.com.telefonica.cobranca.service;

import br.com.telefonica.cobranca.messaging.ProducerCobranca;
import br.com.telefonica.cobranca.model.CobrancaMongoDB;
import br.com.telefonica.cobranca.repository.CobrancaMongoRepository;
import br.com.telefonica.cobranca.util.Functions;
import br.com.telefonica.cobranca.util.SftpClient;
import br.com.telefonica.cobranca.util.Status;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component("processBillings")
public class ProcessBillings {
    @Value("${config.sftp.server}")
    private String sftpServer;

    @Value("${config.sftp.sendUser}")
    private String sendUser;

    @Value("${config.sftp.returnUser}")
    private String returnUser;

    @Value("${config.sftp.password}")
    private String sftpPass;

    @Value("${directory.received}")
    private String receivedFilesDirectory;

    @Value("${directory.sent}")
    private String sentFilesDirectory;

    @Value("${directory.processed}")
    private String processedFilesDirectory;
    
    @Autowired
    private CobrancaMongoRepository cobrancaMongoRepository;

    @Autowired
    private ProducerCobranca producerCobranca;

    public void readBillings(){
        System.out.println("Starting reading FTP Server");
        SftpClient sftpClient = new SftpClient(sftpServer, sendUser);

        try {
            System.out.println("Connecting");
            sftpClient.authPassword(sftpPass);
            System.out.println("Listing Files on SFT Server root folder /");
            List<String> fileNames = sftpClient.listFiles("/");
            File folder = new File(receivedFilesDirectory);
            if(!folder.exists()){
                folder.mkdir();
            }
            for (String fileName: fileNames) {
                sftpClient.downloadFile(fileName,receivedFilesDirectory+fileName);
            }
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } finally {
            sftpClient.close();
        }

    }

    public void sendBillings(){
        SftpClient sftpClient = new SftpClient(sftpServer, returnUser);
        try {
            String billingStatus = "Fatura paga em "+new Date();
            String encryptedStatus = Functions.encrypt(billingStatus);
            String fileName = String.format("billing%1s.txt",System.currentTimeMillis());
            File sentFolder = new File(sentFilesDirectory);
            if(!sentFolder.exists()){
                sentFolder.mkdir();
            }
            FileOutputStream billingOutPut = new FileOutputStream(sentFilesDirectory+fileName);
            billingOutPut.write(encryptedStatus.getBytes());
            System.out.println("Connecting on SFTP Server");
            sftpClient.authPassword(sftpPass);
            sftpClient.uploadFile(sentFilesDirectory+fileName, fileName);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            sftpClient.close();
        }

    }

    public void uploadBillings(CobrancaMongoDB billing){
        SftpClient sftpClient = new SftpClient(sftpServer, sendUser);
        try {
            String billingStatus = String.format("BillingID=%1$s Vencimento=%2$s Valor=%3$s", billing.getBilling_id(),
                    billing.getBilling_vencimento(), billing.getBilling_valor_fatura());
            System.out.println("------ "+billingStatus);
            String encryptedStatus = Functions.encrypt(billingStatus);
            System.out.println("------ "+encryptedStatus);
            String fileName = String.format("billing_%1s.txt", billing.getBilling_id());
            File sentFolder = new File(sentFilesDirectory);
            if(!sentFolder.exists()){
                sentFolder.mkdir();
            }
            FileOutputStream billingOutPut = new FileOutputStream(sentFilesDirectory+fileName);
            billingOutPut.write(encryptedStatus.getBytes());
            System.out.println("Connecting on SFTP Server");
            sftpClient.authPassword(sftpPass);
            sftpClient.uploadFile(sentFilesDirectory+fileName, fileName);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            sftpClient.close();
        }
    }

    public void searchStatusThanUpload(){

        List<CobrancaMongoDB> listaStatusNOK = cobrancaMongoRepository.findByBilling_status("1");

            listaStatusNOK.forEach(status -> {
                try {
                    uploadBillings(status);
                    status.setBilling_status(Status.Processed.toString());
                }catch (Exception e){
                    status.setBilling_status(Status.NotProcessed.toString());
                }finally {
                    cobrancaMongoRepository.save(status);
                }
            });

    }

    public void decryptAndUpdateMongo() throws Exception {

        try {
            File folder = new File(receivedFilesDirectory);
            for (File x : folder.listFiles()){

                Scanner sc = new Scanner(x);
                String line = sc.nextLine();

                String DecriptedLine = Functions.decrypt(line);

                String[] split = DecriptedLine.split(";");
                String idstring = split[8];
                Long id = Long.parseLong(idstring);

                CobrancaMongoDB billing = cobrancaMongoRepository.findByBilling_id(id);
                if (billing != null){
                    String status = split[11];
                    billing.setBilling_status(status);
                    cobrancaMongoRepository.save(billing);
                    producerCobranca.send(billing);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void processesServiceOk(){
        Functions.processesOkAndCopyToPath(receivedFilesDirectory, processedFilesDirectory);
    }

    public void processesServiceNOk(){
        Functions.processesNOkAndCopyToPath(receivedFilesDirectory, processedFilesDirectory);
    }
}
