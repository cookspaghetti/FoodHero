package ui.dialog;

import java.awt.GridLayout;
import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import dto.AddTransactionDTO;
import dto.CustomerDTO;
import enumeration.PaymentMethod;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.customer.CustomerService;
import service.general.SessionControlService;
import service.transaction.TransactionService;
import service.utils.IdGenerationUtils;

public class TopUpPaymentDialog extends JDialog {

    private JLabel nameLabel;
    private JLabel phoneLabel;
    private JLabel amountLabel;
    private JTextField amountField;
    private JComboBox<PaymentMethod> paymentMethodComboBox;
    private JButton confirmButton;

    public TopUpPaymentDialog(JFrame parent, String customerId, String customerName, String customerPhone) {
        super(parent, "Select Payment Method", true);
        setSize(300, 200);
        setLayout(new GridLayout(4, 2, 5, 5)); // 4 rows (Name, Phone, Amount, Payment)

        setLocationRelativeTo(parent);

        nameLabel = new JLabel("  Name: " + customerName);
        phoneLabel = new JLabel("  Phone: " + customerPhone);
        amountLabel = new JLabel("  Amount: ");
        amountField = new JTextField();
        paymentMethodComboBox = new JComboBox<>(PaymentMethod.values());
        confirmButton = new JButton("Confirm");

        add(nameLabel);
        add(new JLabel());
        add(phoneLabel);
        add(new JLabel());
        add(amountLabel);
        add(amountField);
        add(paymentMethodComboBox);
        add(confirmButton);

        confirmButton.addActionListener(e -> topup((PaymentMethod) paymentMethodComboBox.getSelectedItem(), customerId));

        setVisible(true);
    }

    // Method to handle topup payment
    private void topup(PaymentMethod paymentMethod, String customerId) {
        // Validate amount
        if (amountField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter amount to top up", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create add transaction
        AddTransactionDTO addTransactionDTO = new AddTransactionDTO();
        addTransactionDTO.setId(IdGenerationUtils.getNextId(ServiceType.ADD_TRANSACTION, null, null));
        addTransactionDTO.setCustomerId(customerId);
        addTransactionDTO.setAmount(Double.parseDouble(amountField.getText()));
        addTransactionDTO.setDate(LocalDateTime.now());
        addTransactionDTO.setDescription("Top Up");
        addTransactionDTO.setAdminId(SessionControlService.getUser().getId());
        addTransactionDTO.setPaymentMethod(paymentMethod);

        // Save transaction
        ResponseCode topup = TransactionService.createAddTransaction(addTransactionDTO);
        if (topup != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Failed to top up", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update customer balance
        CustomerDTO customer = CustomerService.readCustomer(customerId);
        customer.setCredit(customer.getCredit() + addTransactionDTO.getAmount());
        ResponseCode updateCustomer = CustomerService.updateCustomer(customer);
        if (updateCustomer != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Failed to update customer balance", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(this, "Top up successful", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        dispose();
    }

}
