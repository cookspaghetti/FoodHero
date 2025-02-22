package ui.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dto.VendorDTO;
import enumeration.ButtonMode;
import service.vendor.VendorService;
import ui.form.VendorCreateUserForm;
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

		loadVendors();

		add(new JScrollPane(vendorTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchVendors(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		
		// Validate search term
		if (searchTerm.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a search term", "Search Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Clear table
		tableModel.setRowCount(0);

		// Get list of vendors
		List<VendorDTO> vendors = VendorService.readAllVendor();

		// Search for runner
		for (VendorDTO vendor : vendors) {
			if (vendor.getName().toLowerCase().contains(searchTerm)) {
				addRow(vendor.getId(), vendor.getName(), vendor.getEmailAddress(), vendor.getStatus());
			}
			if (vendor.getId().toLowerCase().contains(searchTerm)) {
				addRow(vendor.getId(), vendor.getName(), vendor.getEmailAddress(), vendor.getStatus());
			}
		}
	}

	private void filterVendors(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(filter);
	}
	
	private void createVendor(ActionEvent e) {
		new VendorCreateUserForm().setVisible(true);
		loadVendors();
	}

	// ======= Filtering Logic =======
	private void filterTable(String filter) {
		for (int i = 0; i < vendorTable.getRowCount(); i++) {
			boolean visible = false;
			switch (filter) {
				case "All":
					visible = true;
					break;
				case "Active":
					visible = vendorTable.getValueAt(i, 3).equals("Active");
					break;
				case "Inactive":
					visible = vendorTable.getValueAt(i, 3).equals("Inactive");
					break;
			}
			vendorTable.setRowHeight(i, visible ? 40 : 0);
		}
	}

	// ======= Utility Method =======
	private void addRow(String id, String name, String email, boolean status) {
		tableModel.addRow(new Object[]{id, name, email, status ? "Active" : "Inactive", "Edit"});
	}

	private void loadVendors() {
		tableModel.setRowCount(0);
		List<VendorDTO> vendors = VendorService.readAllVendor();
		for (VendorDTO vendor : vendors) {
			addRow(vendor.getId(), vendor.getName(), vendor.getEmailAddress(), vendor.getStatus());
		}
	}
}
