package Helpers;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.jcraft.jsch.JSchException;

public class Functions {    
    
    
    public static AlgorithmParameterSpec getIV() {
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);

        return ivParameterSpec;
    }
	
	public static void createFile(String path, String text) throws IOException {
	    File file = new File(path);
	    file.createNewFile();
	    
	    FileWriter fw = new FileWriter(file);
	    BufferedWriter bw = new BufferedWriter(fw);
	    bw.write(text);	
	    
	    bw.close();
	    fw.close();
    }	
	
	public static void createFileFtp() throws IOException {
		FTPClient ftp = new FTPClient();
		try
		{			
			ftp.connect("sftp://bancos.blob.core.windows.net", 22);
		}
		catch(IOException ex)
		{
			throw new IOException("Não foi possível conectar-se ao FTP.");
		}
			
		if( FTPReply.isPositiveCompletion(ftp.getReplyCode()) )
		{
			try
			{
				ftp.login("telefonica", "Ia6GM3ceCT+HfXaN+39GUnjAO9n3xe9G");
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			}
			catch(IOException ex)
			{
				throw new IOException("Não foi possível logar-se ao FTP.");
			}
		}
		else
		{
			throw new IOException("Não foi possível conectar-se ao FTP.");
		}
	}
	
	public static void createFileFtp(String hostName, String fileName, String userName, String password, String text) throws IOException {
		FTPClient ftp = new FTPClient();
		ftp.connect( hostName );
		ftp.login( userName, password );
		//ftp.changeWorkingDirectory (diretorio);

		OutputStream os = ftp.storeFileStream(fileName);
		os.write(text.getBytes());
		os.flush();
		os.close();

		ftp.logout();
		ftp.disconnect();
    }
	
	public static void createFileFtp2() throws IOException{
		FTPClient client = new FTPClient();
		FTP ftp = new FTP(
				"sftp://bancos.blob.core.windows.net", 
				"bancos.envio.telefonica", 
				"Ia6GM3ceCT+HfXaN+39GUnjAO9n3xe9G", 
				client);
		
		ftp.connect();
		//ftp.getFile("TESTE111");
		ftp.sendFTPFile("\\", "TESTE 111");
		ftp.disconnectFTP();
	}
	
	public static void createFileFtp3() throws IOException, JSchException{
		Sftp sftp = new Sftp();
		sftp.connect();
	}
	
	public static String encrypt(String plainText) throws Exception {

		Cipher cipher;
	    SecretKeySpec key ;
	    //AlgorithmParameterSpec spec;
	    String SEED_16_CHARACTER = "Olá Mundo 12345";
	    
	    MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] seedBytes = SEED_16_CHARACTER.getBytes("UTF-8");
        digest.update(seedBytes);
        cipher = Cipher.getInstance("AES");
        key = new SecretKeySpec(seedBytes, "AES");
        //spec = getIV();
		
		
		cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        //String encryptedText = new String(Base64.getE(encrypted, Base64.DEFAULT), "UTF-8");
        String encryptedText = Base64.getEncoder().encodeToString(encrypted);        		
        
        return encryptedText;
    }

    public static String decrypt(String cryptedText) throws Exception {
    	Cipher cipher;
	    SecretKeySpec key ;
	    //AlgorithmParameterSpec spec;
	    String SEED_16_CHARACTER = "Olá Mundo 12345";
	    
	    MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] seedBytes = SEED_16_CHARACTER.getBytes("UTF-8");
        digest.update(seedBytes);
        cipher = Cipher.getInstance("AES");
        key = new SecretKeySpec(seedBytes, "AES");
        //spec = getIV();
        
        
        cipher.init(Cipher.DECRYPT_MODE, key);
        //byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
        byte[] bytes = Base64.getDecoder().decode(cryptedText.getBytes());
        byte[] decrypted = cipher.doFinal(bytes);
        String decryptedText = new String(decrypted, "UTF-8");
        return decryptedText;
    }
}
