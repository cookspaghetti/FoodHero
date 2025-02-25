package ui.order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dto.OrderDTO;
import enumeration.ButtonMode;
import enumeration.OrderStatus;
import service.general.SessionControlService;
import service.order.OrderService;
import service.processor.ItemProcessor;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;
import ui.utils.MultiLineRenderer;

public class OrderHistoryPage extends JFrame {
	private JComboBox<OrderStatus> orderStatusComboBox;
	private JComboBox<String> timeRangeComboBox;
	private JTable orderTable;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public OrderHistoryPage() {
		setTitle("Order History");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// First ComboBox - OrderStatus
		JPanel filterPanel = new JPanel();
        orderStatusComboBox = new JComboBox<>(OrderStatus.values());
		orderStatusComboBox.addActionListener(this::filterByOrderStatus);
        filterPanel.add(new JLabel("Filter by Order Status:"));
        filterPanel.add(orderStatusComboBox);

        // Second ComboBox - Time Range
        filterPanel.add(new JLabel("Filter by Time Range:"));
        String[] timeRanges = { "Today", "This Week", "This Month", "6 Months", "This Year" };
        timeRangeComboBox = new JComboBox<>(timeRanges);
		timeRangeComboBox.addActionListener(this::filterByTimeRange);
        filterPanel.add(timeRangeComboBox);
        
        add(filterPanel, BorderLayout.NORTH);
		
		// Table Setup
		String[] columnNames = {"Order Id", "Placement Time", "Completed Time", "Items", "Amount", "Status", "Actions"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6; // Make only the "Action" column editable
			}
		};
		orderTable = new JTable(tableModel);
		tableScrollPane = new JScrollPane(orderTable);
		add(tableScrollPane, BorderLayout.CENTER);
		orderTable.setRowHeight(40);

		// Set column widths
		orderTable.getColumnModel().getColumn(0).setPreferredWidth(90);  // Id
		orderTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Time
		orderTable.getColumnModel().getColumn(2).setPreferredWidth(140); // Time
		orderTable.getColumnModel().getColumn(3).setPreferredWidth(280); // Items (maximized)
		orderTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Status
		orderTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Actions (fit one button)

		// Apply renderer and editor to the table
		orderTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer(ButtonMode.VIEW));
		orderTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(orderTable, ButtonMode.VIEW));

		// Apply multi line renderer to the table
		orderTable.getColumnModel().getColumn(3).setCellRenderer(new MultiLineRenderer());

		loadOrders();

		setVisible(true);
	}

	public void addRow(Object[] rowData) {
		tableModel.addRow(rowData);
	}

	// Load orders from the database
	private void loadOrders() {
		List<OrderDTO> orders = OrderService.readVendorOrders(SessionControlService.getUser().getId());
		orders.sort((o1, o2) -> o2.getPlacementTime().compareTo(o1.getPlacementTime()));
		for (OrderDTO order : orders) {
			Object[] rowData = {
				order.getId(),
				order.getPlacementTime().format(formatter),
				order.getCompletionTime() == null ? "" : order.getCompletionTime().format(formatter),
				processItemList(order.getVendorId(), order.getItems()),
				order.getTotalAmount(),
				order.getStatus(),
				"View History"
			};
			addRow(rowData);
		}
	}

	// Filter orders by order status
	private void filterByOrderStatus(ActionEvent e) {
		OrderStatus selectedStatus = (OrderStatus) orderStatusComboBox.getSelectedItem();
		tableModel.setRowCount(0);
		List<OrderDTO> orders = OrderService.readVendorOrders(SessionControlService.getUser().getId());
		orders.sort((o1, o2) -> o2.getPlacementTime().compareTo(o1.getPlacementTime()));
		for (OrderDTO order : orders) {
			if (order.getStatus() == selectedStatus) {
				Object[] rowData = {
					order.getId(),
					order.getPlacementTime().format(formatter),
					order.getCompletionTime() == null ? "" : order.getCompletionTime().format(formatter),
					processItemList(order.getVendorId(), order.getItems()),
					order.getTotalAmount(),
					order.getStatus(),
					"View History"
				};
				addRow(rowData);
			}
		}
	}

	// Filter orders by time range
	private void filterByTimeRange(ActionEvent e) {
		String selectedTimeRange = (String) timeRangeComboBox.getSelectedItem();
		tableModel.setRowCount(0);
		List<OrderDTO> orders = OrderService.readVendorOrders(SessionControlService.getUser().getId());
		orders.sort((o1, o2) -> o2.getPlacementTime().compareTo(o1.getPlacementTime()));
		for (OrderDTO order : orders) {
			if (isWithinTimeRange(order.getPlacementTime(), selectedTimeRange)) {
				Object[] rowData = {
					order.getId(),
					order.getPlacementTime().format(formatter),
					order.getCompletionTime() == null ? "" : order.getCompletionTime().format(formatter),
					processItemList(order.getVendorId(), order.getItems()),
					order.getTotalAmount(),
					order.getStatus(),
					"View History"
				};
				addRow(rowData);
			}
		}
	}

	// Check if the order is within the selected time range
	private boolean isWithinTimeRange(LocalDateTime placementTime, String selectedTimeRange) {
		LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        switch (selectedTimeRange) {
            case "Today":
                return placementTime.toLocalDate().isEqual(today);

            case "This Week":
                LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                return !placementTime.toLocalDate().isBefore(startOfWeek);

            case "This Month":
                LocalDate startOfMonth = today.withDayOfMonth(1);
                return !placementTime.toLocalDate().isBefore(startOfMonth);

            case "6 Months":
                LocalDate sixMonthsAgo = today.minusMonths(6);
                return !placementTime.toLocalDate().isBefore(sixMonthsAgo);

            case "This Year":
                LocalDate startOfYear = today.withDayOfYear(1);
                return !placementTime.toLocalDate().isBefore(startOfYear);

            default:
                return false; // If the range is not recognized
		}
	}

	// Process item list to display in the table
	private String processItemList(String vendorId, HashMap<String, Integer> items) {
		try{
			CompletableFuture<String> itemFuture = ItemProcessor.processItemListAsync(vendorId, items);
			return itemFuture.join();
		} catch (Exception e) {
			System.err.println("Error processing item list: " + e.getMessage());
			return "Error";
		}
	}
}