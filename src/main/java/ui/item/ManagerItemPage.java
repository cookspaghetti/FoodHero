package ui.item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import dto.ItemDTO;
import dto.VendorDTO;
import enumeration.ButtonMode;
import service.item.ItemService;
import service.vendor.VendorService;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class ManagerItemPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable itemTable;
	private DefaultTableModel tableModel;

	public ManagerItemPage() {
		setTitle("Item Management");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Search Panel
		JPanel searchPanel = new JPanel();
		searchField = new JTextField(20);
		searchButton = new JButton("Search");
		filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
		filterComboBox.addActionListener(this::filterItems);
		searchButton.addActionListener(this::searchVendors);

		searchPanel.add(new JLabel("Search Vendor:"));
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
		itemTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer(ButtonMode.DISABLEDELETE));
		itemTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(itemTable, ButtonMode.DISABLEDELETE));

		// Set column widths
		TableColumn idColumn = itemTable.getColumn("Item ID");
		idColumn.setPreferredWidth(80);

		TableColumn nameColumn = itemTable.getColumn("Item Name");
		nameColumn.setPreferredWidth(250);

		TableColumn priceColumn = itemTable.getColumn("Price");
		priceColumn.setPreferredWidth(100);

		TableColumn descColumn = itemTable.getColumn("Vendor ID");
		descColumn.setPreferredWidth(80);

		TableColumn availColumn = itemTable.getColumn("Availability");
		availColumn.setPreferredWidth(80);

		TableColumn actionColumn = itemTable.getColumn("Actions");
		actionColumn.setPreferredWidth(180);

		add(new JScrollPane(itemTable), BorderLayout.CENTER);

		setVisible(true);
	}

	// ======= Action Methods =======
	private void searchVendors(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();

		// Validate search term
		if (searchTerm.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a search term", "Search Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get vendor info
		VendorDTO vendor = new VendorDTO();
		if (VendorService.readVendor(searchTerm) != null) {
			vendor = VendorService.readVendor(searchTerm);
		} else if (VendorService.readVendorByName(searchTerm) != null) {
			vendor = VendorService.readVendorByEmail(searchTerm);
		} else {
			JOptionPane.showMessageDialog(this, "Vendor not found", "Search Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Clear table
		tableModel.setRowCount(0);

		// Update table
		List<ItemDTO> items = ItemService.readAllItem(vendor.getId());
		for (ItemDTO item : items) {
			addItemRow(item.getId(), item.getName(), String.valueOf(item.getPrice()), item.getDescription(), item.isAvailability() ? "Active" : "Inactive");
		}
	}

	private void filterItems(ActionEvent e) {
        String filter = (String) filterComboBox.getSelectedItem();
        if (filter != null) {
            filterTable(filter);
        }
    }

	// ======= Filtering Logic =======
	private void filterTable(String filter) {
        for (int i = 0; i < itemTable.getRowCount(); i++) {
            String status = itemTable.getValueAt(i, 4).toString(); // Column index for Availability
            boolean matchesFilter = filter.equals("All") || status.equals(filter);
            itemTable.setRowHeight(i, matchesFilter ? 40 : 0);
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

}


