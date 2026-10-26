package com.exercicio1.service;

import com.exercicio1.model.Maquina;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaquinaService {

    private final List<Maquina> maquinas = new ArrayList<>();
    private Long proximoId = 1L;

    public Maquina cadastrar(Maquina maquina) {
        maquina.setId(proximoId++);
        maquinas.add(maquina);
        return maquina;
    }

    public List<Maquina> listar() {
        return maquinas;
    }

    public Maquina buscarPorId(Long id) {
        return maquinas.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Máquina não encontrada"));
    }
}