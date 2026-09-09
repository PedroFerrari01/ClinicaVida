package ui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelAgenda extends JPanel {

    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final ConvenioDAO convenioDAO = new ConvenioDAO();
    private final ProntuarioDAO prontuarioDAO = new ProntuarioDAO();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FMT_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final JComboBox<Paciente> comboPaciente = new JComboBox<>();
    private final JComboBox<Medico> comboMedico = new JComboBox<>();
    private final JTextField campoData = new JTextField(10);
    private final JTextField campoHora = new JTextField(6);
    private final JTextField campoValor = new JTextField(8);
    private final JTextField campoObs = new JTextField(20);

    private final JTextField campoFiltroDataInicio = new JTextField(10);
    private final JTextField campoFiltroDataFim = new JTextField(10);

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Data/Hora", "Paciente", "Médico", "Status", "Valor"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelAgenda() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(criarFormularioAgendamento(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarBarraAcoes(), BorderLayout.SOUTH);

        carregarCombos();

        LocalDate hoje = LocalDate.now();
        campoFiltroDataInicio.setText(hoje.format(FMT_DATA));
        campoFiltroDataFim.setText(hoje.plusDays(30).format(FMT_DATA));
        atualizarTabela();
    }

    private JPanel criarFormularioAgendamento() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Agendar consulta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        int linha = 0;
        gbc.gridx = 0; gbc.gridy = linha; painel.add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1; painel.add(comboPaciente, gbc);
        gbc.gridx = 2; painel.add(new JLabel("Médico:"), gbc);
        gbc.gridx = 3; painel.add(comboMedico, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha; painel.add(new JLabel("Data (dd/mm/aaaa):"), gbc);
        gbc.gridx = 1; painel.add(campoData, gbc);
        gbc.gridx = 2; painel.add(new JLabel("Hora (HH:mm):"), gbc);
        gbc.gridx = 3; painel.add(campoHora, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha; painel.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 1; painel.add(campoValor, gbc);
        gbc.gridx = 2; painel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 3; painel.add(campoObs, gbc);

        JButton botaoAgendar = new JButton("Agendar");
        botaoAgendar.addActionListener(e -> agendar());
        linha++;
        gbc.gridx = 3; gbc.gridy = linha;
        painel.add(botaoAgendar, gbc);

        comboPaciente.addActionListener(e -> preencherValorSugerido());
        comboMedico.addActionListener(e -> preencherValorSugerido());

        return painel;
    }

    private JPanel criarBarraAcoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.add(new JLabel("Período:"));
        painel.add(campoFiltroDataInicio);
        painel.add(new JLabel("até"));
        painel.add(campoFiltroDataFim);

        JButton botaoFiltrar = new JButton("Filtrar");
        botaoFiltrar.addActionListener(e -> atualizarTabela());
        painel.add(botaoFiltrar);

        JButton botaoRealizar = new JButton("Marcar como realizada + Prontuário/Pagamento");
        botaoRealizar.addActionListener(e -> realizarConsulta());
        painel.add(botaoRealizar);

        JButton botaoCancelar = new JButton("Cancelar consulta");
        botaoCancelar.addActionListener(e -> cancelarConsulta());
        painel.add(botaoCancelar);

        return painel;
    }

    private void carregarCombos() {
        try {
            comboPaciente.removeAllItems();
            for (Paciente p : pacienteDAO.listarTodos()) {
                comboPaciente.addItem(p);
            }
            comboMedico.removeAllItems();
            for (Medico m : medicoDAO.listarTodos()) {
                comboMedico.addItem(m);
            }
            preencherValorSugerido();
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void preencherValorSugerido() {
        try {
            Paciente paciente = (Paciente) comboPaciente.getSelectedItem();
            if (paciente != null && paciente.getConvenioId() != null) {
                Convenio convenio = convenioDAO.buscarPorId(paciente.getConvenioId());
                if (convenio != null) {
                    campoValor.setText(convenio.getValorConsulta().toPlainString());
                    return;
                }
            }
            campoValor.setText("250.00"); // valor particular padrão
        } catch (Exception ignored) {
            // campo de sugestão não é crítico; se falhar, deixa o usuário preencher manualmente
        }
    }

    private void agendar() {
        try {
            Paciente paciente = (Paciente) comboPaciente.getSelectedItem();
            Medico medico = (Medico) comboMedico.getSelectedItem();
            if (paciente == null || medico == null) {
                JOptionPane.showMessageDialog(this, "Cadastre ao menos um paciente e um médico antes de agendar.");
                return;
            }
            LocalDate data = LocalDate.parse(campoData.getText().trim(), FMT_DATA);
            LocalTime hora = LocalTime.parse(campoHora.getText().trim(), FMT_HORA);
            LocalDateTime dataHora = LocalDateTime.of(data, hora);
            BigDecimal valor = new BigDecimal(campoValor.getText().trim().replace(",", "."));

            Consulta consulta = new Consulta(paciente.getId(), medico.getId(), dataHora, valor, campoObs.getText().trim());
            consultaDAO.agendar(consulta);

            campoObs.setText("");
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Consulta agendada para " + dataHora.format(FMT_DATA_HORA) + "!");
        } catch (IllegalStateException ex) {
            // conflito de horário identificado pela regra de negócio no DAO
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Conflito de agenda", JOptionPane.WARNING_MESSAGE);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data ou hora inválida. Use dd/mm/aaaa e HH:mm.");
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void atualizarTabela() {
        try {
            LocalDate inicio = LocalDate.parse(campoFiltroDataInicio.getText().trim(), FMT_DATA);
            LocalDate fim = LocalDate.parse(campoFiltroDataFim.getText().trim(), FMT_DATA);
            List<Consulta> consultas = consultaDAO.listarAgenda(inicio.atStartOfDay(), fim.atTime(23, 59, 59));

            modeloTabela.setRowCount(0);
            for (Consulta c : consultas) {
                modeloTabela.addRow(new Object[]{
                        c.getId(), c.getDataHora().format(FMT_DATA_HORA),
                        c.getNomePaciente(), c.getNomeMedico(), c.getStatus(), "R$ " + c.getValor()
                });
            }
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private int obterConsultaSelecionadaId() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma consulta na tabela primeiro.");
            return -1;
        }
        return (int) modeloTabela.getValueAt(linha, 0);
    }

    private void realizarConsulta() {
        try {
            int consultaId = obterConsultaSelecionadaId();
            if (consultaId == -1) return;

            Consulta consulta = consultaDAO.buscarPorId(consultaId);
            if (consulta.getStatus() != StatusConsulta.AGENDADA) {
                JOptionPane.showMessageDialog(this, "Essa consulta não está mais AGENDADA.");
                return;
            }

            String diagnostico = JOptionPane.showInputDialog(this, "Diagnóstico:");
            if (diagnostico == null) return; // usuário cancelou
            String prescricao = JOptionPane.showInputDialog(this, "Prescrição:");
            if (prescricao == null) return;

            String[] formas = {"DINHEIRO", "CARTAO", "PIX", "CONVENIO"};
            String formaEscolhida = (String) JOptionPane.showInputDialog(this, "Forma de pagamento:",
                    "Pagamento", JOptionPane.QUESTION_MESSAGE, null, formas, formas[0]);
            if (formaEscolhida == null) return;

            consultaDAO.atualizarStatus(consultaId, StatusConsulta.REALIZADA);
            prontuarioDAO.salvar(new Prontuario(consultaId, diagnostico, prescricao, LocalDateTime.now()));
            pagamentoDAO.salvar(new Pagamento(consultaId, consulta.getValor(), FormaPagamento.valueOf(formaEscolhida), LocalDateTime.now()));

            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Consulta finalizada: prontuário e pagamento registrados!");
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void cancelarConsulta() {
        try {
            int consultaId = obterConsultaSelecionadaId();
            if (consultaId == -1) return;

            int confirmacao = JOptionPane.showConfirmDialog(this, "Cancelar esta consulta?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacao != JOptionPane.YES_OPTION) return;

            consultaDAO.atualizarStatus(consultaId, StatusConsulta.CANCELADA);
            atualizarTabela();
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void mostrarErro(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /** Chamado pela tela principal quando outra aba cadastra paciente/médico novo. */
    public void recarregarCombos() {
        carregarCombos();
    }
}
