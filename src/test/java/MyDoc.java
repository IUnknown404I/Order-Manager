

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 *
 * @author MrUnknown404
 */
public class MyDoc extends DefaultStyledDocument{

    public MyDoc() {
        super();
    }
    
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // fields don't want to have multiple lines.  We may provide a field-specific
        // model in the future in which case the filtering logic here will no longer
        // be needed.
        Object filterNewlines = getProperty("filterNewlines");
        System.out.println("AAAAAAAAAAAAAAA");

        if ((filterNewlines instanceof Boolean) && filterNewlines.equals(Boolean.TRUE)) {
            if ((str != null) && (str.indexOf('\n') >= 0)) {
                StringBuilder filtered = new StringBuilder(str);
                int n = filtered.length();

                for (int i = 0; i < n; i++) {
                    if (filtered.charAt(i) == '\n') {
                        filtered.replace(i, i, "\n ");
                    }
                }
                str = filtered.toString();
            }
        }
        if (getLength()+str.length() < 12) {
            super.insertString(offs, str, a);
        }
    }
}
