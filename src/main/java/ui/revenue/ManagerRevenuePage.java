package ui.revenue;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import javax.swing.table.DefaultTableModel;

import java.util.List;

import dto.OrderDTO;
import dto.VendorDTO;
import enumeration.OrderStatus;
import service.order.OrderService;
import service.vendor.VendorService;

public class ManagerRevenuePage extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> yearFilterComboBox;
    private JTable revenueTable;
    private JScrollPane tableScrollPane;
    private DefaultTableModel tableModel;

    private VendorDTO vendor;

    public ManagerRevenuePage() {
        setTitle("Vendor Revenue Review");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        yearFilterComboBox = new JComboBox<>();
        
        // Populate year combo box (starting from 2015)
        for (int year = 2015; year <= Year.now().getValue(); year++) {
            yearFilterComboBox.addItem(String.valueOf(year));
        }

        yearFilterComboBox.setSelectedItem(String.valueOf(Year.now().getValue()));
        
        searchPanel.add(new JLabel("Search Vendor:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Year:"));
        searchPanel.add(yearFilterComboBox);
        add(searchPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"Month", "Orders", "Earnings"};
        tableModel = new DefaultTableModel(columnNames, 0);
        revenueTable = new JTable(tableModel);
        tableScrollPane = new JScrollPane(revenueTable);
        add(tableScrollPane, BorderLayout.CENTER);
        
        searchButton.addActionListener(e -> searchVendor());
        yearFilterComboBox.addActionListener(e -> filterYear(yearFilterComboBox.getSelectedItem().toString()));

        setVisible(true);
    }
    
    // Method to update the table
    private static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };
    
    private void updateTable(String year) {
        if (vendor == null) {
            JOptionPane.showMessageDialog(this, "Vendor not found", "Search Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Get list of orders
        List<OrderDTO> orders = OrderService.readVendorOrders(vendor.getId());
        tableModel.setRowCount(0);
    
        // Initialize table with zeros
        for (String month : MONTHS) {
            tableModel.addRow(new Object[]{month, 0, 0.0});
        }
    
        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders found for " + year, 
                "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        try {
            for (OrderDTO order : orders) {
                // Skip invalid orders
                if (order == null || order.getCompletionTime() == null || 
                    order.getStatus() != OrderStatus.DELIVERED || 
                    !String.valueOf(order.getCompletionTime().getYear()).equals(year)) {
                    continue;
                }
    
                int month = order.getCompletionTime().getMonthValue();
                int currentCount = Integer.parseInt(tableModel.getValueAt(month - 1, 1).toString());
                double currentEarnings = Double.parseDouble(tableModel.getValueAt(month - 1, 2).toString());
    
                // Update count and earnings with proper formatting
                tableModel.setValueAt(currentCount + 1, month - 1, 1);
                tableModel.setValueAt(
                    String.format("%.2f", currentEarnings + order.getTotalAmount()), 
                    month - 1, 2
                );
            }
        } catch (Exception e) {
            System.err.println("Error processing orders: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error processing orders", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to filter by year
    private void filterYear(String year) {
        if (vendor != null) {
            updateTable(year);
        }
    }
    
    // Method to read the vendor details
    private void searchVendor() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        vendor = null;

		// Validate search term
		if (searchTerm.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a search term", "Search Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

        // Reset table
        tableModel.setRowCount(0);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            tableModel.addRow(new Object[]{month, "N/A", "N/A"});
        }

		// Search for vendor
		if (VendorService.readVendor(searchTerm) != null) {
            System.out.println("Vendor found by ID");
            vendor = VendorService.readVendor(searchTerm);
        } else if (VendorService.readVendorByName(searchTerm) != null) {
            System.out.println("Vendor found by name");
            vendor = VendorService.readVendorByName(searchTerm);
        }

        if (vendor == null) {
            JOptionPane.showMessageDialog(this, "Vendor not found", "Search Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update table
        updateTable(String.valueOf(Year.now().getValue()));
    }

}

