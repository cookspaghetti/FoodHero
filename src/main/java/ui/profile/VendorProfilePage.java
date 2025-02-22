package ui.profile;

import javax.swing.*;

import dto.AddressDTO;
import dto.VendorDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.vendor.VendorService;

import java.awt.*;

public class VendorProfilePage extends JFrame {
    private JLabel idLabel;
    private JTextField nameField, vendorNameField, phoneField, emailField, passwordField;
    private JCheckBox statusCheckBox, openCheckBox;
    private JButton saveButton, editAddressButton;

    public VendorProfilePage(VendorDTO vendor) {
        setTitle("Vendor Profile");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(9, 2, 5, 5));

        add(createCenteredLabel("ID:"));
        idLabel = new JLabel(vendor.getId());
        getContentPane().add(idLabel);

        add(createCenteredLabel("Name:"));
        nameField = new JTextField(vendor.getName());
        getContentPane().add(nameField);
        
        add(createCenteredLabel("Vendor Name:"));
        vendorNameField = new JTextField(vendor.getVendorName());
        getContentPane().add(vendorNameField);

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

    private void updateVendor(VendorDTO vendor) {
        // Check if the fields are empty
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (nameField.getText().equals(vendor.getName()) && phoneField.getText().equals(vendor.getPhoneNumber()) && emailField.getText().equals(vendor.getEmailAddress()) && passwordField.getText().equals(vendor.getPassword()) && statusCheckBox.isSelected() == vendor.getStatus()) {
            JOptionPane.showMessageDialog(this, "No changes made", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        VendorDTO updatedVendor = new VendorDTO();
        updatedVendor.setId(vendor.getId());
        updatedVendor.setName(nameField.getText());
        updatedVendor.setPhoneNumber(phoneField.getText());
        updatedVendor.setEmailAddress(emailField.getText());
        updatedVendor.setPassword(passwordField.getText());
        updatedVendor.setStatus(statusCheckBox.isSelected());
        updatedVendor.setVendorName(vendorNameField.getText());
        updatedVendor.setVendorType(vendor.getVendorType());
        updatedVendor.setAddressId(vendor.getAddressId());

        ResponseCode response = VendorService.updateVendor(updatedVendor);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Vendor updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update vendor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}