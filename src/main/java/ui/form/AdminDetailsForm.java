package ui.form;

import javax.swing.*;

import dto.AddressDTO;
import dto.AdminDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.admin.AdminService;

import java.awt.*;

public class AdminDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, addressField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton, editAddressButton;

    public AdminDetailsForm(AdminDTO admin) {
        setTitle("Admin Information");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
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

        JLabel label_3 = new JLabel("Address:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_3);
        editAddressButton = new JButton("Edit Address");
        getContentPane().add(editAddressButton);

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

        saveButton.addActionListener(e -> {
            updateAdmin(admin);

        });

        closeButton.addActionListener(e -> dispose());

        editAddressButton.addActionListener(e -> {
            openAddressDialog(admin);
        });
    }

    // Open dialog to edit address
    private void openAddressDialog(AdminDTO admin) {
        // Get address object from admin object
        AddressDTO address = AddressService.getAddressById(admin.getAddressId());
        
        JDialog dialog = new JDialog(this, "Edit Address", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField countryField = new JTextField();

        streetField.setText(address.getStreet());
        cityField.setText(address.getCity());
        stateField.setText(address.getState());
        postalCodeField.setText(address.getPostalCode());
        countryField.setText(address.getCountry());

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

        JButton saveButton = new JButton("Save");
        JButton closeButton = new JButton("Close");
        dialog.add(saveButton);
        dialog.add(closeButton);

        saveButton.addActionListener(e -> {
            // Update address object
            
            dialog.dispose();
        });

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Update admin object with new values
    private void updateAdmin(AdminDTO admin) {
        // Check if the fields are empty
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (idField.getText().equals(admin.getId()) && nameField.getText().equals(admin.getName()) && phoneField.getText().equals(admin.getPhoneNumber()) && addressField.getText().equals(admin.getAddressId()) && emailField.getText().equals(admin.getEmailAddress()) && passwordField.getText().equals(admin.getPassword()) && statusCheckBox.isSelected() == admin.getStatus()) {
            JOptionPane.showMessageDialog(this, "No changes made", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(this, "Admin updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        AdminDTO updatedAdmin = new AdminDTO();
        updatedAdmin.setId(idField.getText());
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
