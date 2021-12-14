package mngr.renders_and_editors;

import mngr.DescriptionInputFrame;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * The Editor class for description column of JTables with <code>DescriptionInputFrame</code> use
 * @author MrUnknown404
 */
public class DescriptionColumnEditor extends DefaultCellEditor{
    /**
     * The description cells editor for JTables
     * @param textField  new JTextField
     */
    public DescriptionColumnEditor(JTextField textField){
        super(textField);
        setClickCountToStart(2);
        
        this.textField = new JTextField();
        this.textField.setOpaque(true);
        this.textField.setEditable(false);
        this.textField.setEnabled(false);
    }
    
    private JTextField textField;
    private String label;
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        // окраска
        if (isSelected) {
            textField.setBackground(Color.CYAN);
        } else {
            textField.setBackground(table.getBackground());
        }
        
        // output the frame of description modifying
        DescriptionInputFrame dialog = new DescriptionInputFrame((String)table.getValueAt(row,0)+" "+((table.getName().equals("Archieve"))?(String)table.getValueAt(row, 1):(String)table.getValueAt(row, 2)),
                                                            value == null ? "":getPlainTextFromFormatted(value.toString()), table.getName().equals("Archieve"));
        label = (value == null) ? "" : getFormattedText(value.toString());
        
        return textField;
    }
    
    /**
     * Getter for formatted text
     * @param text text to format
     * @return formatted text
     */
    private String getFormattedText(String text){
        String formattedText = text;
        return formattedText.replaceAll("\n", " \n");
    }
    /**
     * Getter for the plain text
     * @param formattedText formatted text
     * @return plain text
     */
    private String getPlainTextFromFormatted(String formattedText){
        String plainText = formattedText;
        return plainText.replaceAll(" \n", "\n");
    }
    
    @Override
    public int getClickCountToStart(){
        return 2;
    }
    @Override
    public Object getCellEditorValue() {
        return DescriptionInputFrame.getValue();
    }
}
