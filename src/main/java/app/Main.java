package app;

import javax.swing.SwingUtilities;
import app.GameOrchestrator;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameOrchestrator.init();  
        });
    }
}
