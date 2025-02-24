package ui.task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dto.OrderDTO;
import dto.RunnerDTO;
import dto.TaskDTO;
import enumeration.TaskStatus;
import service.general.SessionControlService;
import service.order.OrderService;
import service.task.TaskService;
import service.vendor.VendorService;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RunnerTaskHistoryPage extends JFrame {
    private JTable taskTable;
    private JDateChooser datePicker;
    private JButton filterButton;
    private JComboBox<TaskStatus> statusFilter;
    private DefaultTableModel tableModel;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private RunnerDTO runner = (RunnerDTO) SessionControlService.getUser();

    public RunnerTaskHistoryPage() {
        setTitle("Runner Task History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Date Picker
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Select Date:"));
        datePicker = new JDateChooser();
        datePicker.setPreferredSize(new Dimension(150, 25));
        filterButton = new JButton("Find");
        headerPanel.add(datePicker);
        headerPanel.add(filterButton);
        add(headerPanel, BorderLayout.NORTH);
        
        headerPanel.add(new JLabel("Filter by Status:"));
        statusFilter = new JComboBox<>(TaskStatus.values());
        headerPanel.add(statusFilter);

        filterButton.addActionListener(e -> filterTaskbyDate());
        statusFilter.addActionListener(e -> filterTaskbyStatus());

        // Table Setup
        String[] columnNames = {"Task ID", "Order ID", "Vendor Name", "Task Details", "Status", "Delivery Fee", "Acceptance Time", "Completion Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(35);
        
        taskTable.getColumn("Task ID").setPreferredWidth(40);
        taskTable.getColumn("Order ID").setPreferredWidth(40);
        taskTable.getColumn("Vendor Name").setPreferredWidth(100);
        taskTable.getColumn("Task Details").setPreferredWidth(100);
        taskTable.getColumn("Delivery Fee").setPreferredWidth(50);
      
        add(new JScrollPane(taskTable), BorderLayout.CENTER);

        loadTaskData();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadTaskData() {
        List<TaskDTO> tasks = TaskService.readTaskByRunnerId(runner.getId());
        for (TaskDTO task : tasks) {
            tableModel.addRow(new Object[]{
                    task.getId(),
                    task.getOrderId(),
                    getVendorName(task.getOrderId()),
                    task.getTaskDetails(),
                    task.getStatus(),
                    task.getDeliveryFee(),
                    task.getAcceptanceTime().format(formatter),
                    task.getCompletionTime() == null ? "" : task.getCompletionTime().format(formatter)
            });
        }
    }

    private String getVendorName(String orderId) {
        OrderDTO order = OrderService.readOrder(orderId);
        return VendorService.readVendor(order.getVendorId()).getName();
    }

    private void filterTaskbyDate() {
        tableModel.setRowCount(0);

        LocalDate selectedDate = datePicker.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        List<TaskDTO> tasks = TaskService.readTaskByRunnerId(runner.getId());
        for (TaskDTO task : tasks) {
            if (task.getAcceptanceTime().toLocalDate().equals(selectedDate)) {
                tableModel.addRow(new Object[]{
                        task.getId(),
                        task.getOrderId(),
                        getVendorName(task.getOrderId()),
                        task.getTaskDetails(),
                        task.getStatus(),
                        task.getDeliveryFee(),
                        task.getAcceptanceTime().format(formatter),
                        task.getCompletionTime() == null ? "" : task.getCompletionTime().format(formatter)
                });
            }
        }
    }

    private void filterTaskbyStatus() {
        tableModel.setRowCount(0);

        TaskStatus selectedStatus = (TaskStatus) statusFilter.getSelectedItem();

        List<TaskDTO> tasks = TaskService.readTaskByRunnerId(runner.getId());
        for (TaskDTO task : tasks) {
            if (task.getStatus().equals(selectedStatus)) {
                tableModel.addRow(new Object[]{
                        task.getId(),
                        task.getOrderId(),
                        getVendorName(task.getOrderId()),
                        task.getTaskDetails(),
                        task.getStatus(),
                        task.getDeliveryFee(),
                        task.getAcceptanceTime().format(formatter),
                        task.getCompletionTime() == null ? "" : task.getCompletionTime().format(formatter)
                });
            }
        }
    }

}
