package ui.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import enumeration.ButtonMode;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class ManagerPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable managerTable;
	private DefaultTableModel tableModel;
	private JButton createManagerButton;

	public ManagerPage() {
		setTitle("Manager Management");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());

		initComponents();
	}

	private void initComponents() {
		// ======= Search & Filter Panel =======
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		searchField = new JTextField(20);
		searchButton = new JButton("Search");
		searchButton.addActionListener(this::searchManagers);

		filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
		filterComboBox.addActionListener(this::filterManagers);

		topPanel.add(new JLabel("Search:"));
		topPanel.add(searchField);
		topPanel.add(searchButton);
		topPanel.add(new JLabel("Filter:"));
		topPanel.add(filterComboBox);

		getContentPane().add(topPanel, BorderLayout.NORTH);
		
		createManagerButton = new JButton("New Manager");
		createManagerButton.addActionListener(this::createManager);
		topPanel.add(createManagerButton);

		// ======= Customer Table =======
		String[] columnNames = {"ID", "Name", "Email", "Status", "Action"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 4; // Make only the "Action" column editable
			}
		};
		managerTable = new JTable(tableModel);
		managerTable.setRowHeight(40); 

		// Apply renderer and editor to the table
		managerTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.EDITDELETE));
		managerTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(managerTable, ButtonMode.EDITDELETE));

		// Example data (replace with actual data)
		addManagerRow("MGR00001", "Alex Tho", "alex@example.com", "Active");
		addManagerRow("MGR00001", "Jane Doe", "jane@example.com", "Inactive");

		getContentPane().add(new JScrollPane(managerTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchManagers(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		filterTable(searchTerm, (String) filterComboBox.getSelectedItem());
	}

	private void filterManagers(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(searchField.getText().trim().toLowerCase(), filter);
	}
	
	private void createManager(ActionEvent e) {
		JOptionPane.showMessageDialog(this, "Add Manager button clicked.");
	}

	// ======= Filtering Logic =======
	private void filterTable(String searchTerm, String filter) {
		for (int i = 0; i < managerTable.getRowCount(); i++) {
			String name = managerTable.getValueAt(i, 1).toString().toLowerCase();
			String email = managerTable.getValueAt(i, 2).toString().toLowerCase();
			String status = managerTable.getValueAt(i, 3).toString();

			boolean matchesSearch = name.contains(searchTerm) || email.contains(searchTerm);
			boolean matchesFilter = filter.equals("All") || status.equals(filter);

			managerTable.setRowHeight(i, (matchesSearch && matchesFilter) ? 20 : 0); // Hide unmatched rows
		}
	}

	// ======= Utility Method =======
	private void addManagerRow(String id, String name, String email, String status) {
		Vector<String> row = new Vector<>();
		row.add(id);
		row.add(name);
		row.add(email);
		row.add(status);
		row.add("Actions");
		tableModel.addRow(row);
	}

	// ======= Main Method for Testing =======
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ManagerPage().setVisible(true));
	}
}
