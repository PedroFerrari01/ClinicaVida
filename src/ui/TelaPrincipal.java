package ui;

import javax.swing.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        super("Clínica Vida - Sistema de Gestão");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();

        PainelPacientes painelPacientes = new PainelPacientes();
        PainelMedicos painelMedicos = new PainelMedicos();
        PainelConvenios painelConvenios = new PainelConvenios();
        PainelAgenda painelAgenda = new PainelAgenda();
        PainelRelatorios painelRelatorios = new PainelRelatorios();

        abas.addTab("Pacientes", painelPacientes);
        abas.addTab("Médicos", painelMedicos);
        abas.addTab("Convênios", painelConvenios);
        abas.addTab("Agenda", painelAgenda);
        abas.addTab("Relatórios", painelRelatorios);

        // Sempre que o usuário entra na aba Agenda, recarrega os combos
        // (para já aparecerem pacientes/médicos cadastrados nas outras abas).
        abas.addChangeListener(e -> {
            if (abas.getSelectedComponent() == painelAgenda) {
                painelAgenda.recarregarCombos();
            }
        });

        add(abas);
    }
}
