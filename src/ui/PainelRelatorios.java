package ui;

import dao.RelatorioDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PainelRelatorios extends JPanel {

    private final RelatorioDAO relatorioDAO = new RelatorioDAO();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JTextField campoInicio = new JTextField(10);
    private final JTextField campoFim = new JTextField(10);
    private final JLabel labelFaturamento = new JLabel("R$ 0,00");
    private final JTextArea areaPorMedico = new JTextArea(8, 30);
    private final JTextArea areaPorConvenio = new JTextArea(8, 30);

    public PainelRelatorios() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        LocalDate hoje = LocalDate.now();
        campoInicio.setText(hoje.withDayOfMonth(1).format(FMT));
        campoFim.setText(hoje.format(FMT));

        topo.add(new JLabel("Faturamento de:"));
        topo.add(campoInicio);
        topo.add(new JLabel("até"));
        topo.add(campoFim);
        JButton botaoGerar = new JButton("Gerar relatórios");
        botaoGerar.addActionListener(e -> gerarRelatorios());
        topo.add(botaoGerar);
        topo.add(new JLabel("   Total faturado:"));
        labelFaturamento.setFont(labelFaturamento.getFont().deriveFont(Font.BOLD, 16f));
        topo.add(labelFaturamento);

        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 10));
        areaPorMedico.setEditable(false);
        areaPorConvenio.setEditable(false);

        JPanel painelMedico = new JPanel(new BorderLayout());
        painelMedico.setBorder(BorderFactory.createTitledBorder("Consultas por médico"));
        painelMedico.add(new JScrollPane(areaPorMedico), BorderLayout.CENTER);

        JPanel painelConvenio = new JPanel(new BorderLayout());
        painelConvenio.setBorder(BorderFactory.createTitledBorder("Consultas por convênio"));
        painelConvenio.add(new JScrollPane(areaPorConvenio), BorderLayout.CENTER);

        centro.add(painelMedico);
        centro.add(painelConvenio);

        add(topo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        gerarRelatorios();
    }

    private void gerarRelatorios() {
        try {
            LocalDate inicio = LocalDate.parse(campoInicio.getText().trim(), FMT);
            LocalDate fim = LocalDate.parse(campoFim.getText().trim(), FMT);

            BigDecimal total = relatorioDAO.faturamentoNoPeriodo(inicio, fim);
            labelFaturamento.setText("R$ " + total);

            Map<String, Long> porMedico = relatorioDAO.consultasPorMedico();
            StringBuilder sbMedico = new StringBuilder();
            porMedico.forEach((nome, qtd) -> sbMedico.append(String.format("%-30s %d%n", nome, qtd)));
            areaPorMedico.setText(sbMedico.length() > 0 ? sbMedico.toString() : "Nenhuma consulta registrada ainda.");

            Map<String, Long> porConvenio = relatorioDAO.consultasPorConvenio();
            StringBuilder sbConvenio = new StringBuilder();
            porConvenio.forEach((nome, qtd) -> sbConvenio.append(String.format("%-30s %d%n", nome, qtd)));
            areaPorConvenio.setText(sbConvenio.length() > 0 ? sbConvenio.toString() : "Nenhuma consulta registrada ainda.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
