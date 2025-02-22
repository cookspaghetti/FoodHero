package ui.revenue;

import java.awt.BorderLayout;
import java.time.Year;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
        yearFilterComboBox.addActionListener(e -> filterYear(yearFilterComboBox.getSelectedItem().toString()));
        
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
    
    // Method to update the table
    private void updateTable(String year) {
        // Get list of orders
        List<OrderDTO> orders = OrderService.readVendorOrders(runner.getId());
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

}
