package ui.dialog;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import enumeration.PaymentMethod;

public class PaymentDialog extends JDialog {
    public PaymentDialog(JFrame parent, String customerName, String customerPhone) {
        super(parent, "Select Payment Method", true);
        setSize(300, 200);
        getContentPane().setLayout(new GridLayout(4, 1, 5, 5));
        setLocationRelativeTo(parent);

        JLabel nameLabel = new JLabel("  Name: " + customerName);
        JLabel phoneLabel = new JLabel("  Phone: " + customerPhone);
        JComboBox<PaymentMethod> paymentMethodComboBox = new JComboBox<>(PaymentMethod.values());
        JButton confirmButton = new JButton("Confirm");

        getContentPane().add(nameLabel);
        getContentPane().add(phoneLabel);
        getContentPane().add(paymentMethodComboBox);
        getContentPane().add(confirmButton);

        confirmButton.addActionListener(e -> dispose());

        setVisible(true);
    }
}
