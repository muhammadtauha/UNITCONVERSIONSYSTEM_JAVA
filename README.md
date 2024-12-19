# Unit Conversion System 

# Overview
The Unit Conversion System is a Java-based application designed to provide seamless and accurate conversions across multiple unit types, such as length, weight, temperature, and time. It features an intuitive and stylish graphical user interface (GUI) built using Swing, offering a user-friendly experience for all.

# Features
Supports Multiple Unit Types:
Perform conversions for length (meters, kilometers, etc.), weight (kilograms, pounds, etc.), temperature (Celsius, Fahrenheit), and time (seconds, minutes, hours).
Custom Conversion Logic:
Special handling for temperature conversions using formulas.
Interactive GUI:
Stylish interface with vibrant colors and smooth layouts.
Dropdowns for selecting "From Unit" and "To Unit."
Clear and dynamic display of results in an easy-to-read format.
Error Handling:
Alerts users for invalid inputs or unsupported conversions.
Ensures a seamless user experience.

# Technology Stack
Java
Swing

# How It Works
Initialize Units:
The program initializes a unit graph containing predefined conversion factors for various unit types.
Handle User Input:
Users input the value and select the "From Unit" and "To Unit."
Perform Conversion:
Using BFS-based logic for indirect conversions, the application computes the result efficiently, handling special cases like temperature.
# Display Result:
The converted value is displayed dynamically in a styled label within the GUI.
