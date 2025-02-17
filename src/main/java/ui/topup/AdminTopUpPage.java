package ui.topup;

import javax.swing.*;

import ui.dialog.TopUpPaymentDialog;

import java.awt.*;

public class AdminTopUpPage extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JLabel idLabel, nameLabel, phoneLabel, addressLabel, emailLabel, statusLabel;
    private JButton topUpButton, closeButton;

    public AdminTopUpPage() {
        setTitle("Add Balance for Customer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search Customer:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        getContentPane().add(searchPanel, BorderLayout.NORTH);

        // Customer Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        idLabel = new JLabel("");
        nameLabel = new JLabel("");
        phoneLabel = new JLabel("");
        addressLabel = new JLabel("");
        emailLabel = new JLabel("");
        statusLabel = new JLabel("");
        JLabel label = new JLabel("ID:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(label);
        detailsPanel.add(idLabel);
        JLabel label_1 = new JLabel("Name:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(label_1);
        detailsPanel.add(nameLabel);
        JLabel label_2 = new JLabel("Phone Number:");
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(label_2);
        detailsPanel.add(phoneLabel);
        JLabel label_3 = new JLabel("Address ID:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(label_3);
        detailsPanel.add(addressLabel);
        JLabel label_4 = new JLabel("Email Address:");
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(label_4);
        detailsPanel.add(emailLabel);
        JLabel label_5 = new JLabel("Status:");
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(label_5);
        detailsPanel.add(statusLabel);
        getContentPane().add(detailsPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        topUpButton = new JButton("Top Up");
        closeButton = new JButton("Close");
        buttonPanel.add(topUpButton);
        buttonPanel.add(closeButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dispose());
        topUpButton.addActionListener(e -> new TopUpPaymentDialog(this, nameLabel.getText(), phoneLabel.getText()));

        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminTopUpPage();
    }
}

