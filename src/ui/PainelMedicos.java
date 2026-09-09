package ui;

import dao.MedicoDAO;
import model.Medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelMedicos extends JPanel {

    private final MedicoDAO medicoDAO = new MedicoDAO();

    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoCrm = new JTextField(16);
    private final JTextField campoEspecialidade = new JTextField(16);

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Nome", "CRM", "Especialidade"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelMedicos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Novo médico"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; form.add(campoNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("CRM:"), gbc);
        gbc.gridx = 1; form.add(campoCrm, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Especialidade:"), gbc);
        gbc.gridx = 1; form.add(campoEspecialidade, gbc);

        JButton botaoSalvar = new JButton("Cadastrar médico");
        botaoSalvar.addActionListener(e -> salvar());
        gbc.gridx = 1; gbc.gridy = 3;
        form.add(botaoSalvar, gbc);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        atualizarTabela();
    }

    private void salvar() {
        try {
            if (campoNome.getText().isBlank() || campoCrm.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Nome e CRM são obrigatórios.");
                return;
            }
            medicoDAO.salvar(new Medico(campoNome.getText().trim(), campoCrm.getText().trim(), campoEspecialidade.getText().trim()));
            campoNome.setText("");
            campoCrm.setText("");
            campoEspecialidade.setText("");
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Médico cadastrado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        try {
            List<Medico> medicos = medicoDAO.listarTodos();
            modeloTabela.setRowCount(0);
            for (Medico m : medicos) {
                modeloTabela.addRow(new Object[]{m.getId(), m.getNome(), m.getCrm(), m.getEspecialidade()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Medico> listarMedicosAtual() throws Exception {
        return medicoDAO.listarTodos();
    }
}
