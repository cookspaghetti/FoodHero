package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JDialog;

import dto.RunnerDTO;
import dto.AddressDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.distance.DistanceService;
import service.runner.RunnerService;

public class RunnerDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, emailField, numberPlateField;
    private JPasswordField passwordField;
    private JCheckBox availableCheckBox;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton, editAddressButton;

    public RunnerDetailsForm(RunnerDTO runner) {
        setTitle("Runner Information");
        setSize(400, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(10, 2, 5, 5));

        JLabel label = new JLabel("ID:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
        idField = new JTextField(runner.getId());
        idField.setEditable(false);
        add(idField);

        JLabel label_1 = new JLabel("Name:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_1);
        nameField = new JTextField(runner.getName());
        add(nameField);

        JLabel label_2 = new JLabel("Phone Number:");
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_2);
        phoneField = new JTextField(runner.getPhoneNumber());
        add(phoneField);

        JLabel label_3 = new JLabel("Address:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_3);
        editAddressButton = new JButton("Edit Address");
        add(editAddressButton);

        JLabel label_4 = new JLabel("Email Address:");
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_4);
        emailField = new JTextField(runner.getEmailAddress());
        add(emailField);

        JLabel label_5 = new JLabel("Password:");
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_5);
        passwordField = new JPasswordField(runner.getPassword());
        add(passwordField);

        JLabel label_7 = new JLabel("Number Plate:");
        label_7.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_7);
        numberPlateField = new JTextField(runner.getPlateNumber());
        add(numberPlateField);

        JLabel label_6 = new JLabel("Status:");
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_6);
        statusCheckBox = new JCheckBox("Active", runner.getStatus());
        add(statusCheckBox);

        JLabel label_8 = new JLabel("Available:");
        label_8.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_8);
        availableCheckBox = new JCheckBox("Available to Tasks", runner.isAvailable());
        add(availableCheckBox);

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
        add(saveButton);
        add(closeButton);

        saveButton.addActionListener(e -> updateRunner(runner));
        editAddressButton.addActionListener(e -> openAddressDialog(runner));
        closeButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
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
