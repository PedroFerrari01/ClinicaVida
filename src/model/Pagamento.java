package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {

    private int id;
    private int consultaId;
    private BigDecimal valor;
    private FormaPagamento formaPagamento;
    private LocalDateTime dataPagamento;

    public Pagamento(int id, int consultaId, BigDecimal valor, FormaPagamento formaPagamento, LocalDateTime dataPagamento) {
        this.id = id;
        this.consultaId = consultaId;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.dataPagamento = dataPagamento;
    }

    public Pagamento(int consultaId, BigDecimal valor, FormaPagamento formaPagamento, LocalDateTime dataPagamento) {
        this(0, consultaId, valor, formaPagamento, dataPagamento);
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
