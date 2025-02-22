package ui.profile;

import javax.swing.*;

import dto.AddressDTO;
import dto.AdminDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.admin.AdminService;

import java.awt.*;

public class AdminProfilePage extends JFrame {
    private JLabel idLabel;
    private JTextField nameField, phoneField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, editAddressButton;

    public AdminProfilePage(AdminDTO admin) {
        setTitle("Admin Profile");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(7, 2, 5, 5));

        add(createCenteredLabel("ID:"));
        idLabel = new JLabel(admin.getId());
        getContentPane().add(idLabel);

        add(createCenteredLabel("Name:"));
        nameField = new JTextField(admin.getName());
        getContentPane().add(nameField);

        add(createCenteredLabel("Phone Number:"));
        phoneField = new JTextField(admin.getPhoneNumber());
        getContentPane().add(phoneField);

        add(createCenteredLabel("Email Address:"));
        emailField = new JTextField(admin.getEmailAddress());
        getContentPane().add(emailField);

        add(createCenteredLabel("Password:"));
        passwordField = new JPasswordField(admin.getPassword());
        getContentPane().add(passwordField);

        add(createCenteredLabel("Status:"));
        statusCheckBox = new JCheckBox("Active", admin.getStatus());
        getContentPane().add(statusCheckBox);

        editAddressButton = new JButton("Edit Address");
        saveButton = new JButton("Save");
        getContentPane().add(saveButton);
        getContentPane().add(editAddressButton);

        saveButton.addActionListener(e -> updateAdmin(admin));
        editAddressButton.addActionListener(e -> openAddressDialog(admin));
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void openAddressDialog(AdminDTO admin) {
        AddressDTO address = AddressService.getAddressById(admin.getAddressId());
        
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

        JButton saveButton = new JButton("Save Address");
        saveButton.addActionListener(e -> {
            // Update address object
            AddressDTO updatedAddress = new AddressDTO();
            updatedAddress.setId(address.getId());
            updatedAddress.setStreet(streetField.getText());
            updatedAddress.setCity(cityField.getText());
            updatedAddress.setState(stateField.getText());
            updatedAddress.setPostalCode(postalCodeField.getText());
            updatedAddress.setCountry(countryField.getText());

            ResponseCode response = AddressService.updateAddress(updatedAddress);
            if (response == ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Address updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update address", "Error", JOptionPane.ERROR_MESSAGE);
            }

            dialog.dispose();
        });
        dialog.add(saveButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Update admin object with new values
    private void updateAdmin(AdminDTO admin) {
        // Check if the fields are empty
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (nameField.getText().equals(admin.getName()) && phoneField.getText().equals(admin.getPhoneNumber()) && emailField.getText().equals(admin.getEmailAddress()) && passwordField.getText().equals(admin.getPassword()) && statusCheckBox.isSelected() == admin.getStatus()) {
            JOptionPane.showMessageDialog(this, "No changes made", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        AdminDTO updatedAdmin = new AdminDTO();
        updatedAdmin.setId(admin.getId());
        updatedAdmin.setName(nameField.getText());
        updatedAdmin.setPhoneNumber(phoneField.getText());
        updatedAdmin.setAddressId(admin.getAddressId());
        updatedAdmin.setEmailAddress(emailField.getText());
        updatedAdmin.setPassword(passwordField.getText());
        updatedAdmin.setStatus(statusCheckBox.isSelected());

        // Update admin object
        ResponseCode response = AdminService.updateAdmin(updatedAdmin);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Admin updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update admin", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}