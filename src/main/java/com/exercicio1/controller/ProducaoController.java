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

    /** Histórico completo de produção e consumo. */
    @GetMapping
    public List<RegistroProducao> listar() {
        return service.listar();
    }

    /** Histórico de uma máquina específica. */
    @GetMapping("/maquina/{maquinaId}")
    public List<RegistroProducao> listarPorMaquina(@PathVariable Long maquinaId) {
        return service.listarPorMaquina(maquinaId);
    }

    @GetMapping("/{id}")
    public RegistroProducao buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public RegistroProducao atualizar(@PathVariable Long id, @RequestBody RegistroProducao registro) {
        return service.atualizar(id, registro);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
