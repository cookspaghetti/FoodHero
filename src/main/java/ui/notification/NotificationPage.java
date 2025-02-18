package ui.notification;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        JScrollPane scrollPane = new JScrollPane(notificationTable);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new NotificationPage();
    }
}
