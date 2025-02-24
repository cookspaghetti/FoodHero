package ui.profile;

import javax.swing.*;

import dto.AddressDTO;
import dto.VendorDTO;
import enumeration.ResponseCode;
import enumeration.VendorType;
import service.address.AddressService;
import service.distance.DistanceService;
import service.vendor.VendorService;

import java.awt.*;

public class VendorProfilePage extends JFrame {
    private JLabel idLabel;
    private JTextField nameField, vendorNameField, phoneField, emailField;
    private JPasswordField passwordField;
    private JCheckBox statusCheckBox, openCheckBox;
    private JButton saveButton, editAddressButton;
    private JComboBox<VendorType> vendorTypeComboBox;

    public VendorProfilePage(VendorDTO vendor) {
        setTitle("Vendor Profile");
        setSize(400, 440);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(10, 2, 5, 5));

        add(createCenteredLabel("ID:"));
        idLabel = new JLabel(vendor.getId());
        getContentPane().add(idLabel);

        add(createCenteredLabel("Name:"));
        nameField = new JTextField(vendor.getName());
        getContentPane().add(nameField);

        add(createCenteredLabel("Vendor Name:"));
        vendorNameField = new JTextField(vendor.getVendorName());
        getContentPane().add(vendorNameField);

        add(createCenteredLabel("Vendor Type:"));
        vendorTypeComboBox = new JComboBox<>(VendorType.values());
        vendorTypeComboBox.setSelectedItem(vendor.getVendorType());
        getContentPane().add(vendorTypeComboBox);

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

        editAddressButton.addActionListener(e -> openAddressDialog(vendor));
        saveButton.addActionListener(e -> updateVendor(vendor));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void openAddressDialog(VendorDTO vendor) {
        AddressDTO address = AddressService.getAddressById(vendor.getAddressId());

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

    private void updateVendor(VendorDTO vendor) {
        // Check if the fields are empty
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
                || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (nameField.getText().equals(vendor.getName()) && phoneField.getText().equals(vendor.getPhoneNumber())
                && emailField.getText().equals(vendor.getEmailAddress())
                && statusCheckBox.isSelected() == vendor.getStatus()
                && vendorNameField.getText().equals(vendor.getVendorName())
                && vendorTypeComboBox.getItemAt(vendorTypeComboBox.getSelectedIndex()).equals(vendor.getVendorType())
                && openCheckBox.isSelected() == vendor.getOpen()
                && passwordField.getPassword().equals(vendor.getPassword().toCharArray())) {
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

        vendor.setName(nameField.getText());
        vendor.setPhoneNumber(phoneField.getText());
        vendor.setEmailAddress(emailField.getText());
        vendor.setPassword(String.valueOf(passwordField.getPassword()));
        vendor.setStatus(statusCheckBox.isSelected());
        vendor.setVendorName(vendorNameField.getText());
        vendor.setVendorType(vendorTypeComboBox.getItemAt(vendorTypeComboBox.getSelectedIndex()));
        vendor.setOpen(openCheckBox.isSelected());

        ResponseCode response = VendorService.updateVendor(vendor);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Vendor updated successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update vendor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}