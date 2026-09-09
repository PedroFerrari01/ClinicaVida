package dao;

import model.Convenio;
import util.ConexaoFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConvenioDAO {

    public void salvar(Convenio convenio) throws SQLException {
        String sql = "INSERT INTO convenio (nome, valor_consulta) VALUES (?, ?)";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, convenio.getNome());
            ps.setBigDecimal(2, convenio.getValorConsulta());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    convenio.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Convenio> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, valor_consulta FROM convenio ORDER BY nome";
        List<Convenio> lista = new ArrayList<>();
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Convenio buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, valor_consulta FROM convenio WHERE id = ?";
        try (Connection con = ConexaoFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Convenio mapear(ResultSet rs) throws SQLException {
        return new Convenio(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getBigDecimal("valor_consulta")
        );
    }
}
