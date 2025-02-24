package ui.profile;

import javax.swing.*;

import dto.AddressDTO;
import dto.RunnerDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.distance.DistanceService;
import service.runner.RunnerService;

import java.awt.*;

public class RunnerProfilePage extends JFrame {
    private JLabel idLabel;
    private JTextField nameField, numberPlateField, phoneField, emailField;
    private JPasswordField passwordField;
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
        JTextField countryField = new JTextField("Malaysia");

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

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateRunner(RunnerDTO runner) {
        // Check if the fields are empty
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
                || passwordField.getPassword().length == 0 || numberPlateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (nameField.getText().equals(runner.getName()) && phoneField.getText().equals(runner.getPhoneNumber())
                && emailField.getText().equals(runner.getEmailAddress())
                && statusCheckBox.isSelected() == runner.getStatus()
                && availableCheckBox.isSelected() == runner.isAvailable()
                && numberPlateField.getText().equals(runner.getPlateNumber())
                && passwordField.getPassword().equals(runner.getPassword().toCharArray())) {
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

        runner.setName(nameField.getText());
        runner.setPhoneNumber(phoneField.getText());
        runner.setEmailAddress(emailField.getText());
        runner.setPassword(String.valueOf(passwordField.getPassword()));
        runner.setStatus(statusCheckBox.isSelected());
        runner.setPlateNumber(numberPlateField.getText());
        runner.setAvailable(availableCheckBox.isSelected());

        ResponseCode response = RunnerService.updateRunner(runner);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Runner updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update runner", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}