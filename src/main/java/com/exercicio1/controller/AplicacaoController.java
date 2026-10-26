package com.exercicio1.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AplicacaoController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("mensagem", "API de Monitoramento de Máquinas - CloudTech Solutions");
    }

    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of(
            "status", "online",
            "mensagem", "Aplicação de monitoramento em execução"
        );
    }

    @GetMapping("/versao")
    public Map<String, String> versao() {
        return Map.of("versao", "1.0.0");
    }
}

