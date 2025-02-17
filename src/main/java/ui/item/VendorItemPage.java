package ui.item;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import enumeration.ButtonMode;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class VendorItemPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JButton filterButton;
	private JComboBox<String> filterComboBox;
	private JTable itemTable;
	private DefaultTableModel tableModel;

	public VendorItemPage() {
		setTitle("Item Management");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Search Panel
		JPanel searchPanel = new JPanel();
		searchField = new JTextField(20);
		searchButton = new JButton("Search");
		filterButton = new JButton("Filter");
		filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
		filterComboBox.addActionListener(this::filterItems);

		searchPanel.add(new JLabel("Search Item:"));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.add(new JLabel("Filter:"));
		searchPanel.add(filterComboBox);

		add(searchPanel, BorderLayout.NORTH);

		// Table Panel
		String[] columnNames = {"Item ID", "Item Name", "Price", "Vendor ID", "Availability", "Actions"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5; // Make only the "Action" column editable
			}
		};
		itemTable = new JTable(tableModel);
		itemTable.setRowHeight(40);

		// Apply renderer and editor to the table
		itemTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer(ButtonMode.EDITDELETE));
		itemTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(itemTable, ButtonMode.EDITDELETE));

		// Set column widths
		TableColumn idColumn = itemTable.getColumn("Item ID");
		idColumn.setPreferredWidth(80); // Fit XXX00000

		TableColumn nameColumn = itemTable.getColumn("Item Name");
		nameColumn.setPreferredWidth(250); // Fit as much as possible

		TableColumn priceColumn = itemTable.getColumn("Price");
		priceColumn.setPreferredWidth(100); // Fit RMXXX.XX

		TableColumn descColumn = itemTable.getColumn("Vendor ID");
		descColumn.setPreferredWidth(80); // Fit as much as possible

		TableColumn availColumn = itemTable.getColumn("Availability");
		availColumn.setPreferredWidth(80); // Active/Inactive

		TableColumn actionColumn = itemTable.getColumn("Actions");
		actionColumn.setPreferredWidth(180); // For one delete button

		addItemRow("ITM00001", "Triple G", "RM88.88", "VDR00001", "Active");
		addItemRow("ITM00001", "Triple G", "RM88.88", "VDR00001", "Active");

		add(new JScrollPane(itemTable), BorderLayout.CENTER);

		setVisible(true);
	}

	// ======= Action Methods =======
	private void searchItems(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		filterTable(searchTerm, (String) filterComboBox.getSelectedItem());
	}

	private void filterItems(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(searchField.getText().trim().toLowerCase(), filter);
	}

	// ======= Filtering Logic =======
	private void filterTable(String searchTerm, String filter) {
		for (int i = 0; i < itemTable.getRowCount(); i++) {
			String name = itemTable.getValueAt(i, 1).toString().toLowerCase();
			String email = itemTable.getValueAt(i, 2).toString().toLowerCase();
			String status = itemTable.getValueAt(i, 3).toString();

			boolean matchesSearch = name.contains(searchTerm) || email.contains(searchTerm);
			boolean matchesFilter = filter.equals("All") || status.equals(filter);

			itemTable.setRowHeight(i, (matchesSearch && matchesFilter) ? 20 : 0); // Hide unmatched rows
		}
	}

	// ======= Utility Method =======
	private void addItemRow(String itemId, String itemName, String price, String desc, String avail) {
		Vector<String> row = new Vector<>();
		row.add(itemId);
		row.add(itemName);
		row.add(price);
		row.add(desc);
		row.add(avail);
		row.add("Actions");
		tableModel.addRow(row);
	}

	public static void main(String[] args) {
		new VendorItemPage();
	}
}
