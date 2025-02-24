package ui.vendor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.ItemDTO;
import enumeration.ButtonMode;
import service.item.ItemService;
import ui.review.CustomerVendorReviewPage;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.awt.*;
import java.util.List;

public class VendorMenuPage extends JFrame {

	DefaultTableModel tableModel;

	public VendorMenuPage(String vendorId) {
		setTitle("Vendor Menu");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		// Header Panel
		JPanel headerPanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("Menu", SwingConstants.LEFT);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		JButton reviewButton = new JButton("Reviews");
		reviewButton.addActionListener(e -> {
			new CustomerVendorReviewPage(vendorId);
		});
		JButton cartButton = new JButton("Cart");
		cartButton.addActionListener(e -> {
			new VendorCartPage(vendorId);
		});

		reviewButton.setPreferredSize(new Dimension(100, 30));
		cartButton.setPreferredSize(new Dimension(100, 30));

		// Create a panel for buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
		buttonPanel.add(reviewButton);
		buttonPanel.add(cartButton);

		// Add components to header panel
		headerPanel.add(titleLabel, BorderLayout.WEST);
		headerPanel.add(buttonPanel, BorderLayout.EAST);
		add(headerPanel, BorderLayout.NORTH);

		// Table Model
		String[] columnNames = { "Item ID", "Item Name", "Price", "Description", "Actions" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 4; // Make only the "Action" column editable
			}
		};
		JTable menuTable = new JTable(tableModel);
		menuTable.setRowHeight(40);

		// Apply renderer and editor to the table
		menuTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.ADDTOCART));
		menuTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(menuTable, ButtonMode.ADDTOCART));

		loadVendorMenu(vendorId);

		JScrollPane scrollPane = new JScrollPane(menuTable);
		add(scrollPane, BorderLayout.CENTER);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void loadVendorMenu(String vendorId) {
		List<ItemDTO> items = ItemService.readAllItem(vendorId);
		for (ItemDTO item : items) {
			if (item.isAvailability() == false) {
				continue;
			}
			Object[] data = { item.getId(), item.getName(), item.getPrice(), item.getDescription(), "Add to Cart" };
			tableModel.addRow(data);
		}
	}

}
