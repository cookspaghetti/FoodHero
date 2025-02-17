package ui.revenue;

import java.awt.BorderLayout;
import java.time.Year;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VendorRevenuePage extends JFrame {
    private JComboBox<String> yearFilterComboBox;
    private JTable revenueTable;
    private JScrollPane tableScrollPane;
    private DefaultTableModel tableModel;

    public VendorRevenuePage() {
        setTitle("Vendor Revenue Review");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel();
        yearFilterComboBox = new JComboBox<>();
        
        // Populate year combo box (starting from 2015)
        for (int year = 2015; year <= Year.now().getValue(); year++) {
            yearFilterComboBox.addItem(String.valueOf(year));
        }
        
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
        new VendorRevenuePage();
    }
}
