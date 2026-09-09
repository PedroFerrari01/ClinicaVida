package dao;

import model.Prontuario;
import util.ConexaoFactory;

import java.sql.*;

public class ProntuarioDAO {

    public void salvar(Prontuario prontuario) throws SQLException {
        String sql = "INSERT INTO prontuario (consulta_id, diagnostico, prescricao, data_registro) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, prontuario.getConsultaId());
            ps.setString(2, prontuario.getDiagnostico());
            ps.setString(3, prontuario.getPrescricao());
            ps.setTimestamp(4, Timestamp.valueOf(prontuario.getDataRegistro()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    prontuario.setId(rs.getInt(1));
                }
            }
        }
    }

    public Prontuario buscarPorConsulta(int consultaId) throws SQLException {
        String sql = "SELECT id, consulta_id, diagnostico, prescricao, data_registro FROM prontuario WHERE consulta_id = ?";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consultaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Prontuario(
                        rs.getInt("id"),
                        rs.getInt("consulta_id"),
                        rs.getString("diagnostico"),
                        rs.getString("prescricao"),
                        rs.getTimestamp("data_registro").toLocalDateTime()
                );
            }
        }
    }
}
