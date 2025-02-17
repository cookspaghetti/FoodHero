package ui.revenue;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import javax.swing.table.DefaultTableModel;

public class ManagerRevenuePage extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> yearFilterComboBox;
    private JTable revenueTable;
    private JScrollPane tableScrollPane;
    private DefaultTableModel tableModel;

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
    
    // Method to calculate the earnings
    
    // Method to update the combo box for filtering
    
    // Method to read the vendor details

    public static void main(String[] args) {
        new ManagerRevenuePage();
    }
}

