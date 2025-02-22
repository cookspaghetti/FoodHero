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

        searchButton.addActionListener(e -> searchVendor());
        yearFilterComboBox.addActionListener(e -> filterYear(yearFilterComboBox.getSelectedItem().toString()));
        
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

        // Populate fixed months
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            tableModel.addRow(new Object[]{month, "N/A", "N/A"});
        }

        setVisible(true);
    }
    
    // Method to update the table
    private void updateTable(String year) {
        // Get list of orders
        List<OrderDTO> orders = OrderService.readVendorOrders(vendor.getId());
        for (OrderDTO order : orders) {
            // Filter orders that are not delivered
            if (order.getStatus() != OrderStatus.DELIVERED) {
                continue;
            }
            // Filter orders that are not in the selected year
            if (!String.valueOf(order.getCompletionTime().getYear()).equals(year)) {
                continue;
            }

            int month = order.getCompletionTime().getMonthValue();
            int orderCount = Integer.parseInt(tableModel.getValueAt(month - 1, 1).toString());
            double earnings = Double.parseDouble(tableModel.getValueAt(month - 1, 2).toString());

            tableModel.setValueAt(orderCount + 1, month - 1, 1);
            tableModel.setValueAt(earnings + order.getTotalAmount(), month - 1, 2);
        }
    }
    
    // Method to filter the year
    private void filterYear(String year) {
        // Reset table
        tableModel.setRowCount(0);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            tableModel.addRow(new Object[]{month, "N/A", "N/A"});
        }

        // Update table
        updateTable(year);
    }
    
    // Method to read the vendor details
    private void searchVendor() {
        String searchTerm = searchField.getText().trim().toLowerCase();

		// Validate search term
		if (searchTerm.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a search term", "Search Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

        // Reset table
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            tableModel.addRow(new Object[]{month, "N/A", "N/A"});
        }

		// Search for vendor
		if (VendorService.readVendor(searchTerm) != null) {
            vendor = VendorService.readVendor(searchTerm);
        }

        if (VendorService.readVendorByName(searchTerm) != null) {
            vendor = VendorService.readVendorByName(searchTerm);
        }

        if (vendor.getId() == null) {
            JOptionPane.showMessageDialog(this, "Vendor not found", "Search Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update table
        updateTable(String.valueOf(Year.now().getValue()));
    }

    public static void main(String[] args) {
        new ManagerRevenuePage();
    }
}

