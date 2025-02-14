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

public class VendorPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable vendorTable;
	private DefaultTableModel tableModel;
	private JButton createVendorButton;

	public VendorPage() {
		setTitle("Vendor Management");
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
		searchButton.addActionListener(this::searchVendors);

		filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
		filterComboBox.addActionListener(this::filterVendors);

		topPanel.add(new JLabel("Search:"));
		topPanel.add(searchField);
		topPanel.add(searchButton);
		topPanel.add(new JLabel("Filter:"));
		topPanel.add(filterComboBox);
		
		createVendorButton = new JButton("New Vendor");
		createVendorButton.addActionListener(this::createVendor);
		topPanel.add(createVendorButton);

		add(topPanel, BorderLayout.NORTH);

		// ======= Vendor Table =======
		String[] columnNames = {"ID", "Name", "Email", "Status", "Action"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 4; // Make only the "Action" column editable
		    }
		};
		vendorTable = new JTable(tableModel);
		vendorTable.setRowHeight(40); 
		
		// Apply renderer and editor to the table
		vendorTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.EDITDELETE));
		vendorTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(vendorTable, ButtonMode.EDITDELETE));

		// Example data (replace with actual data)
		addVendorRow("VDR00001", "Alex Tho", "alex@example.com", "Active");
		addVendorRow("VDR00001", "Jane Doe", "jane@example.com", "Inactive");

		add(new JScrollPane(vendorTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchVendors(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		filterTable(searchTerm, (String) filterComboBox.getSelectedItem());
	}

	private void filterVendors(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(searchField.getText().trim().toLowerCase(), filter);
	}
	
	private void createVendor(ActionEvent e) {
		JOptionPane.showMessageDialog(this, "Add Manager button clicked.");
	}

	// ======= Filtering Logic =======
	private void filterTable(String searchTerm, String filter) {
		for (int i = 0; i < vendorTable.getRowCount(); i++) {
			String name = vendorTable.getValueAt(i, 1).toString().toLowerCase();
			String email = vendorTable.getValueAt(i, 2).toString().toLowerCase();
			String status = vendorTable.getValueAt(i, 3).toString();

			boolean matchesSearch = name.contains(searchTerm) || email.contains(searchTerm);
			boolean matchesFilter = filter.equals("All") || status.equals(filter);

			vendorTable.setRowHeight(i, (matchesSearch && matchesFilter) ? 20 : 0); // Hide unmatched rows
		}
	}

	// ======= Utility Method =======
	private void addVendorRow(String id, String name, String email, String status) {
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
		SwingUtilities.invokeLater(() -> new VendorPage().setVisible(true));
	}
}
