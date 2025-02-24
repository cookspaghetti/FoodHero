package ui.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import dto.CustomerDTO;
import enumeration.ButtonMode;
import service.customer.CustomerService;
import ui.form.CustomerCreateUserForm;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class CustomerPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable customerTable;
	private DefaultTableModel tableModel;
	private JButton createCustomerButton;
	private TableRowSorter<DefaultTableModel> sorter;

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

		filterComboBox = new JComboBox<>(new String[] { "All", "Active", "Inactive" });
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
		String[] columnNames = { "ID", "Name", "Email", "Status", "Action" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 4; // Make only the "Action" column editable
			}
		};
		customerTable = new JTable(tableModel);
		customerTable.setRowHeight(40);

		// Initialize table sorter after creating the table
		sorter = new TableRowSorter<>(tableModel);
		customerTable.setRowSorter(sorter);

		// Apply renderer and editor to the table
		customerTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.EDITDELETE));
		customerTable.getColumnModel().getColumn(4)
				.setCellEditor(new ButtonEditor(customerTable, ButtonMode.EDITDELETE));

		loadCustomers();

		add(new JScrollPane(customerTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchCustomers(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();

		// Validate search term
		if (searchTerm.isEmpty()) {
			loadCustomers();
			return;
		}

		// Clear table
		tableModel.setRowCount(0);

		// Get list of customers
		List<CustomerDTO> customers = CustomerService.readAllCustomer();

		// Search for customers
		for (CustomerDTO customer : customers) {
			if (customer.getName().toLowerCase().contains(searchTerm)) {
				addRow(customer.getId(), customer.getName(), customer.getEmailAddress(), customer.getStatus());
			}
			if (customer.getId().toLowerCase().contains(searchTerm)) {
				addRow(customer.getId(), customer.getName(), customer.getEmailAddress(), customer.getStatus());
			}
		}
	}

	private void filterCustomers(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(filter);
	}

	private void createCustomer(ActionEvent e) {
		new CustomerCreateUserForm().setVisible(true);
	}

	// ======= Filtering Logic =======
	private void filterTable(String filter) {
		sorter.setRowFilter(filter.equals("All") ? null : new RowFilter<DefaultTableModel, Integer>() {
			@Override
			public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
				String status = (String) entry.getValue(3); // Status column index
				return status.equalsIgnoreCase(filter);
			}
		});
	}

	private void addRow(String id, String name, String email, boolean status) {
		tableModel.addRow(new Object[] { id, name, email, status ? "Active" : "Inactive", "Edit" });
	}

	private void loadCustomers() {
		// Clear table
		tableModel.setRowCount(0);

		// Get list of customers
		List<CustomerDTO> customers = CustomerService.readAllCustomer();

		// Add customers to the table
		for (CustomerDTO customer : customers) {
			addRow(customer.getId(), customer.getName(), customer.getEmailAddress(), customer.getStatus());
		}
	}
}
