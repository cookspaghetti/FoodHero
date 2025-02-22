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

import dto.RunnerDTO;
import enumeration.ButtonMode;
import service.runner.RunnerService;
import ui.form.RunnerCreateUserForm;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class RunnerPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> filterComboBox;
	private JTable runnerTable;
	private DefaultTableModel tableModel;
	private JButton createRunnerButton;

	public RunnerPage() {
		setTitle("Runner Management");
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
		searchButton.addActionListener(this::searchRunners);

		filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
		filterComboBox.addActionListener(this::filterRunners);

		topPanel.add(new JLabel("Search:"));
		topPanel.add(searchField);
		topPanel.add(searchButton);
		topPanel.add(new JLabel("Filter:"));
		topPanel.add(filterComboBox);
		
		createRunnerButton = new JButton("New Runner");
		createRunnerButton.addActionListener(this::createRunner);
		topPanel.add(createRunnerButton);

		add(topPanel, BorderLayout.NORTH);

		// ======= Runner Table =======
		String[] columnNames = {"ID", "Name", "Email", "Status", "Action"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 4; // Make only the "Action" column editable
		    }
		};
		runnerTable = new JTable(tableModel);
		runnerTable.setRowHeight(40); 
		
		// Apply renderer and editor to the table
		runnerTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.EDITDELETE));
		runnerTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(runnerTable, ButtonMode.EDITDELETE));

		loadRunners();

		add(new JScrollPane(runnerTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchRunners(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		
		// Validate search term
		if (searchTerm.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a search term", "Search Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Clear table
		tableModel.setRowCount(0);

		// Get list of runners
		List<RunnerDTO> runners = RunnerService.readAllRunner();

		// Search for runner
		for (RunnerDTO runner : runners) {
			if (runner.getName().toLowerCase().contains(searchTerm)) {
				addRow(runner.getId(), runner.getName(), runner.getEmailAddress(), runner.getStatus());
			}
			if (runner.getId().toLowerCase().contains(searchTerm)) {
				addRow(runner.getId(), runner.getName(), runner.getEmailAddress(), runner.getStatus());
			}
		}
	}

	private void filterRunners(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(filter);
	}
	
	private void createRunner(ActionEvent e) {
		new RunnerCreateUserForm().setVisible(true);
		loadRunners();
	}

	// ======= Filtering Logic =======
	private void filterTable(String filter) {
		for (int i = 0; i < runnerTable.getRowCount(); i++) {
			String status = (String) runnerTable.getValueAt(i, 3);
			if (filter.equals("All") || status.equalsIgnoreCase(filter)) {
				runnerTable.setRowHeight(i, 40);
				runnerTable.setValueAt(true, i, 4);
			} else {
				runnerTable.setRowHeight(i, 0);
				runnerTable.setValueAt(false, i, 4);
			}
		}
	}

	// ======= Utility Method =======
	private void addRow(String id, String name, String email, boolean status) {
		tableModel.addRow(new Object[]{id, name, email, status ? "Active" : "Inactive", "Edit"});
	}

	private void loadRunners() {
		tableModel.setRowCount(0);

		List<RunnerDTO> runners = RunnerService.readAllRunner();
		for (RunnerDTO runner : runners) {
			addRow(runner.getId(), runner.getName(), runner.getEmailAddress(), runner.getStatus());
		}
	}

}
