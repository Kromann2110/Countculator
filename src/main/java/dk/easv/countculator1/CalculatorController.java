package dk.easv.countculator1;

import dk.easv.countculator1.business.Calculator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CalculatorController implements Initializable {

    @FXML private TextArea Result;
    @FXML private VBox historyPanel;
    @FXML private Button btnHistoryToggle;
    @FXML private Button btnClearHistory;
    @FXML private ListView<String> historyListView;
    @FXML private Label lblHistoryCount;

    private Calculator calculator;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0;
    private boolean startNewNumber = true;
    private List<CalculationRecord> history;
    private static final String HISTORY_FILE = "calculator_history.dat";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calculator = new Calculator();
        history = new ArrayList<>();
        loadHistoryFromFile();
        Result.setText("0");
        Result.setEditable(false);
        updateHistoryDisplay();
    }

    @FXML
    private void handleButtonClick(javafx.event.ActionEvent event) {
        Button button = (Button) event.getSource();
        String buttonText = button.getText();

        switch (buttonText) {
            case "C":
                clearCalculator();
                break;
            case "+/-":
                toggleSign();
                break;
            case "%":
            case "%%":
                applyPercentage();
                break;
            case "+":
            case "-":
            case "X":
            case "/":
                handleOperator(buttonText);
                break;
            case ",":
            case ".":
                handleDecimal();
                break;
            default:
                // It's a number
                handleNumber(buttonText);
                break;
        }
    }

    @FXML
    private void handleEquals() {
        if (operator.isEmpty() || currentInput.isEmpty()) {
            return;
        }

        try {
            double secondOperand = parseInput(currentInput);
            String expression = formatNumber(firstOperand) + " " + operator + " " + formatNumber(secondOperand);

            double result = calculator.calculate(firstOperand, secondOperand, operator);
            String resultStr = formatNumber(result);

            Result.setText(resultStr);

            // Add to history
            CalculationRecord record = new CalculationRecord(expression, resultStr);
            history.add(record);
            updateHistoryDisplay();
            saveHistoryToFile();

            // Reset for next calculation
            currentInput = resultStr;
            operator = "";
            startNewNumber = true;
        } catch (IllegalArgumentException e) {
            Result.setText("Error: " + e.getMessage());
            clearCalculator();
        }
    }

    private void handleNumber(String number) {
        if (startNewNumber) {
            currentInput = number;
            startNewNumber = false;
        } else {
            currentInput += number;
        }
        Result.setText(currentInput);
    }

    private void handleOperator(String op) {
        if (!currentInput.isEmpty()) {
            if (!operator.isEmpty()) {
                handleEquals();
            }
            firstOperand = parseInput(currentInput);
            operator = op;
            startNewNumber = true;
        }
    }

    private void handleDecimal() {
        if (startNewNumber) {
            currentInput = "0.";
            startNewNumber = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        Result.setText(currentInput);
    }

    private void toggleSign() {
        if (!currentInput.isEmpty() && !currentInput.equals("0")) {
            double value = parseInput(currentInput);
            value = calculator.toggleSign(value);
            currentInput = formatNumber(value);
            Result.setText(currentInput);
        }
    }

    private void applyPercentage() {
        if (!currentInput.isEmpty()) {
            double value = parseInput(currentInput);
            value = calculator.percentage(value);
            currentInput = formatNumber(value);
            Result.setText(currentInput);
        }
    }

    private void clearCalculator() {
        currentInput = "";
        operator = "";
        firstOperand = 0;
        startNewNumber = true;
        Result.setText("0");
    }

    @FXML
    private void toggleHistory() {
        boolean isVisible = historyPanel.isVisible();
        historyPanel.setVisible(!isVisible);
        historyPanel.setManaged(!isVisible);
        btnHistoryToggle.setText(isVisible ? "Show History" : "Hide History");
    }

    @FXML
    private void clearHistory() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear History");
        alert.setHeaderText("Are you sure you want to clear the calculation history?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            history.clear();
            historyListView.getItems().clear();
            lblHistoryCount.setText("0 items");
            saveHistoryToFile();
        }
    }

    private void updateHistoryDisplay() {
        historyListView.getItems().clear();
        for (CalculationRecord record : history) {
            historyListView.getItems().add(record.toString());
        }
        lblHistoryCount.setText(history.size() + " items");
    }

    private void loadHistoryFromFile() {
        history = HistoryManager.loadHistory(HISTORY_FILE);
    }

    private void saveHistoryToFile() {
        HistoryManager.saveHistory(history, HISTORY_FILE);
    }

    private double parseInput(String input) {
        return Double.parseDouble(input.replace(",", "."));
    }

    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            return String.format("%s", number).replace(".", ",");
        }
    }
}