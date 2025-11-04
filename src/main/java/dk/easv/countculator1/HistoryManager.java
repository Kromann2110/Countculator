package dk.easv.countculator1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    public static void saveHistory(List<CalculationRecord> history, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(new ArrayList<>(history));
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<CalculationRecord> loadHistory(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<CalculationRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading history: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}