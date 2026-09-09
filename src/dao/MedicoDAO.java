package dao;

import model.Medico;
import util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    public void salvar(Medico medico) throws SQLException {
        String sql = "INSERT INTO medico (nome, crm, especialidade) VALUES (?, ?, ?)";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getCrm());
            ps.setString(3, medico.getEspecialidade());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    medico.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Medico> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, crm, especialidade FROM medico ORDER BY nome";
        List<Medico> lista = new ArrayList<>();
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Medico buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, crm, especialidade FROM medico WHERE id = ?";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Medico mapear(ResultSet rs) throws SQLException {
        return new Medico(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("crm"),
                rs.getString("especialidade")
        );
    }
}
