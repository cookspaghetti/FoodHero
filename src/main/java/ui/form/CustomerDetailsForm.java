package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dto.CustomerDTO;

public class CustomerDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, addressField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton;

    public CustomerDetailsForm(CustomerDTO customer) {
        setTitle("Customer Information");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("ID:"));
        idField = new JTextField(customer.getId());
        add(idField);

        add(new JLabel("Name:"));
        nameField = new JTextField(customer.getName());
        add(nameField);

        add(new JLabel("Phone Number:"));
        phoneField = new JTextField(customer.getPhoneNumber());
        add(phoneField);

        add(new JLabel("Address ID:"));
        addressField = new JTextField(customer.getAddressId());
        add(addressField);

        add(new JLabel("Email Address:"));
        emailField = new JTextField(customer.getEmailAddress());
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField(customer.getPassword());
        add(passwordField);

        add(new JLabel("Status:"));
        statusCheckBox = new JCheckBox("Active", customer.getStatus());
        add(statusCheckBox);

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
        add(saveButton);
        add(closeButton);

        closeButton.addActionListener(e -> dispose());
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
