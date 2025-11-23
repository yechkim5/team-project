public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        GameOrchestrator.init();   // ← THIS LOADS OR STARTS FRESH
        // then show your first panel
    });
}