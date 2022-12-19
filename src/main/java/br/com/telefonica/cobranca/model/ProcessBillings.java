package br.com.telefonica.cobranca.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String getSftpServer() {
        return sftpServer;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getReturnUser() {
        return returnUser;
    }

    public void setReturnUser(String returnUser) {
        this.returnUser = returnUser;
    }

    public String getSftpPass() {
        return sftpPass;
    }

    public void setSftpPass(String sftpPass) {
        this.sftpPass = sftpPass;
    }

    public String getReceivedFilesDirectory() {
        return receivedFilesDirectory;
    }

    public void setReceivedFilesDirectory(String receivedFilesDirectory) {
        this.receivedFilesDirectory = receivedFilesDirectory;
    }

    public String getSentFilesDirectory() {
        return sentFilesDirectory;
    }

    public void setSentFilesDirectory(String sentFilesDirectory) {
        this.sentFilesDirectory = sentFilesDirectory;
    }

    public String getProcessedFilesDirectory() {
        return processedFilesDirectory;
    }

    public void setProcessedFilesDirectory(String processedFilesDirectory) {
        this.processedFilesDirectory = processedFilesDirectory;
    }

    public void setSftpServer(String sftpServer) {
        this.sftpServer = sftpServer;
    }

    @Value("${directory.sent}")
    private String sentFilesDirectory;

    @Value("${directory.processed}")
    private String processedFilesDirectory;
}