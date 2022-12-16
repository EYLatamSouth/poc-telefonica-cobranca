package br.com.telefonica.cobranca.scheduler;

import br.com.telefonica.cobranca.service.ProcessBillings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerProcessesBilling {

    @Autowired
    private ProcessBillings processBillings;

    @Scheduled(cron = "0 0 18 * * 2,4", zone = "America/Sao_Paulo")
    public void rotinaCadastroDespesasApKmOnline() {
        System.out.println("---------- COMECANDO ROTINA DE PROCURAR STATUS E FAZER UPLOAD PARA BANCO ----------");
        try {
            processBillings.searchStatusThanUpload();
        } catch (Exception e){
            throw new RuntimeException();
        }

    }

}
