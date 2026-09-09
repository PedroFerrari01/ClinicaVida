package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Consulta {

    private int id;
    private int pacienteId;
    private int medicoId;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private BigDecimal valor;
    private String observacoes;

    // Campos auxiliares só para exibição em telas (não persistidos diretamente aqui)
    private String nomePaciente;
    private String nomeMedico;

    public Consulta(int id, int pacienteId, int medicoId, LocalDateTime dataHora,
                     StatusConsulta status, BigDecimal valor, String observacoes) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.status = status;
        this.valor = valor;
        this.observacoes = observacoes;
    }

    public Consulta(int pacienteId, int medicoId, LocalDateTime dataHora, BigDecimal valor, String observacoes) {
        this(0, pacienteId, medicoId, dataHora, StatusConsulta.AGENDADA, valor, observacoes);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public int getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(int medicoId) {
        this.medicoId = medicoId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    @Override
    public String toString() {
        return String.format("#%d | %s | %s | %s", id, dataHora, status,
                nomePaciente != null ? nomePaciente : "paciente " + pacienteId);
    }
}
