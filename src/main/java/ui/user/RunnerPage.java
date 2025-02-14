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

		// Example data (replace with actual data)
		addRunnerRow("RUN00001", "Alex Tho", "alex@example.com", "Active");
		addRunnerRow("RUN00001", "Jane Doe", "jane@example.com", "Inactive");

		add(new JScrollPane(runnerTable), BorderLayout.CENTER);
	}

	// ======= Action Methods =======
	private void searchRunners(ActionEvent e) {
		String searchTerm = searchField.getText().trim().toLowerCase();
		filterTable(searchTerm, (String) filterComboBox.getSelectedItem());
	}

	private void filterRunners(ActionEvent e) {
		String filter = (String) filterComboBox.getSelectedItem();
		filterTable(searchField.getText().trim().toLowerCase(), filter);
	}
	
	private void createRunner(ActionEvent e) {
		JOptionPane.showMessageDialog(this, "Add Manager button clicked.");
	}

	// ======= Filtering Logic =======
	private void filterTable(String searchTerm, String filter) {
		for (int i = 0; i < runnerTable.getRowCount(); i++) {
			String name = runnerTable.getValueAt(i, 1).toString().toLowerCase();
			String email = runnerTable.getValueAt(i, 2).toString().toLowerCase();
			String status = runnerTable.getValueAt(i, 3).toString();

			boolean matchesSearch = name.contains(searchTerm) || email.contains(searchTerm);
			boolean matchesFilter = filter.equals("All") || status.equals(filter);

			runnerTable.setRowHeight(i, (matchesSearch && matchesFilter) ? 20 : 0); // Hide unmatched rows
		}
	}

	// ======= Utility Method =======
	private void addRunnerRow(String id, String name, String email, String status) {
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
		SwingUtilities.invokeLater(() -> new RunnerPage().setVisible(true));
	}
}
