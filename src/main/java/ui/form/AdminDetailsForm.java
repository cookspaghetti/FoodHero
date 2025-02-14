package ui.form;

import javax.swing.*;

import dto.AdminDTO;

import java.awt.*;

public class AdminDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, addressField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton;

    public AdminDetailsForm(AdminDTO admin) {
        setTitle("Admin Information");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        JLabel label = new JLabel("ID:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label);
        idField = new JTextField(admin.getId());
        getContentPane().add(idField);

        JLabel label_1 = new JLabel("Name:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_1);
        nameField = new JTextField(admin.getName());
        getContentPane().add(nameField);

        JLabel label_2 = new JLabel("Phone Number:");
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_2);
        phoneField = new JTextField(admin.getPhoneNumber());
        getContentPane().add(phoneField);

        JLabel label_3 = new JLabel("Address ID:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_3);
        addressField = new JTextField(admin.getAddressId());
        getContentPane().add(addressField);

        JLabel label_4 = new JLabel("Email Address:");
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_4);
        emailField = new JTextField(admin.getEmailAddress());
        getContentPane().add(emailField);

        JLabel label_5 = new JLabel("Password:");
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_5);
        passwordField = new JPasswordField(admin.getPassword());
        getContentPane().add(passwordField);

        JLabel label_6 = new JLabel("Status:");
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_6);
        statusCheckBox = new JCheckBox("Active", admin.getStatus());
        getContentPane().add(statusCheckBox);

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
        getContentPane().add(saveButton);
        getContentPane().add(closeButton);

        closeButton.addActionListener(e -> dispose());
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        AdminDTO admin = new AdminDTO(); // Replace with actual admin object
        new AdminDetailsForm(admin);
    }
}

