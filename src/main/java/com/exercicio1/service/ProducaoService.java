package com.exercicio1.service;

import com.exercicio1.model.RegistroProducao;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProducaoService {

    private final List<RegistroProducao> registros = new CopyOnWriteArrayList<>();
    private final AtomicLong proximoId = new AtomicLong(1);
    private final MaquinaService maquinaService;

    public ProducaoService(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    public RegistroProducao registrar(RegistroProducao registro) {
        maquinaService.buscarPorId(registro.getMaquinaId()); // 404 se a máquina não existir
        registro.setId(proximoId.getAndIncrement());
        registros.add(registro);
        return registro;
    }

    public List<RegistroProducao> listar() {
        return new ArrayList<>(registros);
    }

    public List<RegistroProducao> listarPorMaquina(Long maquinaId) {
        maquinaService.buscarPorId(maquinaId);
        List<RegistroProducao> resultado = new ArrayList<>();
        for (RegistroProducao r : registros) {
            if (r.getMaquinaId().equals(maquinaId)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public RegistroProducao buscarPorId(Long id) {
        return registros.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de produção não encontrado"));
    }

    public RegistroProducao atualizar(Long id, RegistroProducao dados) {
        RegistroProducao r = buscarPorId(id);
        if (dados.getMaquinaId() != null) {
            maquinaService.buscarPorId(dados.getMaquinaId());
            r.setMaquinaId(dados.getMaquinaId());
        }
        r.setConsumoKw(dados.getConsumoKw());
        r.setHorarioAcionamento(dados.getHorarioAcionamento());
        r.setPecasProduzidas(dados.getPecasProduzidas());
        return r;
    }

    public void remover(Long id) {
        registros.remove(buscarPorId(id));
    }
}
