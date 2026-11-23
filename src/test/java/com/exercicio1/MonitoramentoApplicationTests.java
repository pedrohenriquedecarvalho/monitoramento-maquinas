package com.exercicio1;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MonitoramentoApplicationTests {

    private static final String MAQUINA_JSON =
            "{\"nome\":\"Máquina CNC 01\",\"tipo\":\"CNC\",\"setor\":\"Usinagem\",\"status\":\"em operação\"}";

    @Autowired
    private MockMvc mvc;

    /** Cadastra uma máquina e devolve o id gerado. */
    private int cadastrarMaquina() throws Exception {
        MvcResult r = mvc.perform(post("/maquinas/cadastro")
                        .contentType(MediaType.APPLICATION_JSON).content(MAQUINA_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        return JsonPath.read(r.getResponse().getContentAsString(), "$.id");
    }

    private String producaoJson(int maquinaId) {
        return "{\"maquinaId\":" + maquinaId
                + ",\"consumoKw\":12.5,\"horarioAcionamento\":\"08:30\",\"pecasProduzidas\":150}";
    }

    // ---------- a) página inicial ----------
    @Test
    void paginaInicialRespondeCorretamente() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").exists());
    }

    // ---------- b) /status ----------
    @Test
    void statusDeveEstarOnline() throws Exception {
        mvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("online"))
                .andExpect(jsonPath("$.mensagem").value("Aplicação de monitoramento em execução"));
    }

    @Test
    void versaoDeveRetornar() throws Exception {
        mvc.perform(get("/versao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versao").exists());
    }

    // ---------- c) cadastrar máquina ----------
    @Test
    void cadastrarMaquinaRetorna201() throws Exception {
        mvc.perform(post("/maquinas/cadastro").contentType(MediaType.APPLICATION_JSON).content(MAQUINA_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Máquina CNC 01"))
                .andExpect(jsonPath("$.tipo").value("CNC"))
                .andExpect(jsonPath("$.setor").value("Usinagem"))
                .andExpect(jsonPath("$.status").value("em operação"));
    }

    // ---------- d) listar máquinas ----------
    @Test
    void listarMaquinasRetornaListaComCadastradas() throws Exception {
        cadastrarMaquina();
        mvc.perform(get("/maquinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].nome").exists());
    }

    @Test
    void buscarMaquinaPorId() throws Exception {
        int id = cadastrarMaquina();
        mvc.perform(get("/maquinas/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void buscarMaquinaInexistenteRetorna404() throws Exception {
        mvc.perform(get("/maquinas/99999")).andExpect(status().isNotFound());
    }

    @Test
    void atualizarMaquinaRetorna200() throws Exception {
        int id = cadastrarMaquina();
        String novo = "{\"nome\":\"Máquina CNC 01\",\"tipo\":\"CNC\",\"setor\":\"Usinagem\",\"status\":\"parada\"}";
        mvc.perform(put("/maquinas/" + id).contentType(MediaType.APPLICATION_JSON).content(novo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("parada"));
    }

    @Test
    void atualizarMaquinaInexistenteRetorna404() throws Exception {
        mvc.perform(put("/maquinas/99999").contentType(MediaType.APPLICATION_JSON).content(MAQUINA_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void removerMaquinaRetorna204ESomeDaBusca() throws Exception {
        int id = cadastrarMaquina();
        mvc.perform(delete("/maquinas/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/maquinas/" + id)).andExpect(status().isNotFound());
    }

    // ---------- e) registrar produção ----------
    @Test
    void registrarProducaoRetorna201() throws Exception {
        int maquinaId = cadastrarMaquina();
        mvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON).content(producaoJson(maquinaId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.maquinaId").value(maquinaId))
                .andExpect(jsonPath("$.consumoKw").value(12.5))
                .andExpect(jsonPath("$.horarioAcionamento").value("08:30"))
                .andExpect(jsonPath("$.pecasProduzidas").value(150));
    }

    @Test
    void producaoParaMaquinaInexistenteRetorna404() throws Exception {
        mvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON).content(producaoJson(9999)))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarProducaoEHistoricoPorMaquina() throws Exception {
        int maquinaId = cadastrarMaquina();
        mvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON).content(producaoJson(maquinaId)))
                .andExpect(status().isCreated());

        mvc.perform(get("/producao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        mvc.perform(get("/producao/maquina/" + maquinaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].maquinaId").value(maquinaId));
    }

    @Test
    void atualizarERemoverProducao() throws Exception {
        int maquinaId = cadastrarMaquina();
        MvcResult r = mvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON)
                        .content(producaoJson(maquinaId)))
                .andExpect(status().isCreated()).andReturn();
        int id = JsonPath.read(r.getResponse().getContentAsString(), "$.id");

        String novo = "{\"maquinaId\":" + maquinaId
                + ",\"consumoKw\":20.0,\"horarioAcionamento\":\"09:00\",\"pecasProduzidas\":300}";
        mvc.perform(put("/producao/" + id).contentType(MediaType.APPLICATION_JSON).content(novo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pecasProduzidas").value(300));

        mvc.perform(delete("/producao/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/producao/" + id)).andExpect(status().isNotFound());
    }
}
