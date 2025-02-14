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

public class CustomerPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable customerTable;
	private DefaultTableModel tableModel;
	private JButton createCustomerButton;

	public CustomerPage() {
		setTitle("Customer Management");
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
		searchButton.addActionListener(this::searchCustomers);

		filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
		filterComboBox.addActionListener(this::filterCustomers);

		topPanel.add(new JLabel("Search:"));
		topPanel.add(searchField);
		topPanel.add(searchButton);
		topPanel.add(new JLabel("Filter:"));
		topPanel.add(filterComboBox);
		
		createCustomerButton = new JButton("New Customer");
		createCustomerButton.addActionListener(this::createCustomer);
		topPanel.add(createCustomerButton);

		add(topPanel, BorderLayout.NORTH);

		// ======= Customer Table =======
		String[] columnNames = {"ID", "Name", "Email", "Status", "Action"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 4; // Make only the "Action" column editable
		    }
		};
		customerTable = new JTable(tableModel);
		customerTable.setRowHeight(40); 
		
		// Apply renderer and editor to the table
		customerTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.EDITDELETE));
		customerTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(customerTable, ButtonMode.EDITDELETE));

		// Example data (replace with actual data)
		addCustomerRow("CUS00001", "Alex Tho", "alex@example.com", "Active");
		addCustomerRow("CUS00001", "Jane Doe", "jane@example.com", "Inactive");

		add(new JScrollPane(customerTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchCustomers(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		filterTable(searchTerm, (String) filterComboBox.getSelectedItem());
	}

	private void filterCustomers(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(searchField.getText().trim().toLowerCase(), filter);
	}
	
	private void createCustomer(ActionEvent e) {
		JOptionPane.showMessageDialog(this, "Add Manager button clicked.");
	}

	// ======= Filtering Logic =======
	private void filterTable(String searchTerm, String filter) {
		for (int i = 0; i < customerTable.getRowCount(); i++) {
			String name = customerTable.getValueAt(i, 1).toString().toLowerCase();
			String email = customerTable.getValueAt(i, 2).toString().toLowerCase();
			String status = customerTable.getValueAt(i, 3).toString();

			boolean matchesSearch = name.contains(searchTerm) || email.contains(searchTerm);
			boolean matchesFilter = filter.equals("All") || status.equals(filter);

			customerTable.setRowHeight(i, (matchesSearch && matchesFilter) ? 20 : 0); // Hide unmatched rows
		}
	}

	// ======= Utility Method =======
	private void addCustomerRow(String id, String name, String email, String status) {
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
		SwingUtilities.invokeLater(() -> new CustomerPage().setVisible(true));
	}
}
