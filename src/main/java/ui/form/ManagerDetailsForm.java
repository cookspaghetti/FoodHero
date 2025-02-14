package ui.form;

import javax.swing.*;

import dto.ManagerDTO;

import java.awt.*;

public class ManagerDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, addressField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton;

    public ManagerDetailsForm(ManagerDTO manager) {
        setTitle("Manager Information");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("ID:"));
        idField = new JTextField(manager.getId());
        add(idField);

        add(new JLabel("Name:"));
        nameField = new JTextField(manager.getName());
        add(nameField);

        add(new JLabel("Phone Number:"));
        phoneField = new JTextField(manager.getPhoneNumber());
        add(phoneField);

        add(new JLabel("Address ID:"));
        addressField = new JTextField(manager.getAddressId());
        add(addressField);

        add(new JLabel("Email Address:"));
        emailField = new JTextField(manager.getEmailAddress());
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField(manager.getPassword());
        add(passwordField);

        add(new JLabel("Status:"));
        statusCheckBox = new JCheckBox("Active", manager.getStatus());
        add(statusCheckBox);

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
        add(saveButton);
        add(closeButton);

        closeButton.addActionListener(e -> dispose());
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        ManagerDTO manager = new ManagerDTO(); // Replace with actual manager object
        new ManagerDetailsForm(manager);
    }
}

