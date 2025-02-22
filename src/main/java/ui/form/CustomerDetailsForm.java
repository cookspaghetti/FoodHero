package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dto.CustomerDTO;
import dto.AddressDTO;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.customer.CustomerService;

public class CustomerDetailsForm extends JFrame {
    private JTextField idField, nameField, phoneField, emailField, passwordField;
    private JCheckBox statusCheckBox;
    private JButton saveButton, closeButton, editAddressButton;

    public CustomerDetailsForm(CustomerDTO customer) {
        setTitle("Customer Information");
        setSize(400, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        JLabel label = new JLabel("ID:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
        idField = new JTextField(customer.getId());
        idField.setEditable(false);
        add(idField);

        JLabel label_1 = new JLabel("Name:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_1);
        nameField = new JTextField(customer.getName());
        add(nameField);

        JLabel label_2 = new JLabel("Phone Number:");
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_2);
        phoneField = new JTextField(customer.getPhoneNumber());
        add(phoneField);

        JLabel label_3 = new JLabel("Address:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_3);
        editAddressButton = new JButton("Edit Address");
        add(editAddressButton);

        JLabel label_4 = new JLabel("Email Address:");
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_4);
        emailField = new JTextField(customer.getEmailAddress());
        add(emailField);

        JLabel label_5 = new JLabel("Password:");
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_5);
        passwordField = new JPasswordField(customer.getPassword());
        add(passwordField);

        JLabel label_6 = new JLabel("Status:");
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_6);
        statusCheckBox = new JCheckBox();
        statusCheckBox.setSelected(customer.getStatus());
        add(statusCheckBox);

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
        add(saveButton);
        add(closeButton);

        closeButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> updateCustomer(customer));
        editAddressButton.addActionListener(e -> openAddressDialog(customer));
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openAddressDialog(CustomerDTO customer) {
        AddressDTO address = AddressService.getAddressById(customer.getAddressId());

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

    private void updateCustomer(CustomerDTO customer) {
        // Check if the fields are empty
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any fields is modified
        if (idField.getText().equals(customer.getId()) && nameField.getText().equals(customer.getName())
                && phoneField.getText().equals(customer.getPhoneNumber()) && emailField.getText().equals(customer.getEmailAddress())
                && passwordField.getText().equals(customer.getPassword()) && statusCheckBox.isSelected() == customer.getStatus()) {
            JOptionPane.showMessageDialog(this, "No changes detected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CustomerDTO updatedCustomer = new CustomerDTO();
        updatedCustomer.setId(customer.getId());
        updatedCustomer.setName(nameField.getText());
        updatedCustomer.setPhoneNumber(phoneField.getText());
        updatedCustomer.setEmailAddress(emailField.getText());
        updatedCustomer.setPassword(passwordField.getText());
        updatedCustomer.setStatus(statusCheckBox.isSelected());

        ResponseCode response = CustomerService.updateCustomer(updatedCustomer);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Customer updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update customer", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        CustomerDTO customer = new CustomerDTO();
        customer.setId("1");
        customer.setName("John Doe");
        customer.setPhoneNumber("12345678");
        customer.setEmailAddress("");
        customer.setPassword("password");
        customer.setStatus(true);
        customer.setAddressId("1");

        new CustomerDetailsForm(customer);
    }
}
