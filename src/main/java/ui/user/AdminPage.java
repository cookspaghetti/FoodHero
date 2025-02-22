package ui.user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.AdminDTO;
import enumeration.ButtonMode;
import service.admin.AdminService;
import ui.form.AdminCreateUserForm;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AdminPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable adminTable;
	private DefaultTableModel tableModel;
	private JButton createAdminButton;

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
		
		createAdminButton = new JButton("New Admin");
		createAdminButton.addActionListener(this::createAdmin);
		topPanel.add(createAdminButton);

		add(topPanel, BorderLayout.NORTH);

		// ======= Admin Table =======
		String[] columnNames = {"ID", "Name", "Email", "Status", "Action"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 4; // Make only the "Action" column editable
		    }
		};
		adminTable = new JTable(tableModel);
		adminTable.setRowHeight(40);
		
		// Apply renderer and editor to the table
		adminTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.EDITDELETE));
		adminTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(adminTable, ButtonMode.EDITDELETE));

		loadAdmins();

		add(new JScrollPane(adminTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchAdmins(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();

		// Validate search term
		if (searchTerm.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a search term", "Search Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Clear table
		tableModel.setRowCount(0);

		// Get list of admins
		List<AdminDTO> admins = AdminService.readAllAdmin();

		// Search for admins
		for (AdminDTO admin : admins) {
			if (admin.getName().toLowerCase().contains(searchTerm)) {
				addRow(admin.getId(), admin.getName(), admin.getEmailAddress(), admin.getStatus());
			}
			if (admin.getId().toLowerCase().contains(searchTerm)) {
				addRow(admin.getId(), admin.getName(), admin.getEmailAddress(), admin.getStatus());
			}
		}
	}

	private void filterAdmins(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(filter);
	}
	
	private void createAdmin(ActionEvent e) {
		new AdminCreateUserForm().setVisible(true);
		loadAdmins();
	}

	// ======= Filtering Logic =======
	private void filterTable(String filter) {
		for (int i = 0; i < adminTable.getRowCount(); i++) {
			String status = (String) adminTable.getValueAt(i, 3);
			if (filter.equals("All") || status.equalsIgnoreCase(filter)) {
				adminTable.setRowHeight(i, 40);
			} else {
				adminTable.setRowHeight(i, 0);
			}
		}
	}

	private void addRow(String id, String name, String email, boolean status) {
		tableModel.addRow(new Object[]{id, name, email, status ? "Active" : "Inactive", "Edit"});
	}

	private void loadAdmins() {
		tableModel.setRowCount(0);
		List<AdminDTO> admins = AdminService.readAllAdmin();
		admins.sort((a1, a2) -> a1.getId().compareTo(a2.getId()));
		for (AdminDTO admin : admins) {
			addRow(admin.getId(), admin.getName(), admin.getEmailAddress(), admin.getStatus());
		}
	}
}
