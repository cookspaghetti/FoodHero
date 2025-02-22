package ui.profile;

import javax.swing.*;

import dto.AddressDTO;
import dto.RunnerDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.runner.RunnerService;

import java.awt.*;

public class RunnerProfilePage extends JFrame {
    private JLabel idLabel;
    private JTextField nameField, numberPlateField, phoneField, emailField, passwordField;
    private JCheckBox statusCheckBox, availableCheckBox;
    private JButton saveButton, editAddressButton;

    public RunnerProfilePage(RunnerDTO runner) {
        setTitle("Runner Profile");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(9, 2, 5, 5));

        add(createCenteredLabel("ID:"));
        idLabel = new JLabel(runner.getId());
        getContentPane().add(idLabel);

        add(createCenteredLabel("Name:"));
        nameField = new JTextField(runner.getName());
        getContentPane().add(nameField);
        
        add(createCenteredLabel("Number Plate:"));
        numberPlateField = new JTextField(runner.getPlateNumber());
        getContentPane().add(numberPlateField);

        add(createCenteredLabel("Phone Number:"));
        phoneField = new JTextField(runner.getPhoneNumber());
        getContentPane().add(phoneField);

        add(createCenteredLabel("Email Address:"));
        emailField = new JTextField(runner.getEmailAddress());
        getContentPane().add(emailField);

        add(createCenteredLabel("Password:"));
        passwordField = new JPasswordField(runner.getPassword());
        getContentPane().add(passwordField);

        add(createCenteredLabel("Status:"));
        statusCheckBox = new JCheckBox("Active", runner.getStatus());
        getContentPane().add(statusCheckBox);
        
        add(createCenteredLabel("Available:"));
        availableCheckBox = new JCheckBox("Available to Tasks", runner.isAvailable());
        getContentPane().add(availableCheckBox);

        editAddressButton = new JButton("Edit Address");
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> updateRunner(runner));
        getContentPane().add(saveButton);
        getContentPane().add(editAddressButton);

        editAddressButton.addActionListener(e -> openAddressDialog(runner));
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void openAddressDialog(RunnerDTO runner) {
        AddressDTO address = AddressService.getAddressById(runner.getAddressId());

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
                JOptionPane.showMessageDialog(this, "Address updated successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update address", "Error", JOptionPane.ERROR_MESSAGE);
            }

            dialog.dispose();
        });

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateRunner(RunnerDTO runner) {
        // Check if the fields are empty
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty()
                || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }

        // Check if any fields is modified
        if (nameField.getText().equals(runner.getName())
                && phoneField.getText().equals(runner.getPhoneNumber())
                && emailField.getText().equals(runner.getEmailAddress())
                && passwordField.getText().equals(runner.getPassword())
                && statusCheckBox.isSelected() == runner.getStatus()) {
            JOptionPane.showMessageDialog(this, "No changes were made", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RunnerDTO updatedRunner = new RunnerDTO();
        updatedRunner.setId(runner.getId());
        updatedRunner.setName(nameField.getText());
        updatedRunner.setPhoneNumber(phoneField.getText());
        updatedRunner.setEmailAddress(emailField.getText());
        updatedRunner.setPassword(passwordField.getText());
        updatedRunner.setStatus(statusCheckBox.isSelected());
        updatedRunner.setAddressId(runner.getAddressId());

        ResponseCode response = RunnerService.updateRunner(updatedRunner);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Runner updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update runner", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}