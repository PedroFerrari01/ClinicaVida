package ui;

import dao.ConvenioDAO;
import dao.PacienteDAO;
import model.Convenio;
import model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelPacientes extends JPanel {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final ConvenioDAO convenioDAO = new ConvenioDAO();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoCpf = new JTextField(14);
    private final JTextField campoNascimento = new JTextField(10);
    private final JTextField campoTelefone = new JTextField(14);
    private final JTextField campoEmail = new JTextField(18);
    private final JComboBox<Convenio> comboConvenio = new JComboBox<>();
    private final JTextField campoBusca = new JTextField(15);

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Nome", "CPF", "Nascimento", "Telefone", "E-mail", "Convênio"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelPacientes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(criarFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarBarraBusca(), BorderLayout.SOUTH);

        carregarConvenios();
        atualizarTabela(null);
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Novo paciente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        int linha = 0;
        adicionarCampo(painel, gbc, linha++, "Nome:", campoNome);
        adicionarCampo(painel, gbc, linha++, "CPF:", campoCpf);
        adicionarCampo(painel, gbc, linha++, "Nascimento (dd/mm/aaaa):", campoNascimento);
        adicionarCampo(painel, gbc, linha++, "Telefone:", campoTelefone);
        adicionarCampo(painel, gbc, linha++, "E-mail:", campoEmail);
        adicionarCampo(painel, gbc, linha++, "Convênio:", comboConvenio);

        JButton botaoSalvar = new JButton("Cadastrar paciente");
        botaoSalvar.addActionListener(e -> salvarPaciente());
        gbc.gridx = 1;
        gbc.gridy = linha;
        painel.add(botaoSalvar, gbc);

        return painel;
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints gbc, int linha, String rotulo, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        painel.add(new JLabel(rotulo), gbc);
        gbc.gridx = 1;
        painel.add(campo, gbc);
    }

    private JPanel criarBarraBusca() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoBuscar = new JButton("Buscar por nome");
        JButton botaoLimpar = new JButton("Listar todos");
        botaoBuscar.addActionListener(e -> atualizarTabela(campoBusca.getText().trim()));
        botaoLimpar.addActionListener(e -> {
            campoBusca.setText("");
            atualizarTabela(null);
        });
        painel.add(new JLabel("Buscar:"));
        painel.add(campoBusca);
        painel.add(botaoBuscar);
        painel.add(botaoLimpar);
        return painel;
    }

    private void carregarConvenios() {
        try {
            comboConvenio.removeAllItems();
            comboConvenio.addItem(null); // representa "Particular / sem convênio"
            for (Convenio c : convenioDAO.listarTodos()) {
                comboConvenio.addItem(c);
            }
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void salvarPaciente() {
        try {
            if (campoNome.getText().isBlank() || campoCpf.getText().isBlank() || campoNascimento.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Nome, CPF e data de nascimento são obrigatórios.");
                return;
            }
            LocalDate nascimento = LocalDate.parse(campoNascimento.getText().trim(), FMT);
            Convenio convenioSelecionado = (Convenio) comboConvenio.getSelectedItem();
            Integer convenioId = convenioSelecionado != null ? convenioSelecionado.getId() : null;

            Paciente paciente = new Paciente(
                    campoNome.getText().trim(),
                    campoCpf.getText().trim(),
                    nascimento,
                    campoTelefone.getText().trim(),
                    campoEmail.getText().trim(),
                    convenioId
            );
            pacienteDAO.salvar(paciente);

            limparFormulario();
            atualizarTabela(null);
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Use o formato dd/mm/aaaa.");
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void limparFormulario() {
        campoNome.setText("");
        campoCpf.setText("");
        campoNascimento.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        comboConvenio.setSelectedIndex(0);
    }

    private void atualizarTabela(String termoBusca) {
        try {
            List<Paciente> pacientes = (termoBusca == null || termoBusca.isBlank())
                    ? pacienteDAO.listarTodos()
                    : pacienteDAO.buscarPorNome(termoBusca);

            modeloTabela.setRowCount(0);
            for (Paciente p : pacientes) {
                String nomeConvenio = "Particular";
                if (p.getConvenioId() != null) {
                    Convenio c = convenioDAO.buscarPorId(p.getConvenioId());
                    if (c != null) {
                        nomeConvenio = c.getNome();
                    }
                }
                modeloTabela.addRow(new Object[]{
                        p.getId(), p.getNome(), p.getCpf(),
                        p.getDataNascimento().format(FMT),
                        p.getTelefone(), p.getEmail(), nomeConvenio
                });
            }
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void mostrarErro(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
