package br.com.telefonica.cobranca.service;

import br.com.telefonica.cobranca.util.Functions;
import br.com.telefonica.cobranca.util.SftpClient;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
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
}
