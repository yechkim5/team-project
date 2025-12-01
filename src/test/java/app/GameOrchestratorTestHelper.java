package app;

import java.lang.reflect.Field;

/**
 * Helper to reset the static 'current' field in GameOrchestrator
 * Needed because GameOrchestrator uses a static field → shared between tests
 */
public class GameOrchestratorTestHelper {

    public static void clearCurrentState() {
        try {
            Field field = GameOrchestrator.class.getDeclaredField("current");
            field.setAccessible(true);
            field.set(null, null);  // null out the static field
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset GameOrchestrator.current", e);
        }
    }
}