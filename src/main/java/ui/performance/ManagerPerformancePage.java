package ui.performance;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import enumeration.ButtonMode;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class ManagerPerformancePage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> ratingFilter;
	private JComboBox<String> sortOptions;
	private JTable runnerTable;
	private JScrollPane tableScrollPane;
	private DefaultTableModel tableModel;

	public ManagerPerformancePage() {
		setTitle("Runner Performance");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Search Panel
		JPanel searchPanel = new JPanel();
		searchField = new JTextField(20);
		searchButton = new JButton("Search");
		ratingFilter = new JComboBox<>(new String[]{"All Ratings", "1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars"});
		sortOptions = new JComboBox<>(new String[]{"Ascending", "Descending"});

		searchPanel.add(new JLabel("Search Runner:"));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.add(new JLabel("Filter by Rating:"));
		searchPanel.add(ratingFilter);
		searchPanel.add(new JLabel("Sort by Rating:"));
		searchPanel.add(sortOptions);

		add(searchPanel, BorderLayout.NORTH);

		// Table Panel
		String[] columnNames = {"Runner ID", "Name", "Phone Number", "Rating", "Actions"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 4; // Make only the "Action" column editable
			}
		};
		runnerTable = new JTable(tableModel);
		tableScrollPane = new JScrollPane(runnerTable);
		add(tableScrollPane, BorderLayout.CENTER);
		runnerTable.setRowHeight(40);

		// Apply renderer and editor to the table
		runnerTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.VIEW));
		runnerTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(runnerTable, ButtonMode.VIEW));

		// Set column widths
		TableColumn idColumn = runnerTable.getColumn("Runner ID");
		idColumn.setPreferredWidth(100);

		TableColumn nameColumn = runnerTable.getColumn("Name");
		nameColumn.setPreferredWidth(250); // Maximize name column width

		TableColumn phoneColumn = runnerTable.getColumn("Phone Number");
		phoneColumn.setPreferredWidth(150);

		TableColumn ratingColumn = runnerTable.getColumn("Rating");
		ratingColumn.setPreferredWidth(100);

		addRow("1", "John Doe", "12345678", "5");

		setVisible(true);
	}

	public void addRow(String id, String name, String phone, String rating) {
		tableModel.addRow(new Object[]{id, name, phone, rating});
	}

	public static void main(String[] args) {
		new ManagerPerformancePage();
	}
}

