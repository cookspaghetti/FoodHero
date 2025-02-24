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

import dto.ManagerDTO;
import enumeration.ButtonMode;
import service.manager.ManagerService;
import ui.form.ManagerCreateUserForm;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class ManagerPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable managerTable;
	private DefaultTableModel tableModel;
	private JButton createManagerButton;
	private TableRowSorter<DefaultTableModel> sorter;

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

		// Initialize table sorter after creating the table
    	sorter = new TableRowSorter<>(tableModel);
    	managerTable.setRowSorter(sorter);

		// Apply renderer and editor to the table
		managerTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.EDITDELETE));
		managerTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(managerTable, ButtonMode.EDITDELETE));

		loadManagers();

		getContentPane().add(new JScrollPane(managerTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchManagers(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		
		// Validate search term
		if (searchTerm.isEmpty()) {
			loadManagers();
			return;
		}

		// Clear table
		tableModel.setRowCount(0);

		// Get list of managers
		List<ManagerDTO> managers = ManagerService.readAllManager();

		// Search for manager
		for (ManagerDTO manager : managers) {
			if (manager.getName().toLowerCase().contains(searchTerm)) {
				addRow(manager.getId(), manager.getName(), manager.getEmailAddress(), manager.getStatus());
			}
			if (manager.getId().toLowerCase().contains(searchTerm)) {
				addRow(manager.getId(), manager.getName(), manager.getEmailAddress(), manager.getStatus());
			}
		}
	}

	private void filterManagers(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(filter);
	}
	
	private void createManager(ActionEvent e) {
		ManagerCreateUserForm form = new ManagerCreateUserForm();
		form.setVisible(true);
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

	// ======= Utility Method =======
	private void addRow(String id, String name, String email, boolean status) {
		tableModel.addRow(new Object[]{id, name, email, status ? "Active" : "Inactive", "Edit"});
	}

	private void loadManagers() {
		// Clear table
		tableModel.setRowCount(0);
		List<ManagerDTO> managers = ManagerService.readAllManager();
		for (ManagerDTO manager : managers) {
			addRow(manager.getId(), manager.getName(), manager.getEmailAddress(), manager.getStatus());
		}
	}
}
