package br.com.telefonica.cobranca.service;
import br.com.telefonica.cobranca.model.CobrancaMongoDB;
import br.com.telefonica.cobranca.model.ProcessBillings;
import br.com.telefonica.cobranca.repository.CobrancaMongoRepository;
import br.com.telefonica.cobranca.util.Functions;
import br.com.telefonica.cobranca.util.SftpClient;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component("processBillingsServiceImpl")
@Service
public class ProcessBillingsServiceImpl implements ProcessBillingsService {

    @Autowired
    ProcessBillings processBillings;
    CobrancaMongoRepository cobrancaMongoRepository;
    public void readBillings(){
        System.out.println("Starting reading FTP Server");
        SftpClient sftpClient = new SftpClient(processBillings.getSftpServer(), processBillings.getSendUser());

        try {
            System.out.println("Connecting");
            sftpClient.authPassword(processBillings.getSftpPass());
            System.out.println("Listing Files on SFT Server root folder /");
            List<String> fileNames = sftpClient.listFiles("/");
            File folder = new File(processBillings.getReceivedFilesDirectory());
            if(!folder.exists()){
                folder.mkdir();
            }
            for (String fileName: fileNames) {
                sftpClient.downloadFile(fileName,processBillings.getReceivedFilesDirectory()+fileName);
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
        SftpClient sftpClient = new SftpClient(processBillings.getSftpServer(), processBillings.getReturnUser());
        try {
            String billingStatus = "Fatura paga em "+new Date();
            String encryptedStatus = Functions.encrypt(billingStatus);
            String fileName = String.format("billing%1s.txt",System.currentTimeMillis());
            File sentFolder = new File(processBillings.getSentFilesDirectory());
            if(!sentFolder.exists()){
                sentFolder.mkdir();
            }
            FileOutputStream billingOutPut = new FileOutputStream(processBillings.getSentFilesDirectory()+fileName);
            billingOutPut.write(encryptedStatus.getBytes());
            System.out.println("Connecting on SFTP Server");
            sftpClient.authPassword(processBillings.getSftpPass());
            sftpClient.uploadFile(processBillings.getSentFilesDirectory()+fileName, fileName);

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
        SftpClient sftpClient = new SftpClient(processBillings.getSftpServer(), processBillings.getSendUser());
        try {
            String billingStatus = String.format("BillingID=%1$s Vencimento=%2$s Valor=%3$s", billing.getBilling_id(),
                    billing.getBilling_vencimento(), billing.getBilling_valor_fatura());
            System.out.println("------ "+billingStatus);
            String encryptedStatus = Functions.encrypt(billingStatus);
            System.out.println("------ "+encryptedStatus);
            String fileName = String.format("billing_%1s.txt", billing.getBilling_id());
            File sentFolder = new File(processBillings.getSentFilesDirectory());
            if(!sentFolder.exists()){
                sentFolder.mkdir();
            }
            FileOutputStream billingOutPut = new FileOutputStream(processBillings.getSentFilesDirectory()+fileName);
            billingOutPut.write(encryptedStatus.getBytes());
            System.out.println("Connecting on SFTP Server");
            sftpClient.authPassword(processBillings.getSftpServer());
            sftpClient.uploadFile(processBillings.getSentFilesDirectory()+fileName, fileName);

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

        List<CobrancaMongoDB> listaStatusNOK = cobrancaMongoRepository.findByBillingStatus("1");

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

        try {
            File folder = new File(processBillings.getReceivedFilesDirectory());
            for (File x : folder.listFiles()){

                Scanner sc = new Scanner(x);
                String line = sc.nextLine();

                String DecriptedLine = Functions.decrypt(line);

                String[] split = DecriptedLine.split(";");
                String idstring = split[8];
                Long id = Long.parseLong(idstring);

                CobrancaMongoDB billing = cobrancaMongoRepository.findByBillingId(id);
                if (billing != null){
                    String status = split[11];
                    billing.setBilling_status(status);
                    cobrancaMongoRepository.save(billing);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void processesServiceOk(){
        Functions.processesOkAndCopyToPath(processBillings.getReceivedFilesDirectory(), processBillings.getProcessedFilesDirectory());
    }

    public void processesServiceNOk(){
        Functions.processesNOkAndCopyToPath(processBillings.getReceivedFilesDirectory(), processBillings.getProcessedFilesDirectory());
    }
}
