package ui.form;

import javax.swing.*;

import dto.ItemDTO;
import enumeration.ResponseCode;
import service.general.SessionControlService;
import service.item.ItemService;

import java.awt.*;

public class VendorItemDetailsForm extends JFrame {
    private JTextField idField, nameField, priceField, defaultAmountField, descriptionField;
    private JComboBox<String> availabilityComboBox;
    private JButton saveButton, closeButton;

    public VendorItemDetailsForm(ItemDTO item) {
        setTitle("Item Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(7, 2, 5, 5));

        getContentPane().add(new JLabel("Item ID:"));
        idField = new JTextField(item.getId());
        idField.setEditable(false);
        getContentPane().add(idField);

        getContentPane().add(new JLabel("Name:"));
        nameField = new JTextField(item.getName());
        getContentPane().add(nameField);

        getContentPane().add(new JLabel("Price:"));
        priceField = new JTextField(String.valueOf(item.getPrice()));
        getContentPane().add(priceField);

        getContentPane().add(new JLabel("Default Amount:"));
        defaultAmountField = new JTextField(String.valueOf(item.getDefaultAmount()));
        getContentPane().add(defaultAmountField);

        getContentPane().add(new JLabel("Description:"));
        descriptionField = new JTextField(item.getDescription());
        getContentPane().add(descriptionField);

        getContentPane().add(new JLabel("Availability:"));
        availabilityComboBox = new JComboBox<>(new String[] { "Active", "Inactive" });
        availabilityComboBox.setSelectedItem(item.isAvailability() ? "Active" : "Inactive");
        getContentPane().add(availabilityComboBox);

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> updateItem());
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        getContentPane().add(saveButton);
        getContentPane().add(closeButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to update item details
    private void updateItem() {
        // Get values from form
        String name = nameField.getText();
        String description = descriptionField.getText();
        String itemId = idField.getText();

        if (itemId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        int defaultAmount;
        try {
            price = Double.parseDouble(priceField.getText());
            defaultAmount = Integer.parseInt(defaultAmountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price and default amount.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.isEmpty() || description.isEmpty() || price <= 0 || defaultAmount <= 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields with valid values.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean availability = "Active".equals(availabilityComboBox.getSelectedItem());

        // Fetch existing item
        ItemDTO item = ItemService.readItem(SessionControlService.getId(), itemId);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update item details
        item.setName(name);
        item.setPrice(price);
        item.setDefaultAmount(defaultAmount);
        item.setDescription(description);
        item.setAvailability(availability);

        // Call service to update item
        ResponseCode response = ItemService.updateItem(item);
        if (response != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Failed to update item details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Item details updated successfully.", "Success",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}
