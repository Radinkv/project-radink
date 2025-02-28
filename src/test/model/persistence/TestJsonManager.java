package model.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import persistence.JsonManager;

/** 
 * This class tests whether JsonManager effectively reads, writes JSON
 * into the specified file and location, and correctly handles exceptions
 * or invalid states of JSON.
 */
class TestJsonManager {
    private static final String TEST_PATH = "./data/test-workout-data.json";
    private static final String TEST_DIRECTORY = "./data";

    private JSONObject mockJsonObject;
    private JSONObject invalidJsonObject;
    private Map<String, JSONObject> mockData;

    @BeforeEach
    void runBefore() {
        cleanupTestFiles();
        
        mockJsonObject = new JSONObject();
        mockJsonObject.put("test", "value");
        mockJsonObject.put("number", 42);
        
        mockData = new HashMap<>();
        mockData.put("mockComponent", mockJsonObject);
        
        // For testing JSONException cases we'll use a circular reference
        // This will throw an exception when, for instance, toString() is called during serialization
        invalidJsonObject = new JSONObject();
        JSONObject circular = new JSONObject();
        invalidJsonObject.put("circular", circular);
        circular.put("back", invalidJsonObject);
    }

    @AfterEach
    void tearDown() {
        cleanupTestFiles(); // Revert changes made to files
    }

    @Test
    void testConstructorThrowsAssertionError() {
        try {
            new JsonManager();
            fail("AssertionError should have been thrown");
        } catch (AssertionError e) {
            assertEquals("Utility class should not be instantiated", e.getMessage());
        }
    }


    @Test
    void testSaveAndLoadWithDefaultPath() {
        try {
            JsonManager.saveData(mockData);
            assertTrue(new File("./data/workout-data.json").exists());

            Map<String, JSONObject> loaded = JsonManager.loadData();
            assertFalse(loaded.isEmpty());
            assertTrue(loaded.containsKey("mockComponent"));
            assertEquals("value", loaded.get("mockComponent").getString("test"));
            assertEquals(42, loaded.get("mockComponent").getInt("number"));
        } catch (JSONException e) {
            fail("JSONException should not have been thrown: " + e.getMessage());
        } finally {
            // Clean up the default path file
            new File("./data/workout-data.json").delete();
        }
    }

    @Test
    void testSaveAndLoadWithCustomPath() {
        try {
            JsonManager.saveData(mockData, TEST_PATH);
            assertTrue(new File(TEST_PATH).exists());

            Map<String, JSONObject> loaded = JsonManager.loadData(TEST_PATH);
            assertFalse(loaded.isEmpty());
            assertTrue(loaded.containsKey("mockComponent"));
            assertEquals("value", loaded.get("mockComponent").getString("test"));
            assertEquals(42, loaded.get("mockComponent").getInt("number"));
        } catch (JSONException e) {
            fail("JSONException should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    void testSaveEmptyData() {
        Map<String, JSONObject> emptyData = new HashMap<>();
        try {
            JsonManager.saveData(emptyData, TEST_PATH);
            
            // Verify file was created and contains an empty JSON object
            String content = new String(Files.readAllBytes(Paths.get(TEST_PATH)));
            assertEquals("{}", content.trim());
            
            // Verify loading returns an empty map
            Map<String, JSONObject> loaded = JsonManager.loadData(TEST_PATH);
            assertTrue(loaded.isEmpty());
        } catch (JSONException | IOException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    void testSaveWithInvalidJson() {
        // Test with invalid JSON that will cause circular reference error
        Map<String, JSONObject> invalidData = new HashMap<>();
        invalidData.put("invalid", invalidJsonObject);
        
        try {
            JsonManager.saveData(invalidData, TEST_PATH);
            fail("Exception should have been thrown");
        } catch (JSONException | StackOverflowError e) {
            // Either exception is acceptable
            // JSONException might be thrown if JSONObject detects the circular reference
            // StackOverflowError might be thrown if the circular reference causes infinite recursion
        }
    }

    @Test
    void testLoadFromNonexistentFile() {
        try {
            Map<String, JSONObject> loaded = JsonManager.loadData(TEST_PATH);
            assertTrue(loaded.isEmpty());
        } catch (JSONException e) {
            fail("JSONException should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    void testLoadFromCorruptedFile() {
        try {
            // Ensure directory exists before writing files
            new File(TEST_DIRECTORY).mkdirs();
        
            // Test with corrupted JSON file
            Files.write(Paths.get(TEST_PATH), "This is not valid JSON".getBytes());
            
            JsonManager.loadData(TEST_PATH);
            fail("JSONException should have been thrown");
        } catch (JSONException e) {
            // Expected exception
            // The exact error message might vary between JSON library versions, so assume it works as expected
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    @Test
    void testLoadFromEmptyFile() {
        try {
            // Ensure directory exists before writing files
            new File(TEST_DIRECTORY).mkdirs();
        
            // Test with empty file
            Files.write(Paths.get(TEST_PATH), "".getBytes());
            
            JsonManager.loadData(TEST_PATH);
            fail("JSONException should have been thrown");
        } catch (JSONException e) {
            // Expected exception
            // Similar to above, assume the error is caused as expeced
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    @Test
    void testSaveIOException() {
        // Create directory and make it read-only to cause write permission error
        File directory = new File(TEST_DIRECTORY);
        directory.mkdirs();
        
        // Set directory to read-only on supported platforms
        if (directory.setReadOnly()) {
            try {
                JsonManager.saveData(mockData, TEST_PATH);
                fail("JSONException should have been thrown");
            } catch (JSONException e) {
                // Expected exception
                assertTrue(e.getMessage().contains("Unable to write to file"));
            } finally {
                // Reset permissions
                directory.setWritable(true);
            }
        }
    }

    @Test
    void testLoadIOException() {
        try {
            // Create a file and make it unreadable to cause read permission error
            new File(TEST_DIRECTORY).mkdirs();
            File file = new File(TEST_PATH);
            file.createNewFile();
            
            // Set file to non readable
            if (file.setReadable(false)) {
                try {
                    JsonManager.loadData(TEST_PATH);
                    fail("JSONException should have been thrown");
                } catch (JSONException e) {
                    // Expected exception
                    assertTrue(e.getMessage().contains("Unable to read from file"));
                } finally {
                    // Reset permissions
                    file.setReadable(true);
                }
            }
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    @Test
    void testDirectoryCreation() {
        // Clean up to ensure directory doesn't exist
        cleanupTestFiles();
        assertFalse(new File(TEST_DIRECTORY).exists());
        
        // Test that directory is created when saving
        try {
            JsonManager.saveData(mockData, TEST_PATH);
            assertTrue(new File(TEST_DIRECTORY).exists());
            assertTrue(new File(TEST_PATH).exists());
        } catch (JSONException e) {
            fail("JSONException should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    void testNestedDirectoryCreation() {
        // Test with a nested directory structure
        String nestedPath = "./data/nested/test/workout-data.json";
        
        try {
            JsonManager.saveData(mockData, nestedPath);
            assertTrue(new File("./data/nested/test").exists());
            assertTrue(new File(nestedPath).exists());
        } catch (JSONException e) {
            fail("JSONException should not have been thrown: " + e.getMessage());
        } finally {
            // Clean up nested directories
            new File(nestedPath).delete();
            new File("./data/nested/test").delete();
            new File("./data/nested").delete();
        }
    }

    @Test
    void testSaveAndLoadMultipleComponents() {
        // Test with multiple components
        Map<String, JSONObject> multiData = new HashMap<>();
        
        JSONObject component1 = new JSONObject();
        component1.put("name", "Component 1");
        
        JSONObject component2 = new JSONObject();
        component2.put("name", "Component 2");
        component2.put("value", 123);
        
        JSONObject component3 = new JSONObject();
        component3.put("name", "Component 3");
        component3.put("active", true);
        
        multiData.put("comp1", component1);
        multiData.put("comp2", component2);
        multiData.put("comp3", component3);
        
        try {
            JsonManager.saveData(multiData, TEST_PATH);
            
            Map<String, JSONObject> loaded = JsonManager.loadData(TEST_PATH);

            // Check save and load data is exactly as propogated
            assertEquals(3, loaded.size());
            assertTrue(loaded.containsKey("comp1"));
            assertTrue(loaded.containsKey("comp2"));
            assertTrue(loaded.containsKey("comp3"));
            
            assertEquals("Component 1", loaded.get("comp1").getString("name"));
            assertEquals("Component 2", loaded.get("comp2").getString("name"));
            assertEquals(123, loaded.get("comp2").getInt("value"));
            assertEquals("Component 3", loaded.get("comp3").getString("name"));
            assertTrue(loaded.get("comp3").getBoolean("active"));
        } catch (JSONException e) {
            fail("JSONException should not have been thrown: " + e.getMessage());
        }
    }

    private void cleanupTestFiles() {
        // Delete file first then directory
        File file = new File(TEST_PATH);
        if (file.exists()) {
            file.delete();
        }
        
        // Also clean the default path file if it exists
        File defaultFile = new File("./data/workout-data.json");
        if (defaultFile.exists()) {
            defaultFile.delete();
        }
        
        File directory = new File(TEST_DIRECTORY);
        if (directory.exists() && directory.isDirectory() && directory.list().length == 0) {
            directory.delete();
        }
    }
}