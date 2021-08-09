package mngr;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * Initializing the application, launching the main window
 * @author MrUnknown404
 */
public class InitialModule {
    /**
     * Launching the main window
     * @param args cmd arhs
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        MainJFrame newFrame;
        
        try {
            newFrame = new MainJFrame();
            ImageIcon icon = new ImageIcon("src//mngrIcon.png");
            newFrame.setIconImage(icon.getImage());
            newFrame.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(InitialModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
