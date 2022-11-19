package br.com.telefonica.cobranca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

// import com.microsoft.applicationinsights.attach.ApplicationInsights;

@EnableKafka
@SpringBootApplication
public class MySpringBootApplication {

    /**
     * A main method to start this application.
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        // ApplicationInsights.attach();
        
        SpringApplication.run(MySpringBootApplication.class, args);
    }

}
