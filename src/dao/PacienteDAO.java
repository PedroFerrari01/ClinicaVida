package dao;

import model.Paciente;
import util.ConexaoFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public void salvar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (nome, cpf, data_nascimento, telefone, email, convenio_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getCpf());
            ps.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            ps.setString(4, paciente.getTelefone());
            ps.setString(5, paciente.getEmail());
            if (paciente.getConvenioId() != null) {
                ps.setInt(6, paciente.getConvenioId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    paciente.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente SET nome=?, cpf=?, data_nascimento=?, telefone=?, email=?, convenio_id=? WHERE id=?";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getCpf());
            ps.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            ps.setString(4, paciente.getTelefone());
            ps.setString(5, paciente.getEmail());
            if (paciente.getConvenioId() != null) {
                ps.setInt(6, paciente.getConvenioId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setInt(7, paciente.getId());
            ps.executeUpdate();
        }
    }

    public List<Paciente> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, email, convenio_id FROM paciente ORDER BY nome";
        List<Paciente> lista = new ArrayList<>();
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Paciente> buscarPorNome(String termo) throws SQLException {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, email, convenio_id FROM paciente WHERE nome LIKE ? ORDER BY nome";
        List<Paciente> lista = new ArrayList<>();
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, email, convenio_id FROM paciente WHERE id = ?";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        int convenioId = rs.getInt("convenio_id");
        Integer convenioIdObj = rs.wasNull() ? null : convenioId;
        return new Paciente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getDate("data_nascimento").toLocalDate(),
                rs.getString("telefone"),
                rs.getString("email"),
                convenioIdObj
        );
    }
}
