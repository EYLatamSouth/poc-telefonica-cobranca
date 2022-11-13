package Helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
