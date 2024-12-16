import java.util.*;

public class UnitConversionSystem {

    // HashMap to store conversion factors for unit types
    private static Map<String, Map<String, Double>> unitGraph = new HashMap<>();

    public static void main(String[] args) {
        initializeUnits(); // Initialize predefined units and conversions
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Unit Conversion System!");

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Convert Units");
            System.out.println("2. Add/Modify Units");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = getValidInteger(scanner);
            switch (choice) {
                case 1 -> performConversion(scanner);
                case 2 -> modifyUnits(scanner);
                case 3 -> {
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Initialize unit categories and conversion factors
    private static void initializeUnits() {
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

    // Perform unit conversion
    private static void performConversion(Scanner scanner) {
        System.out.print("Enter the source unit: ");
        String fromUnit = scanner.nextLine().toLowerCase();

        System.out.print("Enter the target unit: ");
        String toUnit = scanner.nextLine().toLowerCase();

        System.out.print("Enter the value to convert: ");
        double value = getValidDouble(scanner);

        try {
            double result = convertUnit(fromUnit, toUnit, value);
            System.out.printf("%.4f %s is equal to %.4f %s\n", value, fromUnit, result, toUnit);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Unit conversion logic including special handling for temperature
    private static double convertUnit(String from, String to, double value) throws Exception {
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

    // Add or modify unit conversion rules
    private static void modifyUnits(Scanner scanner) {
        System.out.print("Enter the source unit: ");
        String from = scanner.nextLine().toLowerCase();

        System.out.print("Enter the target unit: ");
        String to = scanner.nextLine().toLowerCase();

        System.out.print("Enter the conversion factor (from -> to): ");
        double factor = getValidDouble(scanner);

        addConversion(from, to, factor);
        System.out.println("Conversion rule added/updated successfully.");
    }

    // Utility method to validate integer input
    private static int getValidInteger(Scanner scanner) {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    // Utility method to validate double input
    private static double getValidDouble(Scanner scanner) {
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
