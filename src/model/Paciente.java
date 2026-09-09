package model;

import java.time.LocalDate;

public class Paciente {

    private int id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private Integer convenioId; // pode ser null (particular)

    public Paciente(int id, String nome, String cpf, LocalDate dataNascimento,
                     String telefone, String email, Integer convenioId) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.convenioId = convenioId;
    }

    public Paciente(String nome, String cpf, LocalDate dataNascimento,
                     String telefone, String email, Integer convenioId) {
        this(0, nome, cpf, dataNascimento, telefone, email, convenioId);
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getConvenioId() {
        return convenioId;
    }

    public void setConvenioId(Integer convenioId) {
        this.convenioId = convenioId;
    }

    @Override
    public String toString() {
        return nome + " (CPF " + cpf + ")";
    }
}
