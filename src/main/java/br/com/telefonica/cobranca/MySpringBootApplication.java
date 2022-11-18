package br.com.telefonica.cobranca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MySpringBootApplication {

    /**
     * A main method to start this application.
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MySpringBootApplication.class, args);
        
        String text = "abc123";
        //String textEncript = Helpers.Functions.encrypt(text);
        //String textDecript = Helpers.Functions.decrypt(textEncript);
        
        //Helpers.Functions.createFile("TestFile1.txt", text);
        //Helpers.Functions.createFile("TestFile2.txt", textEncript);
        //Helpers.Functions.createFile("TestFile3.txt", textDecript);
        
        Helpers.Functions.createFileFtp3();
        /*Helpers.Functions.createFileFtp(
        		"bancos.envio.telefonica@bancos.blob.core.windows.net", 
        		"TestFile1.txt", 
        		"telefonica", 
        		"Ia6GM3ceCT+HfXaN+39GUnjAO9n3xe9G", 
        		"texto criptografado teste 1");*/
    }

}
