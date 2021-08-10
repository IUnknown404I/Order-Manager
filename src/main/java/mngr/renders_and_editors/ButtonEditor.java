package mngr.renders_and_editors;

import mngr.TableMethods;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 * The class for creating buttons in JTable
 * @author MrUnknown404
 */
public class ButtonEditor extends DefaultCellEditor {
    private String label;
    private String pathString;
    private JButton button;
    private boolean isPushed;
    private final Color inactiveColor = Color.LIGHT_GRAY;

    /**
     * Generator of buttons for JTable
     * @param checkBox new JCcheckBox
     */
    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        
        button = new JButton();
//        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(inactiveColor);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        
        if (table.getName().equals("Archieve"))
            pathString = "АРХИВ\\" + (String)table.getValueAt(row, 0) +" "+ TableMethods.toValidNameFile((String)table.getValueAt(row, 1));
        else
            pathString = "Текущие заказы\\" + (String)table.getValueAt(row, 0) +" "+ TableMethods.toValidNameFile((String)table.getValueAt(row, 2));
        label = (value == null) ? "" : value.toString();
        
        isPushed = true;
        
        if (isPushed)
            button.setBackground(inactiveColor);
        
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        Runnable action = () -> {
            if (pathString != null) {
                try {
                    Path realPath = Path.of(TableMethods.getRootPath().toString(), pathString);
                    
                    if(Files.exists(realPath))
                        Runtime.getRuntime().exec("explorer.exe /open, " + realPath);
                } catch (IOException ex) {
                    Logger.getLogger(ButtonEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        if (isPushed) 
            action.run();
        
        isPushed = false;
        return label;
    }
    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}