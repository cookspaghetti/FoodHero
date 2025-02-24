package ui.form;

import javax.swing.*;

import dto.AddressDTO;
import dto.AdminDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.address.AddressService;
import service.utils.IdGenerationUtils;
import service.admin.AdminService;
import service.distance.DistanceService;

import java.awt.*;

public class AdminCreateUserForm extends JFrame {

    private JTextField idField, nameField, phoneField, emailField;
    private JPasswordField passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton, editAddressButton;

    private String newAddressId;

    public AdminCreateUserForm() {
        setTitle("Create Admin");
        setSize(400,405);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        JLabel label = new JLabel("ID:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label);
        idField = new JTextField(IdGenerationUtils.getNextId(ServiceType.ADMIN, null, null));
        idField.setEditable(false);
        getContentPane().add(idField);

        JLabel label_1 = new JLabel("Name:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_1);
        nameField = new JTextField();
        getContentPane().add(nameField);

        JLabel label_2 = new JLabel("Phone Number:");
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_2);
        phoneField = new JTextField();
        getContentPane().add(phoneField);

        JLabel label_3 = new JLabel("Address:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_3);
        editAddressButton = new JButton("Edit Address");
        getContentPane().add(editAddressButton);

        JLabel label_4 = new JLabel("Email Address:");
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_4);
        emailField = new JTextField();
        getContentPane().add(emailField);

        JLabel label_5 = new JLabel("Password:");
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_5);
        passwordField = new JPasswordField();
        getContentPane().add(passwordField);

        JLabel label_6 = new JLabel("Status:");
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_6);
        statusCheckBox = new JCheckBox("Active", true);
        getContentPane().add(statusCheckBox);

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
        getContentPane().add(saveButton);
        getContentPane().add(closeButton);

        saveButton.addActionListener(e -> createAdmin());
        closeButton.addActionListener(e -> dispose());
        editAddressButton.addActionListener(e -> openAddressDialog());

        setVisible(true);
    }

    // Open dialog to edit address
    private void openAddressDialog() {  
        JDialog dialog = new JDialog(this, "Edit Address", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField countryField = new JTextField("Malaysia");

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
            // New address object
            AddressDTO newAddress = new AddressDTO();
            newAddress.setId(IdGenerationUtils.getNextId(ServiceType.ADDRESS, null, null));
            newAddress.setUserId(IdGenerationUtils.getNextId(ServiceType.ADMIN, null, null));
            newAddress.setStreet(streetField.getText());
            newAddress.setCity(cityField.getText());
            newAddress.setState(stateField.getText());
            newAddress.setPostalCode(postalCodeField.getText());
            newAddress.setCountry(countryField.getText());

            // verification using api
            if (!DistanceService.verifyAddress(newAddress)) {
                JOptionPane.showMessageDialog(this, "Invalid address", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ResponseCode response = AddressService.createAddress(newAddress);
            if (response == ResponseCode.SUCCESS) {
                newAddressId = newAddress.getId();
                JOptionPane.showMessageDialog(this, "Address created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create address", "Error", JOptionPane.ERROR_MESSAGE);
            }

            dialog.dispose();
        });

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void createAdmin() {
        if (newAddressId == null) {
            JOptionPane.showMessageDialog(this, "Please create an address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the fields are empty
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
                || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
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

        AdminDTO admin = new AdminDTO();
        admin.setId(IdGenerationUtils.getNextId(ServiceType.ADMIN, null, null));
        admin.setName(nameField.getText());
        admin.setPhoneNumber(phoneField.getText());
        admin.setEmailAddress(emailField.getText());
        admin.setPassword(String.valueOf(passwordField.getPassword()));
        admin.setStatus(statusCheckBox.isSelected());
        admin.setAddressId(newAddressId);

        ResponseCode response = AdminService.createAdmin(admin);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Admin created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create admin", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
