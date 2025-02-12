package ui.user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class AdminPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable adminTable;
	private DefaultTableModel tableModel;

	public AdminPage() {
		setTitle("Admin Management");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		initComponents();
	}

	private void initComponents() {
		// ======= Search & Filter Panel =======
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		searchField = new JTextField(20);
		searchButton = new JButton("Search");
		searchButton.addActionListener(this::searchAdmins);

		filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
		filterComboBox.addActionListener(this::filterAdmins);

		topPanel.add(new JLabel("Search:"));
		topPanel.add(searchField);
		topPanel.add(searchButton);
		topPanel.add(new JLabel("Filter:"));
		topPanel.add(filterComboBox);

		add(topPanel, BorderLayout.NORTH);

		// ======= Admin Table =======
		String[] columnNames = {"ID", "Name", "Email", "Status"};
		tableModel = new DefaultTableModel(columnNames, 0);
		adminTable = new JTable(tableModel);

		// Example data (replace with actual data)
		addAdminRow("1", "Alex Tho", "alex@example.com", "Active");
		addAdminRow("2", "Jane Doe", "jane@example.com", "Inactive");

		add(new JScrollPane(adminTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchAdmins(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		filterTable(searchTerm, (String) filterComboBox.getSelectedItem());
	}

	private void filterAdmins(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(searchField.getText().trim().toLowerCase(), filter);
	}

	// ======= Filtering Logic =======
	private void filterTable(String searchTerm, String filter) {
		for (int i = 0; i < adminTable.getRowCount(); i++) {
			String name = adminTable.getValueAt(i, 1).toString().toLowerCase();
			String email = adminTable.getValueAt(i, 2).toString().toLowerCase();
			String status = adminTable.getValueAt(i, 3).toString();

			boolean matchesSearch = name.contains(searchTerm) || email.contains(searchTerm);
			boolean matchesFilter = filter.equals("All") || status.equals(filter);

			adminTable.setRowHeight(i, (matchesSearch && matchesFilter) ? 20 : 0); // Hide unmatched rows
		}
	}

	// ======= Utility Method =======
	private void addAdminRow(String id, String name, String email, String status) {
		Vector<String> row = new Vector<>();
		row.add(id);
		row.add(name);
		row.add(email);
		row.add(status);
		tableModel.addRow(row);
	}

	// ======= Main Method for Testing =======
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new AdminPage().setVisible(true));
	}
}
