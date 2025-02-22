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
import enumeration.VendorType;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.vendor.VendorService;

public class VendorDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, emailField, passwordField, vendorNameField, vendorTypeField;
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
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (idField.getText().equals(vendor.getId()) && nameField.getText().equals(vendor.getName()) && phoneField.getText().equals(vendor.getPhoneNumber()) && emailField.getText().equals(vendor.getEmailAddress()) && passwordField.getText().equals(vendor.getPassword()) && statusCheckBox.isSelected() == vendor.getStatus()) {
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

    public static void main(String[] args) {
        VendorDTO vendor = new VendorDTO(); // Replace with actual Vendor object
        vendor.setStatus(true);
        vendor.setVendorType(VendorType.CHINESE);
        new VendorDetailsForm(vendor);
    }
}
