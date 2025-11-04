package dk.easv.countculator1;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CalculationRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String expression;
    private String result;
    private LocalDateTime timestamp;

    public CalculationRecord(String expression, String result) {
        this.expression = expression;
        this.result = result;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getExpression() { return expression; }
    public String getResult() { return result; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("%s = %s (%s)", expression, result,
                timestamp.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}