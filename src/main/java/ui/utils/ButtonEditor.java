package ui.utils;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import enumeration.ButtonMode;
import enumeration.Role;
import enumeration.ServiceType;
import service.admin.AdminService;
import service.complain.ComplaintService;
import service.customer.CustomerService;
import service.general.SessionControlService;
import service.item.ItemService;
import service.manager.ManagerService;
import service.order.OrderService;
import service.runner.RunnerService;
import service.vendor.VendorService;
import ui.form.AdminDetailsForm;
import ui.form.CustomerComplaintDetailsForm;
import ui.form.CustomerDetailsForm;
import ui.form.CustomerOrderDetailsForm;
import ui.form.CustomerOrderHistoryForm;
import ui.form.ManagerComplaintDetailsForm;
import ui.form.ManagerDetailsForm;
import ui.form.RunnerDetailsForm;
import ui.form.VendorDetailsForm;
import ui.form.VendorItemDetailsForm;
import ui.form.VendorOrderDetailsForm;
import ui.form.VendorOrderHistoryForm;
import ui.review.RunnerReviewPage;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
	private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 6));
	private final JButton editButton = new JButton("Edit");
	private final JButton deleteButton = new JButton("Delete");
    private final JButton disableButton = new JButton("Disable");
	private final JButton updateButton = new JButton("Update");
	private final JButton viewButton = new JButton("View");
	private String itemId;
	private String vendorId; // specifically for itemPage
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
		}

	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		itemId = table.getValueAt(row, 0).toString(); // Get ID from the first column
		vendorId = table.getValueAt(row, 3).toString(); // Get Vendor ID from the fourth column in itemPage, only used for item
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
//		case TASK -> TaskService.editTask(itemId);
//		case COMPLAIN -> ComplainService.editComplain(itemId);
//		case NOTIFICATION -> NotificationService.editNotification(itemId);
		default -> JOptionPane.showMessageDialog(null, "Invalid type for editing.");
		}
	}

	private void handleDeleteAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot delete.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
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
//		case TASK -> TaskService.deleteTask(itemId);
//		case COMPLAIN -> ComplainService.deleteComplain(itemId);
//		case NOTIFICATION -> NotificationService.deleteNotification(itemId);
		default -> JOptionPane.showMessageDialog(null, "Invalid type for deletion.");
		}
	}
	
	private void handleDisableAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot delete.");
			return;
		}
		
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to disable this item?", "Confirm Disable", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}
		
		// Update the item availability
		
		// Send notification to vendor
	}
	
	private void handleUpdateAction() {
		if (type == null) {
			JOptionPane.showMessageDialog(null, "Unknown type. Cannot update.");
			return;
		}
		
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

	private ServiceType getDataTypeFromId(String id) {
		if (id.length() < 3) return null; // Invalid ID format

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
