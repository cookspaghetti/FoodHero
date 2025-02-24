package ui.profile;

import javax.swing.*;

import dto.AddressDTO;
import dto.AdminDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.admin.AdminService;
import service.distance.DistanceService;

import java.awt.*;

public class AdminProfilePage extends JFrame {
    private JLabel idLabel;
    private JTextField nameField, phoneField, emailField;
    private JPasswordField passwordField;
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

        JTextField streetField = new JTextField(address.getStreet());
        JTextField cityField = new JTextField(address.getCity());
        JTextField stateField = new JTextField(address.getState());
        JTextField postalCodeField = new JTextField(address.getPostalCode());
        JTextField countryField = new JTextField(address.getCountry());

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
            // validation
            if (streetField.getText().isEmpty() || cityField.getText().isEmpty() || stateField.getText().isEmpty()
                    || postalCodeField.getText().isEmpty() || countryField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Update address object
            address.setStreet(streetField.getText());
            address.setCity(cityField.getText());
            address.setState(stateField.getText());
            address.setPostalCode(postalCodeField.getText());
            address.setCountry(countryField.getText());

            // verification using api
            if (!DistanceService.verifyAddress(address)) {
                JOptionPane.showMessageDialog(this, "Invalid address", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ResponseCode response = AddressService.updateAddress(address);
            if (response == ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Address updated successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
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
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
                || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (nameField.getText().equals(admin.getName()) && phoneField.getText().equals(admin.getPhoneNumber())
                && emailField.getText().equals(admin.getEmailAddress())
                && statusCheckBox.isSelected() == admin.getStatus()
                && passwordField.getPassword().equals(admin.getPassword().toCharArray())) {
            JOptionPane.showMessageDialog(this, "No changes made", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // check phone number
        if (!phoneField.getText().matches("\\d{10}") && !phoneField.getText().matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // check email
        if (!emailField.getText().matches("^(.+)@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // password length
        if (passwordField.getPassword().length < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        admin.setName(nameField.getText());
        admin.setPhoneNumber(phoneField.getText());
        admin.setEmailAddress(emailField.getText());
        admin.setPassword(String.valueOf(passwordField.getPassword()));
        admin.setStatus(statusCheckBox.isSelected());

        // Update admin object
        ResponseCode response = AdminService.updateAdmin(admin);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Admin updated successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update admin", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}