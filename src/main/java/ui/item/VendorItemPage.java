package ui.item;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.List;

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

import dto.ItemDTO;
import enumeration.ButtonMode;
import service.general.SessionControlService;
import service.item.ItemService;
import ui.form.VendorCreateItemForm;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class VendorItemPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JButton createButton;
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
		searchButton.addActionListener(this::searchItems);
		filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
		filterComboBox.addActionListener(this::filterItems);
		createButton = new JButton("Create Item");
		createButton.addActionListener(e -> new VendorCreateItemForm());

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

		// Load the items
		loadItems();

		add(new JScrollPane(itemTable), BorderLayout.CENTER);

		setVisible(true);
	}

	// ======= Action Methods =======
	private void searchItems(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		
		// Clear table
		tableModel.setRowCount(0);

		List<ItemDTO> items = ItemService.readAllItem(SessionControlService.getUser().getId());
		for (ItemDTO item : items) {
			if (item.getName().toLowerCase().contains(searchTerm)){
				addItemRow(item.getId(), item.getName(), String.valueOf(item.getPrice()), item.getVendorId(), item.isAvailability() ? "Active" : "Inactive");
			}
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

	// Method to load the items
	private void loadItems() {
		// Clear table
		tableModel.setRowCount(0);

		// Update table
		List<ItemDTO> items = ItemService.readAllItem(SessionControlService.getUser().getId());
		for (ItemDTO item : items) {
			addItemRow(item.getId(), item.getName(), String.valueOf(item.getPrice()), item.getVendorId(), item.isAvailability() ? "Active" : "Inactive");
		}
	}

}
