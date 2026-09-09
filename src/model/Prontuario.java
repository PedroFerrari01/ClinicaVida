package model;

import java.time.LocalDateTime;

public class Prontuario {

    private int id;
    private int consultaId;
    private String diagnostico;
    private String prescricao;
    private LocalDateTime dataRegistro;

    public Prontuario(int id, int consultaId, String diagnostico, String prescricao, LocalDateTime dataRegistro) {
        this.id = id;
        this.consultaId = consultaId;
        this.diagnostico = diagnostico;
        this.prescricao = prescricao;
        this.dataRegistro = dataRegistro;
    }

    public Prontuario(int consultaId, String diagnostico, String prescricao, LocalDateTime dataRegistro) {
        this(0, consultaId, diagnostico, prescricao, dataRegistro);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(int consultaId) {
        this.consultaId = consultaId;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
