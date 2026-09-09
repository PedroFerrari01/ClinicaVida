package dao;

import model.FormaPagamento;
import model.Pagamento;
import util.ConexaoFactory;

import java.sql.*;

public class PagamentoDAO {

    public void salvar(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO pagamento (consulta_id, valor, forma_pagamento, data_pagamento) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pagamento.getConsultaId());
            ps.setBigDecimal(2, pagamento.getValor());
            ps.setString(3, pagamento.getFormaPagamento().name());
            ps.setTimestamp(4, Timestamp.valueOf(pagamento.getDataPagamento()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pagamento.setId(rs.getInt(1));
                }
            }
        }
    }

    public Pagamento buscarPorConsulta(int consultaId) throws SQLException {
        String sql = "SELECT id, consulta_id, valor, forma_pagamento, data_pagamento FROM pagamento WHERE consulta_id = ?";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consultaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Pagamento(
                        rs.getInt("id"),
                        rs.getInt("consulta_id"),
                        rs.getBigDecimal("valor"),
                        FormaPagamento.valueOf(rs.getString("forma_pagamento")),
                        rs.getTimestamp("data_pagamento").toLocalDateTime()
                );
            }
        }
    }
}
