package ui.notification;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.List;

import service.general.SessionControlService;
import service.notification.NotificationService;
import dto.NotificationDTO;

public class NotificationPage extends JFrame {
    private JComboBox<String> filterComboBox;
    private JTable notificationTable;
    private DefaultTableModel tableModel;

    public NotificationPage() {
        setTitle("Notifications");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Filter Panel
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Filter: "));
        filterComboBox = new JComboBox<>(new String[]{"All", "Read", "Unread"});
        filterComboBox.addActionListener(this::filterNotifications);
        filterPanel.add(filterComboBox);
        add(filterPanel, BorderLayout.NORTH);

        // Table Model
        String[] columnNames = {"Timestamp", "Title", "Message"};
        tableModel = new DefaultTableModel(columnNames, 0);
        notificationTable = new JTable(tableModel);

        // Adjust column widths
        notificationTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        notificationTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        notificationTable.getColumnModel().getColumn(2).setPreferredWidth(350);

        // Load data into the table
        loadNotifications();

        JScrollPane scrollPane = new JScrollPane(notificationTable);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Filter notifications based on the selected filter
    private void filterNotifications(ActionEvent e) {
        String filter = (String) filterComboBox.getSelectedItem();
        // Get all notifications
        List<NotificationDTO> notifications = NotificationService.readAllNotification(SessionControlService.getUser().getId());
        // Clear the table
        tableModel.setRowCount(0);
        // Add notifications based on the filter
        for (NotificationDTO notification : notifications) {
            if (filter.equals("All") || (filter.equals("Read") && notification.isRead()) || (filter.equals("Unread") && !notification.isRead())) {
                tableModel.addRow(new Object[]{notification.getTimestamp(), notification.getTitle(), notification.getMessage()});
            }
        }
    }

    // Load all notifications into the table
    private void loadNotifications() {
        List<NotificationDTO> notifications = NotificationService.readAllNotification(SessionControlService.getUser().getId());
        for (NotificationDTO notification : notifications) {
            tableModel.addRow(new Object[]{notification.getTimestamp(), notification.getTitle(), notification.getMessage()});
        }
    }
}
