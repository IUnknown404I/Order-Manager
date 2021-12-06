package mngr.orders_functional;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import mngr.TableMethods;

/**
 * This is Popup menu for mouse's right button click-event 
 * @author MrUnknown404
 */
public class EditPopup extends JPopupMenu {
    private final JMenuItem archieve_replaceItem;
    
    /**
     * Constructor for Popup menu
     * @param main - the table of actual orders
     * @param archive - the table of archive orders
     */
    public EditPopup(JTable main, JTable archive) {
        archieve_replaceItem = new JMenuItem("Переместить в архив");
        archieve_replaceItem.setForeground(Color.RED);
        TableMethods.resetSelection(main, false);
        
        archieve_replaceItem.addActionListener((ActionEvent e) -> {
            if(TableMethods.getWarningPaneReplace() == JOptionPane.YES_OPTION) {
                try {
                    TableMethods.setModifyingAvaibleMark(false);
                    TableMethods.replaceOrderToArchive(main.getSelectedRow(), main, archive);
                    TableMethods.resetSelection(main, true);
                    TableMethods.setModifyingAvaibleMark(true);
                } catch (IOException ex) { Logger.getLogger(EditPopup.class.getName()).log(Level.SEVERE, null, ex); }
            } 
        });
        
        add(archieve_replaceItem);
    }
}
