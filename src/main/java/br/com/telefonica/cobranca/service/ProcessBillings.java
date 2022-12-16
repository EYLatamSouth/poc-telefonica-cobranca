package br.com.telefonica.cobranca.service;

import br.com.telefonica.cobranca.model.CobrancaMongoDB;
import br.com.telefonica.cobranca.repository.CobrancaMongoRepository;
import br.com.telefonica.cobranca.util.Functions;
import br.com.telefonica.cobranca.util.SftpClient;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.List;

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
    
    @Autowired
    private CobrancaMongoRepository cobrancaMongoRepository;

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
                    status.setBilling_status("0");
                }catch (Exception e){
                    status.setBilling_status("1");
                }finally {
                    cobrancaMongoRepository.save(status);
                }
            });

    }

    public void decryptAndUpdateMongo() throws Exception {

        
        //fazer while / for verificando se passou por todos os arquivos do diretorio "receivedFilesDirectory" 
        Long i = null;

        Functions.decrypt("teste")

        cobrancaMongoRepository.findByBilling_id(i);

    }
}
