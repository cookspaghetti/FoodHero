package ui.task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.TaskDTO;
import enumeration.TaskStatus;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RunnerTaskHistoryPage extends JFrame {
    private JTable taskTable;
    private JComboBox<String> datePicker;
    private JComboBox<TaskStatus> statusFilter;
    private DefaultTableModel tableModel;

    public RunnerTaskHistoryPage() {
        setTitle("Runner Task History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Date Picker
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Select Date:"));
        datePicker = new JComboBox<>();
        populateDatePicker();
        headerPanel.add(datePicker);
        add(headerPanel, BorderLayout.NORTH);
        
        headerPanel.add(new JLabel("Filter by Status:"));
        statusFilter = new JComboBox<>(TaskStatus.values());
        headerPanel.add(statusFilter);

        // Table Setup
        String[] columnNames = {"Task ID", "Order ID", "Vendor Name", "Task Details", "Status", "Delivery Fee", "Acceptance Time", "Completion Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(35);
        
        taskTable.getColumn("Task ID").setPreferredWidth(40);
        taskTable.getColumn("Order ID").setPreferredWidth(40);
        taskTable.getColumn("Vendor Name").setPreferredWidth(100);
        taskTable.getColumn("Task Details").setPreferredWidth(100);
        taskTable.getColumn("Delivery Fee").setPreferredWidth(60);
      
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void populateDatePicker() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            datePicker.addItem(today.minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

    private void loadTaskData(List<TaskDTO> taskData) {
        for (TaskDTO task : taskData) {
            
        }
    }

}
