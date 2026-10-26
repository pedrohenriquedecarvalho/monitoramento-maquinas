package com.exercicio1.controller;

import com.exercicio1.model.RegistroProducao;
import com.exercicio1.service.ProducaoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producao")
public class ProducaoController {

    private final ProducaoService service;

    public ProducaoController(ProducaoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroProducao registrar(@RequestBody RegistroProducao registro) {
        return service.registrar(registro);
    }

    @GetMapping
    public List<RegistroProducao> listar() {
        return service.listar();
    }

    @GetMapping("/{maquinaId}")
    public List<RegistroProducao> listarPorMaquina(@PathVariable Long maquinaId) {
        return service.listarPorMaquina(maquinaId);
    }
}