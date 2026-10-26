package com.exercicio1.service;

import com.exercicio1.model.RegistroProducao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProducaoService {

    private final List<RegistroProducao> registros = new ArrayList<>();
    private final MaquinaService maquinaService;

    public ProducaoService(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    public RegistroProducao registrar(RegistroProducao registro) {
        maquinaService.buscarPorId(registro.getMaquinaId()); // 404 se a máquina não existir
        registros.add(registro);
        return registro;
    }

    public List<RegistroProducao> listar() {
        return registros;
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
}