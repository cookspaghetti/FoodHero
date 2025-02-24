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

import dto.VendorDTO;
import dto.AddressDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.distance.DistanceService;
import service.vendor.VendorService;

public class VendorDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, emailField, vendorNameField, vendorTypeField;
    private JPasswordField passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton, editAddressButton;

    public VendorDetailsForm(VendorDTO vendor) {
        setTitle("Vendor Information");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(10, 2, 5, 5));

        JLabel label = new JLabel("ID:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label);
        idField = new JTextField(vendor.getId());
        idField.setEditable(false);
        getContentPane().add(idField);

        JLabel label_1 = new JLabel("Name:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_1);
        nameField = new JTextField(vendor.getName());
        getContentPane().add(nameField);

        JLabel label_2 = new JLabel("Phone Number:");
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_2);
        phoneField = new JTextField(vendor.getPhoneNumber());
        getContentPane().add(phoneField);

        JLabel label_3 = new JLabel("Address:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_3);
        editAddressButton = new JButton("Edit Address");
        getContentPane().add(editAddressButton);

        JLabel label_4 = new JLabel("Email Address:");
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_4);
        emailField = new JTextField(vendor.getEmailAddress());
        getContentPane().add(emailField);

        JLabel label_5 = new JLabel("Password:");
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_5);
        passwordField = new JPasswordField(vendor.getPassword());
        getContentPane().add(passwordField);

        JLabel label_6 = new JLabel("Status:");
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_6);
        statusCheckBox = new JCheckBox("Active", vendor.getStatus());
        getContentPane().add(statusCheckBox);

        JLabel label_7 = new JLabel("Vendor Name");
        label_7.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_7);
        vendorNameField = new JTextField(vendor.getVendorName());
        getContentPane().add(vendorNameField);

        JLabel label_8 = new JLabel("Vendor Type");
        label_8.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label_8);
        vendorTypeField = new JTextField(vendor.getVendorType().toString());
        getContentPane().add(vendorTypeField);

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
        add(saveButton);
        add(closeButton);

        closeButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> updateVendor(vendor));
        editAddressButton.addActionListener(e -> openAddressDialog(vendor));

        setLocationRelativeTo(null);
        setVisible(true);
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
