package ui.utils;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import enumeration.ButtonMode;

public class ButtonRenderer extends JPanel implements TableCellRenderer {
	private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton disableButton = new JButton("Disable");
    private final JButton updateButton = new JButton("Update");
    private final JButton viewButton = new JButton("View");
    private final JButton addToCartButton = new JButton("Add to Cart");

    public ButtonRenderer(ButtonMode mode) {
    	if (mode == ButtonMode.EDIT) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(editButton);
    	} else if (mode == ButtonMode.DELETE) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(deleteButton);
    	} else if (mode == ButtonMode.EDITDELETE) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(editButton);
            add(deleteButton);
    	} else if (mode == ButtonMode.DISABLEDELETE) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(disableButton);
            add(deleteButton);
    	} else if (mode ==  ButtonMode.UPDATE) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(updateButton);
    	} else if (mode == ButtonMode.VIEW) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(viewButton);
    	} else if (mode == ButtonMode.VIEWORDER) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(viewButton);
    	} else if (mode == ButtonMode.VIEWORDERHISTORY) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(viewButton);
    	} else if (mode == ButtonMode.ADDTOCART) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(addToCartButton);
    	} else if (mode == ButtonMode.CARTEDITDELETE) {
    		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 6));
            add(editButton);
            add(deleteButton);
    	}
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
