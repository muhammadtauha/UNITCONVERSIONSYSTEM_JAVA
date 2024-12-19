import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class UnitConversionSystem {

    // HashMap to store conversion factors for unit types
    private static Map<String, Map<String, Double>> unitGraph = new HashMap<>();

    public static void main(String[] args) {
        initializeUnits(); // Initialize predefined units and conversions

        // Launch GUI
        SwingUtilities.invokeLater(() -> new StyledUnitConversionGUI().createAndShowGUI());
    }

    // Initialize unit categories and conversion factors
    public static void initializeUnits() {
        // Length units
        addConversion("centimeter", "meter", 0.01);
        addConversion("meter", "centimeter", 100.0);
        addConversion("meter", "kilometer", 0.001);
        addConversion("kilometer", "meter", 1000.0);

        // Weight units
        addConversion("gram", "kilogram", 0.001);
        addConversion("kilogram", "gram", 1000.0);
        addConversion("pound", "kilogram", 0.4536);
        addConversion("kilogram", "pound", 2.2046);

        // Temperature units
        addTemperatureConversion();

        // Time units
        addConversion("minute", "second", 60.0);
        addConversion("second", "minute", 1 / 60.0);
        addConversion("hour", "minute", 60.0);
        addConversion("minute", "hour", 1 / 60.0);
    }

    // Add a conversion between two units
    private static void addConversion(String from, String to, double factor) {
        from = from.toLowerCase();
        to = to.toLowerCase();

        unitGraph.putIfAbsent(from, new HashMap<>());
        unitGraph.get(from).put(to, factor);
    }

    // Add temperature conversions (Celsius to Fahrenheit and vice versa)
    private static void addTemperatureConversion() {
        unitGraph.putIfAbsent("celsius", new HashMap<>());
        unitGraph.putIfAbsent("fahrenheit", new HashMap<>());

        unitGraph.get("celsius").put("fahrenheit", 1.8); // Placeholder for calculation
        unitGraph.get("fahrenheit").put("celsius", 0.5556);
    }

    // Unit conversion logic including special handling for temperature
    public static double convertUnit(String from, String to, double value) throws Exception {
        from = from.toLowerCase();
        to = to.toLowerCase();

        if (from.equals("celsius") && to.equals("fahrenheit")) {
            return (value * 1.8) + 32;
        } else if (from.equals("fahrenheit") && to.equals("celsius")) {
            return (value - 32) * 0.5556;
        }

        // Regular BFS conversion for other units
        if (!unitGraph.containsKey(from) || !unitGraph.containsKey(to)) {
            throw new Exception("Invalid unit(s) entered.");
        }

        Queue<String> queue = new LinkedList<>();
        Map<String, Double> visited = new HashMap<>();

        queue.add(from);
        visited.put(from, 1.0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            double currentFactor = visited.get(current);

            if (current.equals(to)) {
                return value * currentFactor;
            }

            Map<String, Double> neighbors = unitGraph.get(current);
            if (neighbors != null) {
                for (Map.Entry<String, Double> entry : neighbors.entrySet()) {
                    if (!visited.containsKey(entry.getKey())) {
                        visited.put(entry.getKey(), currentFactor * entry.getValue());
                        queue.add(entry.getKey());
                    }
                }
            }
        }

        throw new Exception("Conversion path not found between " + from + " and " + to);
    }

    // Styled GUI Class
    static class StyledUnitConversionGUI {

        public void createAndShowGUI() {
            // Main frame
            JFrame frame = new JFrame("Unit Conversion System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setResizable(false);

            // Content panel with padding
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            frame.setContentPane(contentPanel);
            contentPanel.setBackground(new Color(34, 49, 63)); // Dark theme

            // Header
            JLabel headerLabel = new JLabel("Unit Conversion System", SwingConstants.CENTER);
            headerLabel.setFont(new Font("Verdana", Font.BOLD, 28));
            headerLabel.setForeground(Color.WHITE);
            headerLabel.setBorder(new EmptyBorder(10, 10, 20, 10));
            contentPanel.add(headerLabel, BorderLayout.NORTH);

            // Center panel for inputs and results
            JPanel centerPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gradientPaint = new GradientPaint(0, 0, new Color(52, 152, 219), getWidth(), getHeight(), new Color(41, 128, 185));
                    g2d.setPaint(gradientPaint);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                }
            };
            centerPanel.setLayout(new GridLayout(4, 2, 10, 10));
            centerPanel.setOpaque(false); // Transparent panel
            centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.add(centerPanel, BorderLayout.CENTER);

            // Input fields and labels
            JLabel fromLabel = new JLabel("From Unit:");
            JLabel toLabel = new JLabel("To Unit:");
            JLabel valueLabel = new JLabel("Value:");
            JLabel resultLabel = new JLabel("Result:");

            styleLabel(fromLabel);
            styleLabel(toLabel);
            styleLabel(valueLabel);
            styleLabel(resultLabel);

            JComboBox<String> fromUnit = new JComboBox<>(unitGraph.keySet().toArray(new String[0]));
            JComboBox<String> toUnit = new JComboBox<>(unitGraph.keySet().toArray(new String[0]));
            JTextField valueField = new JTextField();
            JLabel resultOutput = new JLabel("---", SwingConstants.CENTER);

            styleDropdown(fromUnit);
            styleDropdown(toUnit);
            styleField(valueField);
            styleResult(resultOutput);

            centerPanel.add(fromLabel);
            centerPanel.add(fromUnit);
            centerPanel.add(toLabel);
            centerPanel.add(toUnit);
            centerPanel.add(valueLabel);
            centerPanel.add(valueField);
            centerPanel.add(resultLabel);
            centerPanel.add(resultOutput);

            // Footer panel for buttons
            JPanel footerPanel = new JPanel();
            footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
            footerPanel.setOpaque(false); // Transparent panel
            contentPanel.add(footerPanel, BorderLayout.SOUTH);

            JButton convertButton = new JButton("Convert");
            JButton exitButton = new JButton("Exit");
            styleButton(convertButton);
            styleButton(exitButton);

            footerPanel.add(convertButton);
            footerPanel.add(exitButton);

            // Button actions
            convertButton.addActionListener(e -> {
                try {
                    String from = fromUnit.getSelectedItem().toString();
                    String to = toUnit.getSelectedItem().toString();
                    double value = Double.parseDouble(valueField.getText());

                    double result = convertUnit(from, to, value);
                    resultOutput.setText(String.format("%.4f", result));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Conversion Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            exitButton.addActionListener(e -> System.exit(0));

            frame.setVisible(true);
        }

        // Styling helpers
        private void styleLabel(JLabel label) {
            label.setFont(new Font("Verdana", Font.BOLD, 16));
            label.setForeground(Color.WHITE);
        }

        private void styleDropdown(JComboBox<String> comboBox) {
            comboBox.setFont(new Font("Verdana", Font.PLAIN, 14));
            comboBox.setBackground(new Color(236, 240, 241)); // Light grey
        }

        private void styleField(JTextField field) {
            field.setFont(new Font("Verdana", Font.PLAIN, 14));
            field.setBackground(new Color(236, 240, 241)); // Light grey
        }

        private void styleResult(JLabel label) {
            label.setFont(new Font("Verdana", Font.BOLD, 18));
            label.setForeground(new Color(39, 174, 96)); // Green color
        }

        private void styleButton(JButton button) {
            button.setFont(new Font("Verdana", Font.BOLD, 14));
            button.setBackground(new Color(52, 152, 219)); // Light blue
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setUI(new MetalButtonUI() {
                protected Color getDisabledTextColor() {
                    return Color.GRAY;
                }
            });
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
}
