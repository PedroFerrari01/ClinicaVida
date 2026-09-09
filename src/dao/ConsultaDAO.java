package dao;

import model.Consulta;
import model.StatusConsulta;
import util.ConexaoFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    /**
     * Verifica se já existe uma consulta AGENDADA para o médico no mesmo horário.
     * Regra de negócio central da agenda: evita choque de horários.
     */
    public boolean existeConflito(int medicoId, LocalDateTime dataHora) throws SQLException {
        String sql = "SELECT COUNT(*) FROM consulta WHERE medico_id = ? AND data_hora = ? AND status <> 'CANCELADA'";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setTimestamp(2, Timestamp.valueOf(dataHora));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public void agendar(Consulta consulta) throws SQLException {
        if (existeConflito(consulta.getMedicoId(), consulta.getDataHora())) {
            throw new IllegalStateException("O médico já possui uma consulta marcada nesse horário.");
        }
        String sql = "INSERT INTO consulta (paciente_id, medico_id, data_hora, status, valor, observacoes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, consulta.getPacienteId());
            ps.setInt(2, consulta.getMedicoId());
            ps.setTimestamp(3, Timestamp.valueOf(consulta.getDataHora()));
            ps.setString(4, consulta.getStatus().name());
            ps.setBigDecimal(5, consulta.getValor());
            ps.setString(6, consulta.getObservacoes());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizarStatus(int consultaId, StatusConsulta status) throws SQLException {
        String sql = "UPDATE consulta SET status = ? WHERE id = ?";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, consultaId);
            ps.executeUpdate();
        }
    }

    public List<Consulta> listarAgenda(LocalDateTime inicio, LocalDateTime fim) throws SQLException {
        String sql = """
                SELECT c.id, c.paciente_id, c.medico_id, c.data_hora, c.status, c.valor, c.observacoes,
                       p.nome AS nome_paciente, m.nome AS nome_medico
                FROM consulta c
                JOIN paciente p ON p.id = c.paciente_id
                JOIN medico m ON m.id = c.medico_id
                WHERE c.data_hora BETWEEN ? AND ?
                ORDER BY c.data_hora
                """;
        List<Consulta> lista = new ArrayList<>();
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public Consulta buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT c.id, c.paciente_id, c.medico_id, c.data_hora, c.status, c.valor, c.observacoes,
                       p.nome AS nome_paciente, m.nome AS nome_medico
                FROM consulta c
                JOIN paciente p ON p.id = c.paciente_id
                JOIN medico m ON m.id = c.medico_id
                WHERE c.id = ?
                """;
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Consulta mapear(ResultSet rs) throws SQLException {
        Consulta c = new Consulta(
                rs.getInt("id"),
                rs.getInt("paciente_id"),
                rs.getInt("medico_id"),
                rs.getTimestamp("data_hora").toLocalDateTime(),
                StatusConsulta.valueOf(rs.getString("status")),
                rs.getBigDecimal("valor"),
                rs.getString("observacoes")
        );
        c.setNomePaciente(rs.getString("nome_paciente"));
        c.setNomeMedico(rs.getString("nome_medico"));
        return c;
    }
}
