package pl.prv.techalive.ui;

import pl.prv.techalive.core.Encryptable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static java.awt.event.ItemEvent.SELECTED;

public class MainFrame extends JFrame implements ItemListener {

    private JEncryptTab encryptTab = new JEncryptTab();
    private JDecryptTab decryptTab = new JDecryptTab();

    private java.util.List<EncryptableListener> encryptableSubscription = new ArrayList<>();

    public MainFrame() {
        setContentPane(new JPanel());
        setTitle("Algorytmy");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Add algorithm list Combo-Box to main pane
        JAlgorithmList algorithmList = new JAlgorithmList(this);
        getContentPane().add(algorithmList);

        // Create tabs
        createTabbedPane();

        // Register both tabs to listen for algorithm change in drop-down
        registerEncryptableListeners();

        // Center GUI
        setLocationRelativeTo(null);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(550, 330));
        pack();
    }

    private void registerEncryptableListeners() {
        encryptableSubscription.add(encryptTab);
        encryptableSubscription.add(decryptTab);
    }

    private void createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Szyfrowanie", null, encryptTab.create(), "Szyfrowanie");
        tabbedPane.addTab("Deszyfrowanie", null, decryptTab.create(), "Deszyfrowanie");
        getContentPane().add(tabbedPane);
    }

    public void display() {
        setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (SELECTED == e.getStateChange()) {
            Object item = e.getItem();
            if (item instanceof Encryptable) {
                // inform tabs for algorithm change
                encryptableSubscription.forEach(o -> o.onEncryptableChange((Encryptable)item));
            }
        }
    }
}
