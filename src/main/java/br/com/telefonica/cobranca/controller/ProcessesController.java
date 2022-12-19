package br.com.telefonica.cobranca.controller;

import br.com.telefonica.cobranca.service.ProcessBillingsService;
import br.com.telefonica.cobranca.util.Functions;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/processes")
public class ProcessesController {

    @Autowired
    private ProcessBillingsService processBillingsService;

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbiden"),
            @ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 422, message = "Unprocessable Entity"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 502, message = "Bad Gateway"),
            @ApiResponse(code = 503, message = "Service Unavailable"),
            @ApiResponse(code = 504, message = "Gateway Timeout"), })
    @GetMapping("/ok")
    public ResponseEntity<?> processesOk() {

        try {
            processBillingsService.processesServiceOk();
            System.out.println("Arquivo processado OK, está na pasta Processed");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            String erro = "Erro no processesOk";

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    public ResponseEntity<?> processesNOk() {

        try {
            processBillingsService.processesServiceNOk();
            System.out.println("Arquivo processado NOK, está na pasta Processed");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            String erro = "Erro no processesNOk";

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
}
