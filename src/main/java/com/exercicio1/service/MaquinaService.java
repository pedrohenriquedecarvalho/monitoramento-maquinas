package com.exercicio1.service;

import com.exercicio1.model.Maquina;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MaquinaService {

    private final List<Maquina> maquinas = new CopyOnWriteArrayList<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    public Maquina cadastrar(Maquina maquina) {
        maquina.setId(proximoId.getAndIncrement());
        maquinas.add(maquina);
        return maquina;
    }

    public List<Maquina> listar() {
        return new ArrayList<>(maquinas);
    }

    public Maquina buscarPorId(Long id) {
        return maquinas.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Máquina não encontrada"));
    }

    public Maquina atualizar(Long id, Maquina dados) {
        Maquina m = buscarPorId(id);
        m.setNome(dados.getNome());
        m.setTipo(dados.getTipo());
        m.setSetor(dados.getSetor());
        m.setStatus(dados.getStatus());
        return m;
    }

    public void remover(Long id) {
        maquinas.remove(buscarPorId(id));
    }
}
