package ui.performance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import dto.RunnerDTO;
import dto.RunnerReviewDTO;
import enumeration.ButtonMode;
import service.review.ReviewService;
import service.runner.RunnerService;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.util.List;

public class ManagerPerformancePage extends JFrame {
	private TableRowSorter<DefaultTableModel> sorter;

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

		// Initialize table sorter
		sorter = new TableRowSorter<>(tableModel);
		runnerTable.setRowSorter(sorter);

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
			loadRunners();
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
			return "0.0";
		}
		double averageRating = totalRating / reviews.size();
		return String.format("%.1f", averageRating);
	}

	// Filter runners by rating
	private void filterByRating(ActionEvent evt) {
    String filter = ratingFilter.getSelectedItem().toString();
    
    if (filter.equals("All Ratings")) {
        sorter.setRowFilter(null);
        return;
    }

    sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
        @Override
        public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
            try {
                double rating = Double.parseDouble(entry.getValue(3).toString());
                int starRating = (int) Math.floor(rating);
                int filterRating = Integer.parseInt(filter.substring(0, 1));
                return starRating == filterRating;
            } catch (NumberFormatException e) {
                System.err.println("Error parsing rating: " + e.getMessage());
                return false;
            }
        }
    });
}

	// Sort runners by rating
	private void sortByRating(ActionEvent evt) {
		String sort = sortOptions.getSelectedItem().toString();
		if (sort.equals("Ascending")) {
			System.out.println("Sort Ascending");
			sortAscending();
		} else {
			System.out.println("Sort Descending");
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

