package com.exercicio1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MonitoramentoApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void statusDeveEstarOnline() throws Exception {
        mvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("online"));
    }

    @Test
    void versaoDeveRetornar() throws Exception {
        mvc.perform(get("/versao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versao").exists());
    }

    @Test
    void cadastrarMaquinaEListar() throws Exception {
        String body = "{\"nome\":\"Máquina CNC 01\",\"tipo\":\"CNC\",\"setor\":\"Usinagem\",\"status\":\"em operação\"}";
        mvc.perform(post("/maquinas/cadastro").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
        mvc.perform(get("/maquinas")).andExpect(status().isOk());
    }

    @Test
    void producaoParaMaquinaInexistenteRetorna404() throws Exception {
        String producao = "{\"maquinaId\":9999,\"consumoKw\":5,\"horarioAcionamento\":\"10:00\",\"pecasProduzidas\":10}";
        mvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON).content(producao))
                .andExpect(status().isNotFound());
    }
}