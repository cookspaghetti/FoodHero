package ui.utils;

import java.awt.Component;
import java.awt.FlowLayout;
import java.time.LocalDateTime;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

import dto.ItemDTO;
import dto.NotificationDTO;
import enumeration.ButtonMode;
import enumeration.ResponseCode;
import enumeration.Role;
import enumeration.ServiceType;
import service.admin.AdminService;
import service.complaint.ComplaintService;
import service.customer.CustomerService;
import service.general.SessionControlService;
import service.item.ItemService;
import service.manager.ManagerService;
import service.notification.NotificationService;
import service.order.OrderService;
import service.runner.RunnerService;
import service.task.TaskService;
import service.vendor.VendorService;
import ui.form.AdminDetailsForm;
import ui.form.CustomerComplaintDetailsForm;
import ui.form.CustomerDetailsForm;
import ui.form.CustomerOrderDetailsForm;
import ui.form.CustomerOrderHistoryForm;
import ui.form.ManagerComplaintDetailsForm;
import ui.form.ManagerDetailsForm;
import ui.form.RunnerDetailsForm;
import ui.form.RunnerTaskDetailsForm;
import ui.form.VendorDetailsForm;
import ui.form.VendorItemDetailsForm;
import ui.form.VendorOrderDetailsForm;
import ui.form.VendorOrderHistoryForm;
import ui.review.RunnerReviewPage;
import ui.vendor.VendorMenuPage;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
	private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 6));
	private final JButton editButton = new JButton("Edit");
	private final JButton deleteButton = new JButton("Delete");
	private final JButton disableButton = new JButton("Disable");
	private final JButton updateButton = new JButton("Update");
	private final JButton viewButton = new JButton("View");
	private final JButton addToCartButton = new JButton("Add to Cart");
	private String itemId;
	private String vendorId; // specifically for itemPage
	private String itemName; // specifically for cartEditDelete
	private String fourthColumn; // specifically for itemPage
	private ServiceType type;

	public ButtonEditor(JTable table, ButtonMode mode) {

		if (mode == ButtonMode.EDIT) {
			editButton.addActionListener(e -> {
				System.out.println("Edit clicked for Admin ID: " + itemId);
				handleEditAction();
			});

			panel.add(editButton);
		} else if (mode == ButtonMode.DELETE) {
			deleteButton.addActionListener(e -> {
				System.out.println("Delete clicked for Admin ID: " + itemId);
				handleDeleteAction();
			});

			panel.add(deleteButton);
		} else if (mode == ButtonMode.EDITDELETE) {
			editButton.addActionListener(e -> {
				System.out.println("Edit clicked for Admin ID: " + itemId);
				handleEditAction();
			});

			deleteButton.addActionListener(e -> {
				System.out.println("Delete clicked for Admin ID: " + itemId);
				handleDeleteAction();
			});

			panel.add(editButton);
			panel.add(deleteButton);
		} else if (mode == ButtonMode.DISABLEDELETE) {
			disableButton.addActionListener(e -> {
				System.out.println("Disable clicked for Admin ID: " + itemId);
				handleDisableAction();
			});

			deleteButton.addActionListener(e -> {
				System.out.println("Delete clicked for Admin ID: " + itemId);
				handleDeleteAction();
			});

			panel.add(disableButton);
			panel.add(deleteButton);
		} else if (mode == ButtonMode.UPDATE) {
			updateButton.addActionListener(e -> {
				System.out.println("Update clicked for Admin ID: " + itemId);
				handleUpdateAction();
			});

			panel.add(updateButton);
		} else if (mode == ButtonMode.VIEW) {
			viewButton.addActionListener(e -> {
				System.out.println("View clicked for Admin ID: " + itemId);
				handleViewAction();
			});

			panel.add(viewButton);
		} else if (mode == ButtonMode.VIEWORDER) {
			viewButton.addActionListener(e -> {
				System.out.println("View clicked for Order ID: " + itemId);
				handleViewOrderAction();
			});

			panel.add(viewButton);
		} else if (mode == ButtonMode.VIEWORDERHISTORY) {
			viewButton.addActionListener(e -> {
				System.out.println("View clicked for Order ID: " + itemId);
				handleViewOrderHistoryAction();
			});

			panel.add(viewButton);
		} else if (mode == ButtonMode.ADDTOCART) {
			addToCartButton.addActionListener(e -> {
				handleAddToCartAction();
			});

			panel.add(addToCartButton);
		} else if (mode == ButtonMode.CARTEDITDELETE) {
			editButton.addActionListener(e -> {
				System.out.println("Edit clicked for Admin ID: " + itemId);
				handleEditCartAction();
			});

			deleteButton.addActionListener(e -> {
				System.out.println("Delete clicked for Admin ID: " + itemId);
				handleDeleteCartAction();
			});

			panel.add(editButton);
			panel.add(deleteButton);
		}

	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		itemId = table.getValueAt(row, 0).toString(); // Get ID from the first column
		vendorId = table.getValueAt(row, 3).toString(); // Get Vendor ID from the fourth column in itemPage, only for itemPage

		// Cart item details
		if (table.getColumnCount() > 1) {
			itemName = table.getValueAt(row, 1).toString(); // Get Item Name from the second column
		}

		// Item Management
		if (table.getColumnCount() > 5) {
			fourthColumn = table.getValueAt(row, 4).toString(); // Get Vendor ID from the fourth column
		}

		type = getDataTypeFromId(itemId);
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	private void handleEditAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot edit.");
			return;
		}

		switch (type) {
			case ADMIN -> new AdminDetailsForm(AdminService.readAdmin(itemId)).setVisible(true);
			case MANAGER -> new ManagerDetailsForm(ManagerService.readManager(itemId)).setVisible(true);
			case CUSTOMER -> new CustomerDetailsForm(CustomerService.readCustomer(itemId)).setVisible(true);
			case VENDOR -> new VendorDetailsForm(VendorService.readVendor(itemId)).setVisible(true);
			case RUNNER -> new RunnerDetailsForm(RunnerService.readRunner(itemId)).setVisible(true);
			case ITEM -> new VendorItemDetailsForm(ItemService.readItem(vendorId, itemId)).setVisible(true);
			default -> JOptionPane.showMessageDialog(null, "Invalid type for editing.");
		}
	}

	private void handleDeleteAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot delete.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?",
				"Confirm Delete", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

		switch (type) {
			case ADMIN -> AdminService.deleteAdmin(itemId);
			case MANAGER -> ManagerService.deleteManager(itemId);
			case CUSTOMER -> CustomerService.deleteCustomer(itemId);
			case VENDOR -> VendorService.deleteVendor(itemId);
			case RUNNER -> RunnerService.deleteRunner(itemId);
			case ITEM -> ItemService.deleteItem(vendorId, itemId);
			default -> JOptionPane.showMessageDialog(null, "Invalid type for deletion.");
		}
	}

	private void handleDisableAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot delete.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to disable this item?",
				"Confirm Disable", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

		// Update the item availability
		ItemDTO item = ItemService.readItem(fourthColumn, itemId);
		item.setAvailability(false);
		ResponseCode response = ItemService.updateItem(item);
		if (response != ResponseCode.SUCCESS) {
			JOptionPane.showMessageDialog(null, "Failed to disable item.", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("Failed to disable item. " + response);
			return;
		}

		// Send notification to vendor
		NotificationDTO notification = new NotificationDTO();
		notification.setUserId(fourthColumn);
		notification.setTimestamp(LocalDateTime.now());
		notification.setTitle("Item Disabled");
		notification.setMessage("Your item '" + item.getName() + "' has been disabled.");
		notification.setRead(false);
		response = NotificationService.createNotification(notification);
		if (response == ResponseCode.SUCCESS) {
			JOptionPane.showMessageDialog(null, "Item disabled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Failed to send notification.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void handleUpdateAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot update.");
			return;
		}
		System.out.println("Update clicked for ID: " + itemId);
		switch (type) {
			case ORDER -> new VendorOrderDetailsForm(OrderService.readOrder(itemId)).setVisible(true);
			default -> JOptionPane.showMessageDialog(null, "Invalid type for updating.");
		}
	}

	private void handleViewAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot view.");
			return;
		}

		switch (type) {
			case RUNNER -> new RunnerReviewPage(itemId).setVisible(true);
			case ORDER -> new VendorOrderHistoryForm(OrderService.readOrder(itemId)).setVisible(true);
			case COMPLAIN -> {
				if (SessionControlService.getRole() == Role.CUSTOMER) {
					new CustomerComplaintDetailsForm(ComplaintService.readComplaint(itemId)).setVisible(true);
				} else {
					new ManagerComplaintDetailsForm(ComplaintService.readComplaint(itemId)).setVisible(true);
				}
			}
			case VENDOR -> new VendorMenuPage(itemId).setVisible(true);
			case TASK -> new RunnerTaskDetailsForm(TaskService.readTask(itemId)).setVisible(true);
			default -> JOptionPane.showMessageDialog(null, "Invalid type for viewing.");
		}
	}

	private void handleViewOrderAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot view.");
			return;
		}

		new CustomerOrderDetailsForm(OrderService.readOrder(itemId)).setVisible(true);
	}

	private void handleViewOrderHistoryAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot view.");
			return;
		}

		new CustomerOrderHistoryForm(OrderService.readOrder(itemId)).setVisible(true);
	}

	private void handleAddToCartAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot add to cart.");
			return;
		}

		// Add item to cart
		SessionControlService.addToCart(itemId, 1);
		JOptionPane.showMessageDialog(null, "Item added to cart.");
	}

	private void handleEditCartAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot edit.");
			return;
		}

		// Create panel to hold components
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		// Item Name Label
		JLabel nameLabel = new JLabel("Item: " + itemName);
		panel.add(nameLabel);

		Integer itemQuantity = SessionControlService.getCartItems().get(itemId);
		if (itemQuantity == null) {
			JOptionPane.showMessageDialog(null, "Item not found in cart.");
			return;
		}

		// Quantity Selector
		SpinnerNumberModel model = new SpinnerNumberModel(itemQuantity.intValue(), 1, 100, 1);
		JSpinner quantitySpinner = new JSpinner(model);
		panel.add(quantitySpinner);

		// Show the dialog
		int option = JOptionPane.showConfirmDialog(null, panel, "Edit Cart Item", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			int newQuantity = (int) quantitySpinner.getValue();

			// Update cart logic
			SessionControlService.updateCart(itemId, newQuantity);
			JOptionPane.showMessageDialog(null, "Cart updated: " + itemName + " x" + newQuantity);
		}
	}

	private void handleDeleteCartAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot delete.");
			return;
		}

		// Delete cart item
		SessionControlService.removeFromCart(itemId);
		JOptionPane.showMessageDialog(null, "Item removed from cart.");
	}

	private ServiceType getDataTypeFromId(String id) {
		if (id.length() < 3)
			return null; // Invalid ID format

		String prefix = id.substring(0, 3).toUpperCase();
		return switch (prefix) {
			case "ADM" -> ServiceType.ADMIN;
			case "MGR" -> ServiceType.MANAGER;
			case "CUS" -> ServiceType.CUSTOMER;
			case "VDR" -> ServiceType.VENDOR;
			case "RUN" -> ServiceType.RUNNER;
			case "ORD" -> ServiceType.ORDER;
			case "ITM" -> ServiceType.ITEM;
			case "TAK" -> ServiceType.TASK;
			case "COM" -> ServiceType.COMPLAIN;
			case "NOT" -> ServiceType.NOTIFICATION;
			default -> null; // Unknown prefix
		};
	}
}
