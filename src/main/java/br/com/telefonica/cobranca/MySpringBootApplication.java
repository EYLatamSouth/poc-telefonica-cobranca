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
        String textEncript = Helpers.Functions.encrypt(text);
        String textDecript = Helpers.Functions.decrypt(textEncript);
        
        Helpers.Functions.createFile("TestFile1.txt", text);
        Helpers.Functions.createFile("TestFile2.txt", textEncript);
        Helpers.Functions.createFile("TestFile3.txt", textDecript);
    }

}
