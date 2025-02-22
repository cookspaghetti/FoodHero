package ui.form;

import javax.swing.*;

import dto.AddressDTO;
import dto.ManagerDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.address.AddressService;
import service.utils.IdGenerationUtils;
import service.manager.ManagerService;


import java.awt.*;

public class ManagerCreateUserForm extends JFrame {

    private JTextField idField, nameField, phoneField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton, editAddressButton;

    private String newAddressId;

    public ManagerCreateUserForm() {
        setTitle("Create Manager");
        setSize(400,405);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        JLabel label = new JLabel("ID:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label);
        idField = new JTextField(IdGenerationUtils.getNextId(ServiceType.MANAGER, null, null));
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

        saveButton.addActionListener(e -> createManager());
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
        JTextField countryField = new JTextField();

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
            // New address object
            AddressDTO newAddress = new AddressDTO();
            newAddress.setId(IdGenerationUtils.getNextId(ServiceType.ADDRESS, null, null));
            newAddress.setUserId(IdGenerationUtils.getNextId(ServiceType.MANAGER, null, null));
            newAddress.setStreet(streetField.getText());
            newAddress.setCity(cityField.getText());
            newAddress.setState(stateField.getText());
            newAddress.setPostalCode(postalCodeField.getText());
            newAddress.setCountry(countryField.getText());

            ResponseCode response = AddressService.createAddress(newAddress);
            if (response == ResponseCode.SUCCESS) {
                newAddressId = newAddress.getId();
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

    private void createManager() {
        if (newAddressId == null) {
            JOptionPane.showMessageDialog(this, "Please create an address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ManagerDTO manager = new ManagerDTO();
        manager.setId(IdGenerationUtils.getNextId(ServiceType.CUSTOMER, null, null));
        manager.setName(nameField.getText());
        manager.setPhoneNumber(phoneField.getText());
        manager.setEmailAddress(emailField.getText());
        manager.setPassword(passwordField.getText());
        manager.setStatus(statusCheckBox.isSelected());
        manager.setAddressId(newAddressId);

        ResponseCode response = ManagerService.createManager(manager);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Manager created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create manager", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
