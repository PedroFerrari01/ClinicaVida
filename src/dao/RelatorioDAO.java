package dao;

import util.ConexaoFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Consultas agregadas usadas na tela de relatórios (JOIN + GROUP BY direto no banco).
 */
public class RelatorioDAO {

    public BigDecimal faturamentoNoPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        String sql = """
                SELECT COALESCE(SUM(valor), 0) AS total
                FROM pagamento
                WHERE data_pagamento BETWEEN ? AND ?
                """;
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(inicio.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(fim.atTime(23, 59, 59)));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        }
    }

    public Map<String, Long> consultasPorMedico() throws SQLException {
        String sql = """
                SELECT m.nome AS medico, COUNT(*) AS total
                FROM consulta c
                JOIN medico m ON m.id = c.medico_id
                WHERE c.status <> 'CANCELADA'
                GROUP BY m.nome
                ORDER BY total DESC
                """;
        return executarAgregacaoPorNome(sql);
    }

    public Map<String, Long> consultasPorConvenio() throws SQLException {
        String sql = """
                SELECT COALESCE(conv.nome, 'Particular') AS convenio, COUNT(*) AS total
                FROM consulta c
                JOIN paciente p ON p.id = c.paciente_id
                LEFT JOIN convenio conv ON conv.id = p.convenio_id
                WHERE c.status <> 'CANCELADA'
                GROUP BY conv.nome
                ORDER BY total DESC
                """;
        return executarAgregacaoPorNome(sql);
    }

    private Map<String, Long> executarAgregacaoPorNome(String sql) throws SQLException {
        Map<String, Long> resultado = new LinkedHashMap<>();
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.put(rs.getString(1), rs.getLong("total"));
            }
        }
        return resultado;
    }
}
