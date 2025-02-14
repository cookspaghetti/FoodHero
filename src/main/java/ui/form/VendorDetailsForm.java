package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dto.VendorDTO;

public class VendorDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, addressField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton;

    public VendorDetailsForm(VendorDTO vendor) {
        setTitle("Vendor Information");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("ID:"));
        idField = new JTextField(vendor.getId());
        add(idField);

        add(new JLabel("Name:"));
        nameField = new JTextField(vendor.getName());
        add(nameField);

        add(new JLabel("Phone Number:"));
        phoneField = new JTextField(vendor.getPhoneNumber());
        add(phoneField);

        add(new JLabel("Address ID:"));
        addressField = new JTextField(vendor.getAddressId());
        add(addressField);

        add(new JLabel("Email Address:"));
        emailField = new JTextField(vendor.getEmailAddress());
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField(vendor.getPassword());
        add(passwordField);

        add(new JLabel("Status:"));
        statusCheckBox = new JCheckBox("Active", vendor.getStatus());
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
        VendorDTO vendor = new VendorDTO(); // Replace with actual Vendor object
        new VendorDetailsForm(vendor);
    }
}
