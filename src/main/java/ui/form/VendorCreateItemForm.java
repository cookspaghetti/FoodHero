package ui.form;

import javax.swing.*;

import dto.ItemDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.item.ItemService;
import service.utils.IdGenerationUtils;

import java.awt.*;

public class VendorCreateItemForm extends JFrame {
    private JTextField idField, nameField, priceField, defaultAmountField, descriptionField;
    private JComboBox<String> availabilityComboBox;
    private JButton saveButton, closeButton;

    public VendorCreateItemForm() {
        setTitle("Create Item");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(7, 2, 5, 5));

        getContentPane().add(new JLabel("Item ID:"));
        idField = new JTextField(IdGenerationUtils.getNextId(ServiceType.ITEM, null, null));
        idField.setEditable(false);
        getContentPane().add(idField);

        getContentPane().add(new JLabel("Name:"));
        nameField = new JTextField();
        getContentPane().add(nameField);

        getContentPane().add(new JLabel("Price:"));
        priceField = new JTextField();
        getContentPane().add(priceField);

        getContentPane().add(new JLabel("Default Amount:"));
        defaultAmountField = new JTextField(String.valueOf(1));
        getContentPane().add(defaultAmountField);

        getContentPane().add(new JLabel("Description:"));
        descriptionField = new JTextField();
        getContentPane().add(descriptionField);

        getContentPane().add(new JLabel("Availability:"));
        availabilityComboBox = new JComboBox<>(new String[]{"Active", "Inactive"});
        availabilityComboBox.setSelectedItem("Active");
        getContentPane().add(availabilityComboBox);

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> createItem());
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        getContentPane().add(saveButton);
        getContentPane().add(closeButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to update item details
    private void createItem() {
        // Get the values from the form
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int defaultAmount = Integer.parseInt(defaultAmountField.getText());
        String description = descriptionField.getText();
        boolean availability = availabilityComboBox.getSelectedItem().equals("Active");

        // Validate the values
        if (name.isEmpty() || price <= 0 || defaultAmount <= 0 || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields with valid values.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the item object
        ItemDTO newItem = new ItemDTO();
        newItem.setId(idField.getText());
        newItem.setName(name);
        newItem.setPrice(price);
        newItem.setDefaultAmount(defaultAmount);
        newItem.setDescription(description);
        newItem.setAvailability(availability);

        // Call the service to update the item details
        ResponseCode response = ItemService.createItem(newItem);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Item created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create item", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
