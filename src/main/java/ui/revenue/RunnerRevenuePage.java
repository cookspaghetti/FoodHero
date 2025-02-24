package ui.revenue;

import java.awt.BorderLayout;
import java.time.Year;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dto.OrderDTO;
import dto.RunnerDTO;
import enumeration.OrderStatus;
import service.general.SessionControlService;
import service.order.OrderService;

public class RunnerRevenuePage extends JFrame {
    private JComboBox<String> yearFilterComboBox;
    private JTable revenueTable;
    private JScrollPane tableScrollPane;
    private DefaultTableModel tableModel;

    private RunnerDTO runner = (RunnerDTO) SessionControlService.getUser();

    public RunnerRevenuePage() {
        setTitle("Runner Revenue Review");
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

        yearFilterComboBox.setSelectedItem(String.valueOf(Year.now().getValue()));
        updateTable(yearFilterComboBox.getSelectedItem().toString());

        yearFilterComboBox.addActionListener(e -> filterYear(yearFilterComboBox.getSelectedItem().toString()));

        setVisible(true);
    }
    
    // Method to update the table
    private void updateTable(String year) {
        // Get list of orders - Fix: using wrong service method
        List<OrderDTO> orders = OrderService.readRunnerOrders(runner.getId());
        tableModel.setRowCount(0);
    
        // Initialize table with zeros
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            tableModel.addRow(new Object[]{month, 0, 0.0});
        }
    
        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders found for " + year, "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        for (OrderDTO order : orders) {
            try {
                // Skip invalid orders
                if (order == null || order.getCompletionTime() == null || 
                    order.getStatus() != OrderStatus.DELIVERED || 
                    !String.valueOf(order.getCompletionTime().getYear()).equals(year)) {
                    continue;
                }
    
                int month = order.getCompletionTime().getMonthValue();
                int currentCount = Integer.parseInt(tableModel.getValueAt(month - 1, 1).toString());
                double currentEarnings = Double.parseDouble(tableModel.getValueAt(month - 1, 2).toString());
    
                tableModel.setValueAt(currentCount + 1, month - 1, 1);
                tableModel.setValueAt(String.format("%.2f", currentEarnings + order.getDeliveryFee()), 
                    month - 1, 2);
    
            } catch (Exception e) {
                System.err.println("Error processing order: " + e.getMessage());
            }
        }
    }
    
    // Method to filter by year
    private void filterYear(String year) {
        tableModel.setRowCount(0);
        updateTable(year);
    }

}
