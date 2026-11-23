package com.exercicio1.controller;

import com.exercicio1.model.Maquina;
import com.exercicio1.service.MaquinaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maquinas")
public class MaquinaController {

    private final MaquinaService service;

    public MaquinaController(MaquinaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Maquina> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Maquina buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public Maquina cadastrar(@RequestBody Maquina maquina) {
        return service.cadastrar(maquina);
    }

    @PutMapping("/{id}")
    public Maquina atualizar(@PathVariable Long id, @RequestBody Maquina maquina) {
        return service.atualizar(id, maquina);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
