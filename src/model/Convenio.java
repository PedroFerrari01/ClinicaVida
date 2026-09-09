package model;

import java.math.BigDecimal;

public class Convenio {

    private int id;
    private String nome;
    private BigDecimal valorConsulta;

    public Convenio(int id, String nome, BigDecimal valorConsulta) {
        this.id = id;
        this.nome = nome;
        this.valorConsulta = valorConsulta;
    }

    public Convenio(String nome, BigDecimal valorConsulta) {
        this(0, nome, valorConsulta);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(BigDecimal valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    @Override
    public String toString() {
        return nome + " (R$ " + valorConsulta + ")";
    }
}
