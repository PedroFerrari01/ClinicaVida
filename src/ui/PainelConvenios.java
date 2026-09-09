package ui;

import dao.ConvenioDAO;
import model.Convenio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PainelConvenios extends JPanel {

    private final ConvenioDAO convenioDAO = new ConvenioDAO();

    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoValor = new JTextField(10);

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Valor da consulta"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelConvenios() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Novo convênio"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; form.add(campoNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Valor da consulta (R$):"), gbc);
        gbc.gridx = 1; form.add(campoValor, gbc);

        JButton botaoSalvar = new JButton("Cadastrar convênio");
        botaoSalvar.addActionListener(e -> salvar());
        gbc.gridx = 1; gbc.gridy = 2;
        form.add(botaoSalvar, gbc);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        atualizarTabela();
    }

    private void salvar() {
        try {
            if (campoNome.getText().isBlank() || campoValor.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Nome e valor são obrigatórios.");
                return;
            }
            BigDecimal valor = new BigDecimal(campoValor.getText().trim().replace(",", "."));
            convenioDAO.salvar(new Convenio(campoNome.getText().trim(), valor));
            campoNome.setText("");
            campoValor.setText("");
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Convênio cadastrado com sucesso!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        try {
            List<Convenio> convenios = convenioDAO.listarTodos();
            modeloTabela.setRowCount(0);
            for (Convenio c : convenios) {
                modeloTabela.addRow(new Object[]{c.getId(), c.getNome(), "R$ " + c.getValorConsulta()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
