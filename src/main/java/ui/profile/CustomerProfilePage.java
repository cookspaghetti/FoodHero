package ui.profile;

import javax.swing.*;

import dto.AddressDTO;
import dto.CustomerDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.address.AddressService;
import service.customer.CustomerService;
import service.distance.DistanceService;
import service.utils.IdGenerationUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerProfilePage extends JFrame {
    private JLabel idLabel;
    private JTextField nameField, phoneField, emailField;
    private JPasswordField passwordField;

    public CustomerProfilePage(CustomerDTO customer) {
        setTitle("Customer Profile");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("ID:"));
        idLabel = new JLabel(customer.getId());
        add(idLabel);

        add(new JLabel("Name:"));
        nameField = new JTextField(customer.getName());
        add(nameField);

        add(new JLabel("Phone Number:"));
        phoneField = new JTextField(customer.getPhoneNumber());
        add(phoneField);

        add(new JLabel("Email Address:"));
        emailField = new JTextField(customer.getEmailAddress());
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField(customer.getPassword());
        add(passwordField);

        JButton addAddressButton = new JButton("Add Address");
        addAddressButton.addActionListener(e -> openAddAddressDialog(customer));
        add(addAddressButton);

        JButton removeAddressButton = new JButton("Remove Address");
        removeAddressButton.addActionListener(e -> openRemoveAddressDialog(customer));
        add(removeAddressButton);

        JButton editAddressButton = new JButton("Edit Address");
        editAddressButton.addActionListener(e -> openEditAddressDialog(customer));
        add(editAddressButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> updateCustomer(customer));
        add(saveButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to add a new address to the customer's list of addresses
    private void openAddAddressDialog(CustomerDTO customer) {
        JDialog dialog = new JDialog(this, "Add Address", true);
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

        JButton saveAddressButton = new JButton("Save Address");
        saveAddressButton.addActionListener(e -> {
            // validation
            if (streetField.getText().isEmpty() || cityField.getText().isEmpty() || stateField.getText().isEmpty()
                    || postalCodeField.getText().isEmpty() || countryField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // save address
            AddressDTO newAddress = new AddressDTO();
            newAddress.setId(IdGenerationUtils.getNextId(ServiceType.ADDRESS, null, null));
            newAddress.setStreet(streetField.getText());
            newAddress.setCity(cityField.getText());
            newAddress.setState(stateField.getText());
            newAddress.setPostalCode(postalCodeField.getText());
            newAddress.setCountry(countryField.getText());
            newAddress.setUserId(customer.getId());

            // verification using api
            if (!DistanceService.verifyAddress(newAddress)) {
                JOptionPane.showMessageDialog(this, "Invalid address", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ResponseCode response = AddressService.createAddress(newAddress);
            if (response != ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Failed to save address", "Error", JOptionPane.ERROR_MESSAGE);
            }
            List<String> deliveryAddresses = customer.getDeliveryAddresses();
            if (deliveryAddresses == null) {
                deliveryAddresses = new ArrayList<>();
            }
            deliveryAddresses.add(newAddress.getId());
            customer.setDeliveryAddresses(deliveryAddresses);

            response = CustomerService.updateCustomer(customer);
            if (response == ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Address saved successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save address", "Error", JOptionPane.ERROR_MESSAGE);
            }

            dialog.dispose();
        });

        dialog.add(saveAddressButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Method to delete an address from the customer's list of addresses
    private void openRemoveAddressDialog(CustomerDTO customer) {
        List<String> deliveryAddresses = customer.getDeliveryAddresses();

        if (deliveryAddresses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No addresses to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Remove Address", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new GridLayout(3, 1, 5, 5));

        JComboBox<String> removeComboBox = new JComboBox<>(new DefaultComboBoxModel<>(deliveryAddresses.toArray(new String[0])));
        dialog.add(removeComboBox);

        JButton removeButton = new JButton("Remove Address");
        removeButton.addActionListener(e -> {
            String selectedAddress = (String) removeComboBox.getSelectedItem();
            if (selectedAddress != null) {
                deliveryAddresses.remove(selectedAddress);
                customer.setDeliveryAddresses(deliveryAddresses);

                ResponseCode response = CustomerService.updateCustomer(customer);
                if (response == ResponseCode.SUCCESS) {
                    JOptionPane.showMessageDialog(this, "Address removed successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove address", "Error", JOptionPane.ERROR_MESSAGE);
                }

                dialog.dispose();
            }
        });
        dialog.add(removeButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Method to edit an address from the customer's list of addresses
    private void openEditAddressDialog(CustomerDTO customer) {
        List<String> deliveryAddresses = customer.getDeliveryAddresses();

        if (deliveryAddresses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No addresses to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Address", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(7, 2, 5, 5));

        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField countryField = new JTextField();

        JComboBox<String> editComboBox = new JComboBox<>(new DefaultComboBoxModel<>(deliveryAddresses.toArray(new String[0])));
        editComboBox.addActionListener(e -> {
            String selectedAddress = (String) editComboBox.getSelectedItem();
            if (selectedAddress != null) {
                AddressDTO address = AddressService.getAddressById(selectedAddress);
                streetField.setText(address.getStreet());
                cityField.setText(address.getCity());
                stateField.setText(address.getState());
                postalCodeField.setText(address.getPostalCode());
                countryField.setText(address.getCountry());
            }
        });
        dialog.add(new JLabel("Select Address:"));
        dialog.add(editComboBox);

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

        JButton saveAddressButton = new JButton("Save Address");
        saveAddressButton.addActionListener(e -> {
            // validation
            if (streetField.getText().isEmpty() || cityField.getText().isEmpty() || stateField.getText().isEmpty()
                    || postalCodeField.getText().isEmpty() || countryField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Update address object
            AddressDTO updatedAddress = new AddressDTO();
            updatedAddress.setId((String) editComboBox.getSelectedItem());
            updatedAddress.setStreet(streetField.getText());
            updatedAddress.setCity(cityField.getText());
            updatedAddress.setState(stateField.getText());
            updatedAddress.setPostalCode(postalCodeField.getText());
            updatedAddress.setCountry(countryField.getText());
            updatedAddress.setUserId(customer.getId());

            // verification using api
            if (!DistanceService.verifyAddress(updatedAddress)) {
                JOptionPane.showMessageDialog(this, "Invalid address", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ResponseCode response = AddressService.updateAddress(updatedAddress);
            if (response == ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Address updated successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update address", "Error", JOptionPane.ERROR_MESSAGE);
            }

            dialog.dispose();
        });
        dialog.add(saveAddressButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Method to update the customer's profile
    private void updateCustomer(CustomerDTO customer) {
        // Check if the fields are empty
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
                || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (nameField.getText().equals(customer.getName()) && phoneField.getText().equals(customer.getPhoneNumber())
                && emailField.getText().equals(customer.getEmailAddress())
                && passwordField.getPassword().toString().equals(customer.getPassword())) {
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
        
        customer.setName(nameField.getText());
        customer.setPhoneNumber(phoneField.getText());
        customer.setEmailAddress(emailField.getText());
        customer.setPassword(String.valueOf(passwordField.getPassword()));

        ResponseCode response = CustomerService.updateCustomer(customer);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Customer updated successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update customer", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
