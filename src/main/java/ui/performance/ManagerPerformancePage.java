package ui.performance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import dto.RunnerDTO;
import dto.RunnerReviewDTO;
import enumeration.ButtonMode;
import service.review.ReviewService;
import service.runner.RunnerService;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.util.List;

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
		searchButton.addActionListener(this::searchRunner);
		ratingFilter = new JComboBox<>(new String[]{"All Ratings", "1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars"});
		ratingFilter.addActionListener(this::filterByRating);
		sortOptions = new JComboBox<>(new String[]{"Ascending", "Descending"});
		sortOptions.addActionListener(this::sortByRating);

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

		loadRunners();

		setVisible(true);
	}

	public void addRow(String id, String name, String phone, String rating) {
		tableModel.addRow(new Object[]{id, name, phone, rating});
	}

	// Load all runners
	private void loadRunners() {
		tableModel.setRowCount(0);
		List<RunnerDTO> runners = RunnerService.readAllRunner();
		for (RunnerDTO runner : runners) {
			addRow(runner.getId(), runner.getName(), runner.getPhoneNumber(), getRunnerRating(runner.getId()));
		}
	}

	// Search for a runner
	private void searchRunner(ActionEvent evt) {
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
				addRow(runner.getId(), runner.getName(), runner.getPhoneNumber(), getRunnerRating(runner.getId()));
			}
			if (runner.getId().toLowerCase().contains(searchTerm)) {
				addRow(runner.getId(), runner.getName(), runner.getPhoneNumber(), getRunnerRating(runner.getId()));
			}
		}
	}

	// Get runner rating
	private String getRunnerRating(String runnerId) {
		List<RunnerReviewDTO> reviews = ReviewService.readAllRunnerReview(runnerId);
		double totalRating = 0;
		for (RunnerReviewDTO review : reviews) {
			totalRating += review.getRating();
		}
		if (reviews.size() == 0) {
			return "N/A";
		}
		double averageRating = totalRating / reviews.size();
		return String.format("%.1f", averageRating);
	}

	// Filter runners by rating
	private void filterByRating(ActionEvent evt) {
		String filter = ratingFilter.getSelectedItem().toString();
		if (filter.equals("All Ratings")) {
			loadRunners();
			return;
		}

		tableModel.setRowCount(0);
		List<RunnerDTO> runners = RunnerService.readAllRunner();
		for (RunnerDTO runner : runners) {
			String rating = getRunnerRating(runner.getId());
			if (rating.equals(filter.substring(0, 1))) {
				addRow(runner.getId(), runner.getName(), runner.getPhoneNumber(), rating);
			}
		}
	}

	// Sort runners by rating
	private void sortByRating(ActionEvent evt) {
		String sort = sortOptions.getSelectedItem().toString();
		if (sort.equals("Ascending")) {
			sortAscending();
		} else {
			sortDescending();
		}
	}

	// Sort runners in ascending order
	private void sortAscending() {
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			for (int j = i + 1; j < tableModel.getRowCount(); j++) {
				if (Double.parseDouble(tableModel.getValueAt(i, 3).toString()) > Double.parseDouble(tableModel.getValueAt(j, 3).toString())) {
					String tempId = tableModel.getValueAt(i, 0).toString();
					String tempName = tableModel.getValueAt(i, 1).toString();
					String tempPhone = tableModel.getValueAt(i, 2).toString();
					String tempRating = tableModel.getValueAt(i, 3).toString();

					tableModel.setValueAt(tableModel.getValueAt(j, 0), i, 0);
					tableModel.setValueAt(tableModel.getValueAt(j, 1), i, 1);
					tableModel.setValueAt(tableModel.getValueAt(j, 2), i, 2);
					tableModel.setValueAt(tableModel.getValueAt(j, 3), i, 3);

					tableModel.setValueAt(tempId, j, 0);
					tableModel.setValueAt(tempName, j, 1);
					tableModel.setValueAt(tempPhone, j, 2);
					tableModel.setValueAt(tempRating, j, 3);
				}
			}
		}
	}

	// Sort runners in descending order
	private void sortDescending() {
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			for (int j = i + 1; j < tableModel.getRowCount(); j++) {
				if (Double.parseDouble(tableModel.getValueAt(i, 3).toString()) < Double.parseDouble(tableModel.getValueAt(j, 3).toString())) {
					String tempId = tableModel.getValueAt(i, 0).toString();
					String tempName = tableModel.getValueAt(i, 1).toString();
					String tempPhone = tableModel.getValueAt(i, 2).toString();
					String tempRating = tableModel.getValueAt(i, 3).toString();

					tableModel.setValueAt(tableModel.getValueAt(j, 0), i, 0);
					tableModel.setValueAt(tableModel.getValueAt(j, 1), i, 1);
					tableModel.setValueAt(tableModel.getValueAt(j, 2), i, 2);
					tableModel.setValueAt(tableModel.getValueAt(j, 3), i, 3);

					tableModel.setValueAt(tempId, j, 0);
					tableModel.setValueAt(tempName, j, 1);
					tableModel.setValueAt(tempPhone, j, 2);
					tableModel.setValueAt(tempRating, j, 3);
				}
			}
		}
	}
}

