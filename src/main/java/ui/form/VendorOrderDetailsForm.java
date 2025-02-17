package ui.form;

import javax.swing.*;

import dto.OrderDTO;
import enumeration.OrderStatus;

import java.awt.*;

public class VendorOrderDetailsForm extends JFrame {
    private JTextField idField, customerIdField, itemField, totalAmountField, placementTimeField, notesField;
    private JComboBox<OrderStatus> statusComboBox;
    private JButton saveButton, cancelButton;

    public VendorOrderDetailsForm(OrderDTO order) {
        setTitle("Order Details");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        getContentPane().add(new JLabel("Order ID:"));
        idField = new JTextField(order.getId());
        idField.setEditable(false);
        getContentPane().add(idField);

        getContentPane().add(new JLabel("Customer ID:"));
        customerIdField = new JTextField(order.getCustomerId());
        customerIdField.setEditable(false);
        getContentPane().add(customerIdField);
        
        getContentPane().add(new JLabel("Items:"));
        itemField = new JTextField(order.getItems().toString()); // need review
        itemField.setEditable(false);
        getContentPane().add(itemField);

        getContentPane().add(new JLabel("Notes:"));
        notesField = new JTextField(order.getNotes());
        notesField.setEditable(false);
        getContentPane().add(notesField);
        
        getContentPane().add(new JLabel("Total Amount:"));
        totalAmountField = new JTextField(String.valueOf(order.getTotalAmount()));
        totalAmountField.setEditable(false);
        getContentPane().add(totalAmountField);

        getContentPane().add(new JLabel("Placement Time:"));
        placementTimeField = new JTextField(order.getPlacementTime().toString());
        placementTimeField.setEditable(false);
        getContentPane().add(placementTimeField);

        getContentPane().add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(OrderStatus.values());
        statusComboBox.setSelectedItem(order.getStatus());
        getContentPane().add(statusComboBox);

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        getContentPane().add(saveButton);
        getContentPane().add(cancelButton);

        cancelButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

