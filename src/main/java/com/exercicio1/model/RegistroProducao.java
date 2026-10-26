package com.exercicio1.model;

public class RegistroProducao {

    private Long maquinaId;
    private Double consumoKw;
    private String horarioAcionamento;
    private Integer pecasProduzidas;

    public RegistroProducao() {}

    public RegistroProducao(Long maquinaId, Double consumoKw, String horarioAcionamento, Integer pecasProduzidas) {
        this.maquinaId = maquinaId;
        this.consumoKw = consumoKw;
        this.horarioAcionamento = horarioAcionamento;
        this.pecasProduzidas = pecasProduzidas;
    }

    public Long getMaquinaId() { return maquinaId; }
    public void setMaquinaId(Long maquinaId) { this.maquinaId = maquinaId; }

    public Double getConsumoKw() { return consumoKw; }
    public void setConsumoKw(Double consumoKw) { this.consumoKw = consumoKw; }

    public String getHorarioAcionamento() { return horarioAcionamento; }
    public void setHorarioAcionamento(String horarioAcionamento) { this.horarioAcionamento = horarioAcionamento; }

    public Integer getPecasProduzidas() { return pecasProduzidas; }
    public void setPecasProduzidas(Integer pecasProduzidas) { this.pecasProduzidas = pecasProduzidas; }
}