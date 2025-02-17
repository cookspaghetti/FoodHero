package ui.form;

import javax.swing.*;

import dto.ItemDTO;

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
        availabilityComboBox = new JComboBox<>(new String[]{"Active", "Inactive"});
        availabilityComboBox.setSelectedItem(item.isAvailability() ? "Active" : "Inactive");
        getContentPane().add(availabilityComboBox);

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        getContentPane().add(saveButton);
        getContentPane().add(closeButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
