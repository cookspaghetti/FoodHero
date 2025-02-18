package ui.profile;

import javax.swing.*;

import dto.CustomerDTO;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class CustomerProfilePage extends JFrame {
    private JTextField idField, nameField, phoneField, emailField;
    private JPasswordField passwordField;
    private List<String> deliveryAddresses;
    private DefaultComboBoxModel<String> addressComboBoxModel;

    public CustomerProfilePage(CustomerDTO customer) {
        deliveryAddresses = new ArrayList<>();
        deliveryAddresses.add("123 Main St, Los Angeles, CA, 90001, USA");
        setTitle("Customer Profile");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("ID:"));
        idField = new JTextField(customer.getId());
        add(idField);

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
        addAddressButton.addActionListener(e -> openAddressDialog());
        add(addAddressButton);

        JButton removeAddressButton = new JButton("Remove Address");
        removeAddressButton.addActionListener(e -> openRemoveAddressDialog());
        add(removeAddressButton);

        JButton saveButton = new JButton("Save");
        add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        add(cancelButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openAddressDialog() {
        JDialog dialog = new JDialog(this, "Add Address", true);
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

        JButton saveAddressButton = new JButton("Save Address");
        saveAddressButton.addActionListener(e -> {
            String fullAddress = streetField.getText() + ", " + cityField.getText() + ", " + stateField.getText() + ", " + postalCodeField.getText() + ", " + countryField.getText();
            deliveryAddresses.add(fullAddress);
            addressComboBoxModel.addElement(fullAddress);
            dialog.dispose();
        });
        dialog.add(saveAddressButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openRemoveAddressDialog() {
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
                addressComboBoxModel.removeElement(selectedAddress);
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
}
