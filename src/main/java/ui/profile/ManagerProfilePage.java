package ui.profile;

import javax.swing.*;

import dto.AddressDTO;
import dto.ManagerDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.manager.ManagerService;

import java.awt.*;

public class ManagerProfilePage extends JFrame {
    private JLabel idLabel;
    private JTextField nameField, phoneField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, editAddressButton;

    public ManagerProfilePage(ManagerDTO manager) {
        setTitle("Manager Profile");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(7, 2, 5, 5));

        add(createCenteredLabel("ID:"));
        idLabel = new JLabel(manager.getId());
        getContentPane().add(idLabel);

        add(createCenteredLabel("Name:"));
        nameField = new JTextField(manager.getName());
        getContentPane().add(nameField);

        add(createCenteredLabel("Phone Number:"));
        phoneField = new JTextField(manager.getPhoneNumber());
        getContentPane().add(phoneField);

        add(createCenteredLabel("Email Address:"));
        emailField = new JTextField(manager.getEmailAddress());
        getContentPane().add(emailField);

        add(createCenteredLabel("Password:"));
        passwordField = new JPasswordField(manager.getPassword());
        getContentPane().add(passwordField);

        add(createCenteredLabel("Status:"));
        statusCheckBox = new JCheckBox("Active", manager.getStatus());
        getContentPane().add(statusCheckBox);

        editAddressButton = new JButton("Edit Address");
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> updateManager(manager));
        getContentPane().add(saveButton);
        getContentPane().add(editAddressButton);

        editAddressButton.addActionListener(e -> openAddressDialog(manager));
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void openAddressDialog(ManagerDTO manager) {
        AddressDTO address = AddressService.getAddressById(manager.getAddressId());

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

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateManager(ManagerDTO manager) {
        // Check if the fields are empty
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (nameField.getText().equals(manager.getName()) && phoneField.getText().equals(manager.getPhoneNumber()) && emailField.getText().equals(manager.getEmailAddress()) && passwordField.getText().equals(manager.getPassword()) && statusCheckBox.isSelected() == manager.getStatus()) {
            JOptionPane.showMessageDialog(this, "No changes were made", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ManagerDTO updatedManager = new ManagerDTO();
        updatedManager.setId(manager.getId());
        updatedManager.setName(nameField.getText());
        updatedManager.setPhoneNumber(phoneField.getText());
        updatedManager.setEmailAddress(emailField.getText());
        updatedManager.setPassword(passwordField.getText());
        updatedManager.setStatus(statusCheckBox.isSelected());
        updatedManager.setAddressId(manager.getAddressId());

        ResponseCode response = ManagerService.updateManager(updatedManager);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Manager updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update manager", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}