package ui.login;

import javax.swing.*;

import dto.AdminDTO;
import enumeration.ResponseCode;
import enumeration.Role;
import service.general.LoginService;
import service.general.SessionControlService;
import ui.dashboard.AdminDashboard;

import java.awt.*;
import java.awt.event.ActionListener;

public class LoginInterface extends JFrame {
	private JTextField emailField;
	private JPasswordField passwordField;
	private Role selectedRole = Role.ADMIN; // Enum Role

	public LoginInterface() {
		setTitle("Login - FoodHero");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Center the window
		getContentPane().setLayout(new BorderLayout());

		// ======= Title =======
		JLabel titleLabel = new JLabel("Welcome to FoodHero!", SwingConstants.CENTER);
		titleLabel.setPreferredSize(new Dimension(110, 80));
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		getContentPane().add(titleLabel, BorderLayout.NORTH);

		// ======= Form Panel =======
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding

		ButtonGroup roleGroup = new ButtonGroup();

		// Role Enum Integration
		ActionListener roleListener = e -> selectedRole = Role.valueOf(e.getActionCommand());

		// ======= Email & Password =======
		JPanel emailPanel = new JPanel();
		GridBagLayout gbl_emailPanel = new GridBagLayout();
		gbl_emailPanel.columnWidths = new int[] {80, 180};
		gbl_emailPanel.rowHeights = new int[] {20, 20};
		gbl_emailPanel.columnWeights = new double[]{0.0, 0.0};
		gbl_emailPanel.rowWeights = new double[]{0.0, 0.0, 0.0};
		emailPanel.setLayout(gbl_emailPanel);

		JRadioButton adminRadio = new JRadioButton("Admin");
		adminRadio.setSelected(true);
		JRadioButton managerRadio = new JRadioButton("Manager");
		JRadioButton customerRadio = new JRadioButton("Customer");
		JRadioButton vendorRadio = new JRadioButton("Vendor");
		JRadioButton runnerRadio = new JRadioButton("Runner");
		roleGroup.add(adminRadio);
		roleGroup.add(managerRadio);
		roleGroup.add(customerRadio);
		roleGroup.add(vendorRadio);
		roleGroup.add(runnerRadio);
		adminRadio.setActionCommand(Role.ADMIN.name());
		managerRadio.setActionCommand(Role.MANAGER.name());
		customerRadio.setActionCommand(Role.CUSTOMER.name());
		vendorRadio.setActionCommand(Role.VENDOR.name());
		runnerRadio.setActionCommand(Role.RUNNER.name());

		adminRadio.addActionListener(roleListener);
		managerRadio.addActionListener(roleListener);
		customerRadio.addActionListener(roleListener);
		vendorRadio.addActionListener(roleListener);
		runnerRadio.addActionListener(roleListener);

		// ======= Role Selection (Horizontal) =======
		JLabel roleLabel = new JLabel("Select Role:");
		roleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		roleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_roleLabel = new GridBagConstraints();
		gbc_roleLabel.anchor = GridBagConstraints.EAST;
		gbc_roleLabel.insets = new Insets(0, 0, 5, 5);
		gbc_roleLabel.gridx = 0;
		gbc_roleLabel.gridy = 0;
		emailPanel.add(roleLabel, gbc_roleLabel);
		roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel rolePanel = new JPanel();
		GridBagConstraints gbc_rolePanel = new GridBagConstraints();
		gbc_rolePanel.insets = new Insets(0, 0, 5, 0);
		gbc_rolePanel.gridx = 1;
		gbc_rolePanel.gridy = 0;
		emailPanel.add(rolePanel, gbc_rolePanel);
		rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.X_AXIS));
		rolePanel.add(adminRadio);
		rolePanel.add(managerRadio);
		rolePanel.add(customerRadio);
		rolePanel.add(vendorRadio);
		rolePanel.add(runnerRadio);
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		emailLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		GridBagConstraints gbc_emailLabel = new GridBagConstraints();
		gbc_emailLabel.anchor = GridBagConstraints.EAST;
		gbc_emailLabel.fill = GridBagConstraints.VERTICAL;
		gbc_emailLabel.insets = new Insets(0, 0, 5, 5);
		gbc_emailLabel.gridx = 0;
		gbc_emailLabel.gridy = 1;
		emailPanel.add(emailLabel, gbc_emailLabel);
		emailField = new JTextField(30);
		GridBagConstraints gbc_emailField = new GridBagConstraints();
		gbc_emailField.anchor = GridBagConstraints.WEST;
		gbc_emailField.insets = new Insets(0, 0, 5, 0);
		gbc_emailField.gridx = 1;
		gbc_emailField.gridy = 1;
		emailPanel.add(emailField, gbc_emailField);
		JLabel passwordLabel = new JLabel("Password:");
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.EAST;
		gbc_passwordLabel.fill = GridBagConstraints.VERTICAL;
		gbc_passwordLabel.insets = new Insets(0, 0, 0, 5);
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 2;
		emailPanel.add(passwordLabel, gbc_passwordLabel);
		formPanel.add(emailPanel);
		passwordField = new JPasswordField(30);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.anchor = GridBagConstraints.WEST;
		gbc_passwordField.fill = GridBagConstraints.VERTICAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 2;
		emailPanel.add(passwordField, gbc_passwordField);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 50));
		formPanel.add(panel);

		// ======= Login Button =======
		JButton loginButton = new JButton("Login");
		panel.add(loginButton);
		loginButton.setPreferredSize(new Dimension(100, 30));
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.addActionListener(e -> performLogin());

		getContentPane().add(formPanel, BorderLayout.CENTER);
	}

	private void performLogin() {
		String email = emailField.getText();
		String password = new String(passwordField.getPassword());
		
		if (selectedRole == null) {
			JOptionPane.showMessageDialog(this, "Please select a role.");
		} else if (email.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter your email.");
		} else if (password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter your password.");
		} else if (!LoginService.validateEmail(email)) {
			JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
		} else {
			System.out.println("Role: " + selectedRole + ", Email: " + email);

			// Authentication logic here
			ResponseCode code = LoginService.login(selectedRole, email, password);

			if (code == ResponseCode.RECORD_NOT_FOUND) {
				JOptionPane.showMessageDialog(this, "Login failed. Please try again.");
			} else if (code == ResponseCode.IO_EXCEPTION) {
				JOptionPane.showMessageDialog(this, "Error reading from file. Please try again.");
			} else if (code == ResponseCode.SUCCESS) {
				JOptionPane.showMessageDialog(this, "Login successful!");

				// Open respective dashboard based on the role
				switch (selectedRole) {
				case ADMIN -> new AdminDashboard((AdminDTO) SessionControlService.getUser()).setVisible(true);
				// case MANAGER -> new ManagerDashboard(user).setVisible(true);
				// case CUSTOMER -> new CustomerDashboard(user).setVisible(true);
				// case VENDOR -> new VendorDashboard(user).setVisible(true);
				// case RUNNER -> new RunnerDashboard(user).setVisible(true);
				default -> JOptionPane.showMessageDialog(this, "Unknown error occurred. Please try again.");
				}

				// Close the login window
				this.dispose();
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginInterface().setVisible(true));
	}
}
