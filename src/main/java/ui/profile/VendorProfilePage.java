package ui.profile;

import javax.swing.*;

import dto.VendorDTO;

import java.awt.*;

public class VendorProfilePage extends JFrame {
    private JLabel idLabel, nameLabel, phoneLabel, addressLabel, emailLabel, passwordLabel, statusLabel;
    private JTextField nameField, vendorNameField, phoneField, addressField, emailField, passwordField;
    private JCheckBox statusCheckBox, openCheckBox;
    private JButton saveButton, editAddressButton;

    public VendorProfilePage(VendorDTO vendor) {
        setTitle("Vendor Profile");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(9, 2, 5, 5));

        add(createCenteredLabel("ID:"));
        idLabel = new JLabel(vendor.getId());
        getContentPane().add(idLabel);

        add(createCenteredLabel("Name:"));
        nameField = new JTextField(vendor.getName());
        getContentPane().add(nameField);
        
        add(createCenteredLabel("Vendor Name:"));
        vendorNameField = new JTextField(vendor.getVendorName());
        getContentPane().add(vendorNameField);

        add(createCenteredLabel("Phone Number:"));
        phoneField = new JTextField(vendor.getPhoneNumber());
        getContentPane().add(phoneField);

        add(createCenteredLabel("Email Address:"));
        emailField = new JTextField(vendor.getEmailAddress());
        getContentPane().add(emailField);

        add(createCenteredLabel("Password:"));
        passwordField = new JPasswordField(vendor.getPassword());
        getContentPane().add(passwordField);

        add(createCenteredLabel("Status:"));
        statusCheckBox = new JCheckBox("Active", vendor.getStatus());
        getContentPane().add(statusCheckBox);
        
        add(createCenteredLabel("Open:"));
        openCheckBox = new JCheckBox("Open for Business", vendor.getOpen());
        getContentPane().add(openCheckBox);

        editAddressButton = new JButton("Edit Address");
        saveButton = new JButton("Save");
        getContentPane().add(saveButton);
        getContentPane().add(editAddressButton);

        editAddressButton.addActionListener(e -> openAddressDialog());
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void openAddressDialog() {
        JDialog dialog = new JDialog(this, "Edit Address", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField countryField = new JTextField();

        dialog.add(new JLabel("Street:"));
        dialog.add(streetField);
        dialog.add(new JLabel("City:"));
        dialog.add(cityField);
        dialog.add(new JLabel("State:"));
        dialog.add(stateField);
        dialog.add(new JLabel("Postal Code:"));
        dialog.add(postalCodeField);
        dialog.add(new JLabel("Country:"));
        dialog.add(countryField);

        JButton saveAddressButton = new JButton("Save Address");
        saveAddressButton.addActionListener(e -> {
            
            dialog.dispose();
        });
        dialog.add(saveAddressButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
}