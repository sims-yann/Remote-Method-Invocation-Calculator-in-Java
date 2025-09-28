package org.client;

import org.interfaces.CalculatorInterface;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class CalculatorGUI extends JFrame {
    private CalculatorInterface calculator;
    private JTextField displayField;
    private JButton connectBtn, addBtn, subBtn, mulBtn, divBtn, powBtn, sqrtBtn, clearBtn, equalsBtn;
    private JButton num0Btn, num1Btn, num2Btn, num3Btn, num4Btn, num5Btn, num6Btn, num7Btn, num8Btn, num9Btn, decimalBtn;
    private JLabel statusLabel;

    private double firstNumber = 0;
    private String operation = "";
    private boolean startNewNumber = true;

    public CalculatorGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Modern Calculator");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initializeComponents() {
        // Custom fonts
        Font displayFont = new Font("Segoe UI", Font.BOLD, 24);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);
        Font statusFont = new Font("Segoe UI", Font.ITALIC, 12);

        // Color scheme
        Color displayColor = new Color(159, 159, 195);
        Color numButtonColor = new Color(174, 149, 193);
        Color opButtonColor = new Color(90, 170, 230);
        Color specialButtonColor = new Color(250, 150, 150);
        Color connectButtonColor = new Color(100, 180, 100);

        // Display field
        displayField = new JTextField("0");
        displayField.setFont(displayFont);
        displayField.setBackground(displayColor);
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Number buttons
        num0Btn = createStyledButton("0", numButtonColor, buttonFont);
        num1Btn = createStyledButton("1", numButtonColor, buttonFont);
        num2Btn = createStyledButton("2", numButtonColor, buttonFont);
        num3Btn = createStyledButton("3", numButtonColor, buttonFont);
        num4Btn = createStyledButton("4", numButtonColor, buttonFont);
        num5Btn = createStyledButton("5", numButtonColor, buttonFont);
        num6Btn = createStyledButton("6", numButtonColor, buttonFont);
        num7Btn = createStyledButton("7", numButtonColor, buttonFont);
        num8Btn = createStyledButton("8", numButtonColor, buttonFont);
        num9Btn = createStyledButton("9", numButtonColor, buttonFont);
        decimalBtn = createStyledButton(".", numButtonColor, buttonFont);

        // Operation buttons
        addBtn = createStyledButton("+", opButtonColor, buttonFont);
        subBtn = createStyledButton("-", opButtonColor, buttonFont);
        mulBtn = createStyledButton("×", opButtonColor, buttonFont);
        divBtn = createStyledButton("÷", opButtonColor, buttonFont);
        powBtn = createStyledButton("x^y", opButtonColor, buttonFont);
        sqrtBtn = createStyledButton("√", opButtonColor, buttonFont);
        equalsBtn = createStyledButton("=", opButtonColor, buttonFont);

        // Special buttons
        clearBtn = createStyledButton("C", specialButtonColor, buttonFont);
        connectBtn = createStyledButton("Connect", connectButtonColor, new Font("Segoe UI", Font.BOLD, 14));

        // Status label
        statusLabel = new JLabel("Not connected to server");
        statusLabel.setForeground(new Color(200, 60, 60));
        statusLabel.setFont(statusFont);

        // Initially disable operation buttons
        setOperationButtonsEnabled(false);
    }

    private JButton createStyledButton(String text, Color bgColor, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void setupLayout() {
        // Main panel with light background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(255, 0, 94));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Display panel
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(new Color(255, 0, 255));
        displayPanel.add(displayField, BorderLayout.CENTER);
        mainPanel.add(displayPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 8, 8));
        buttonPanel.setBackground(new Color(255, 0, 98));

        // First row
        buttonPanel.add(clearBtn);
        buttonPanel.add(sqrtBtn);
        buttonPanel.add(powBtn);
        buttonPanel.add(divBtn);

        // Second row
        buttonPanel.add(num7Btn);
        buttonPanel.add(num8Btn);
        buttonPanel.add(num9Btn);
        buttonPanel.add(mulBtn);

        // Third row
        buttonPanel.add(num4Btn);
        buttonPanel.add(num5Btn);
        buttonPanel.add(num6Btn);
        buttonPanel.add(subBtn);

        // Fourth row
        buttonPanel.add(num1Btn);
        buttonPanel.add(num2Btn);
        buttonPanel.add(num3Btn);
        buttonPanel.add(addBtn);

        // Fifth row
        buttonPanel.add(connectBtn);
        buttonPanel.add(num0Btn);
        buttonPanel.add(decimalBtn);
        buttonPanel.add(equalsBtn);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(new Color(230, 230, 240));
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void setupEventHandlers() {
        // Connect button
        connectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        // Number buttons
        ActionListener numberListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startNewNumber) {
                    displayField.setText("");
                    startNewNumber = false;
                }
                String currentText = displayField.getText();
                String buttonText = ((JButton)e.getSource()).getText();

                // Prevent multiple decimal points
                if (buttonText.equals(".") && currentText.contains(".")) {
                    return;
                }

                displayField.setText(currentText + buttonText);
            }
        };

        num0Btn.addActionListener(numberListener);
        num1Btn.addActionListener(numberListener);
        num2Btn.addActionListener(numberListener);
        num3Btn.addActionListener(numberListener);
        num4Btn.addActionListener(numberListener);
        num5Btn.addActionListener(numberListener);
        num6Btn.addActionListener(numberListener);
        num7Btn.addActionListener(numberListener);
        num8Btn.addActionListener(numberListener);
        num9Btn.addActionListener(numberListener);
        decimalBtn.addActionListener(numberListener);

        // Operation buttons
        ActionListener operationListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculator == null) {
                    JOptionPane.showMessageDialog(CalculatorGUI.this, "Please connect to server first!",
                            "Not Connected", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                firstNumber = Double.parseDouble(displayField.getText());
                operation = ((JButton)e.getSource()).getText();
                startNewNumber = true;
            }
        };

        addBtn.addActionListener(operationListener);
        subBtn.addActionListener(operationListener);
        mulBtn.addActionListener(operationListener);
        divBtn.addActionListener(operationListener);
        powBtn.addActionListener(operationListener);
        sqrtBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculator == null) {
                    JOptionPane.showMessageDialog(CalculatorGUI.this, "Please connect to server first!",
                            "Not Connected", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                performOperation("sqrt");
                startNewNumber = true;
            }
        });

        // Equals button
        equalsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculator == null) {
                    JOptionPane.showMessageDialog(CalculatorGUI.this, "Please connect to server first!",
                            "Not Connected", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!operation.isEmpty()) {
                    String op = "";
                    switch (operation) {
                        case "+": op = "add"; break;
                        case "-": op = "subtract"; break;
                        case "×": op = "multiply"; break;
                        case "÷": op = "divide"; break;
                        case "x^y": op = "power"; break;
                    }

                    if (!op.isEmpty()) {
                        performOperation(op);
                    }
                    operation = "";
                    startNewNumber = true;
                }
            }
        });

        // Clear button
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayField.setText("0");
                firstNumber = 0;
                operation = "";
                startNewNumber = true;
            }
        });
    }

    private void connectToServer() {
        try {
            calculator = (CalculatorInterface) Naming.lookup("//localhost:1099/Calculator");
            statusLabel.setText("Connected to server");
            statusLabel.setForeground(new Color(60, 160, 60));
            setOperationButtonsEnabled(true);
            JOptionPane.showMessageDialog(this, "Successfully connected to server!",
                    "Connection Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            statusLabel.setText("Failed to connect to server");
            statusLabel.setForeground(new Color(220, 80, 80));
            setOperationButtonsEnabled(false);
            JOptionPane.showMessageDialog(this, "Failed to connect to server:\n" + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performOperation(String operation) {
        try {
            double result = 0;
            double secondNumber = Double.parseDouble(displayField.getText());

            if ("sqrt".equals(operation)) {
                result = calculator.sqrt(secondNumber);
            } else {
                switch (operation) {
                    case "add":
                        result = calculator.add(firstNumber, secondNumber);
                        break;
                    case "subtract":
                        result = calculator.subtract(firstNumber, secondNumber);
                        break;
                    case "multiply":
                        result = calculator.multiply(firstNumber, secondNumber);
                        break;
                    case "divide":
                        result = calculator.divide(firstNumber, secondNumber);
                        break;
                    case "power":
                        result = calculator.power(firstNumber, secondNumber);
                        break;
                }
            }

            displayField.setText(String.valueOf(result));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Remote operation failed:\n" + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setOperationButtonsEnabled(boolean enabled) {
        addBtn.setEnabled(enabled);
        subBtn.setEnabled(enabled);
        mulBtn.setEnabled(enabled);
        divBtn.setEnabled(enabled);
        powBtn.setEnabled(enabled);
        sqrtBtn.setEnabled(enabled);
        equalsBtn.setEnabled(enabled);

        // Visual feedback for disabled state
        Color enabledColor = new Color(90, 170, 230);
        Color disabledColor = new Color(180, 180, 190);

        addBtn.setBackground(enabled ? enabledColor : disabledColor);
        subBtn.setBackground(enabled ? enabledColor : disabledColor);
        mulBtn.setBackground(enabled ? enabledColor : disabledColor);
        divBtn.setBackground(enabled ? enabledColor : disabledColor);
        powBtn.setBackground(enabled ? enabledColor : disabledColor);
        sqrtBtn.setBackground(enabled ? enabledColor : disabledColor);
        equalsBtn.setBackground(enabled ? enabledColor : disabledColor);
    }
}


//

//package org.client;
//
//import org.interfaces.CalculatorInterface;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.rmi.Naming;
//import java.rmi.RemoteException;
//
//public class CalculatorGUI extends JFrame {
//    private CalculatorInterface calculator;
//    private JTextField num1Field, num2Field, resultField;
//    private JButton connectBtn, addBtn, subBtn, mulBtn, divBtn, powBtn, sqrtBtn, clearBtn;
//    private JLabel statusLabel;
//
//    public CalculatorGUI() {
//        initializeComponents();
//        setupLayout();
//        setupEventHandlers();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setTitle("RMI Calculator Client");
//        setSize(400, 500);
//        setLocationRelativeTo(null);
//    }
//
//    private void initializeComponents() {
//        // Text fields
//        num1Field = new JTextField(15);
//        num2Field = new JTextField(15);
//        resultField = new JTextField(15);
//        resultField.setEditable(false);
//
//        // Buttons
//        connectBtn = new JButton("Connect to Server");
//        addBtn = new JButton("Add (+)");
//        subBtn = new JButton("Subtract (-)");
//        mulBtn = new JButton("Multiply (*)");
//        divBtn = new JButton("Divide (/)");
//        powBtn = new JButton("Power (^)");
//        sqrtBtn = new JButton("Square Root");
//        clearBtn = new JButton("Clear");
//
//        // Status label
//        statusLabel = new JLabel("Not connected to server");
//        statusLabel.setForeground(Color.RED);
//
//        // Initially disable operation buttons
//        setOperationButtonsEnabled(false);
//    }
//
//    private void setupLayout() {
//        setLayout(new BorderLayout());
//
//        // Title panel
//        JPanel titlePanel = new JPanel();
//        titlePanel.add(new JLabel("RMI Calculator", JLabel.CENTER));
//        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        add(titlePanel, BorderLayout.NORTH);
//
//        // Input panel
//        JPanel inputPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));
//
//        gbc.insets = new Insets(5, 5, 5, 5);
//
//        gbc.gridx = 0; gbc.gridy = 0;
//        inputPanel.add(new JLabel("Number 1:"), gbc);
//        gbc.gridx = 1;
//        inputPanel.add(num1Field, gbc);
//
//        gbc.gridx = 0; gbc.gridy = 1;
//        inputPanel.add(new JLabel("Number 2:"), gbc);
//        gbc.gridx = 1;
//        inputPanel.add(num2Field, gbc);
//
//        gbc.gridx = 0; gbc.gridy = 2;
//        inputPanel.add(new JLabel("Result:"), gbc);
//        gbc.gridx = 1;
//        inputPanel.add(resultField, gbc);
//
//        // Button panel
//        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 5, 5));
//        buttonPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
//        buttonPanel.add(addBtn);
//        buttonPanel.add(subBtn);
//        buttonPanel.add(mulBtn);
//        buttonPanel.add(divBtn);
//        buttonPanel.add(powBtn);
//        buttonPanel.add(sqrtBtn);
//        buttonPanel.add(clearBtn);
//
//        // Control panel
//        JPanel controlPanel = new JPanel();
//        controlPanel.add(connectBtn);
//
//        // Status panel
//        JPanel statusPanel = new JPanel();
//        statusPanel.add(statusLabel);
//
//        // Main panel
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.add(inputPanel, BorderLayout.NORTH);
//        mainPanel.add(buttonPanel, BorderLayout.CENTER);
//        mainPanel.add(controlPanel, BorderLayout.SOUTH);
//
//        add(mainPanel, BorderLayout.CENTER);
//        add(statusPanel, BorderLayout.SOUTH);
//    }
//
//    private void setupEventHandlers() {
//        connectBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                connectToServer();
//            }
//        });
//
//        addBtn.addActionListener(e -> performOperation("add"));
//        subBtn.addActionListener(e -> performOperation("subtract"));
//        mulBtn.addActionListener(e -> performOperation("multiply"));
//        divBtn.addActionListener(e -> performOperation("divide"));
//        powBtn.addActionListener(e -> performOperation("power"));
//        sqrtBtn.addActionListener(e -> performOperation("sqrt"));
//
//        clearBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                num1Field.setText("");
//                num2Field.setText("");
//                resultField.setText("");
//            }
//        });
//    }
//
//    private void connectToServer() {
//        try {
//            calculator = (CalculatorInterface) Naming.lookup("//localhost:1099/Calculator");
//            statusLabel.setText("Connected to server");
//            statusLabel.setForeground(Color.GREEN);
//            connectBtn.setText("Reconnect");
//            setOperationButtonsEnabled(true);
//            JOptionPane.showMessageDialog(this, "Successfully connected to server!",
//                    "Connection Status", JOptionPane.INFORMATION_MESSAGE);
//        } catch (Exception e) {
//            statusLabel.setText("Failed to connect to server");
//            statusLabel.setForeground(Color.RED);
//            setOperationButtonsEnabled(false);
//            JOptionPane.showMessageDialog(this, "Failed to connect to server:\n" + e.getMessage(),
//                    "Connection Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void performOperation(String operation) {
//        if (calculator == null) {
//            JOptionPane.showMessageDialog(this, "Please connect to server first!",
//                    "Not Connected", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        try {
//            double num1 = 0, num2 = 0, result = 0;
//
//            // For square root, we only need one number
//            if ("sqrt".equals(operation)) {
//                if (num1Field.getText().trim().isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Please enter a number for square root!",
//                            "Input Error", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//                num1 = Double.parseDouble(num1Field.getText().trim());
//                result = calculator.sqrt(num1);
//            } else {
//                // For other operations, we need two numbers
//                if (num1Field.getText().trim().isEmpty() || num2Field.getText().trim().isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Please enter both numbers!",
//                            "Input Error", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//
//                num1 = Double.parseDouble(num1Field.getText().trim());
//                num2 = Double.parseDouble(num2Field.getText().trim());
//
//                switch (operation) {
//                    case "add":
//                        result = calculator.add(num1, num2);
//                        break;
//                    case "subtract":
//                        result = calculator.subtract(num1, num2);
//                        break;
//                    case "multiply":
//                        result = calculator.multiply(num1, num2);
//                        break;
//                    case "divide":
//                        result = calculator.divide(num1, num2);
//                        break;
//                    case "power":
//                        result = calculator.power(num1, num2);
//                        break;
//                }
//            }
//
//            resultField.setText(String.valueOf(result));
//
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Please enter valid numbers!",
//                    "Input Error", JOptionPane.ERROR_MESSAGE);
//        } catch (RemoteException e) {
//            JOptionPane.showMessageDialog(this, "Remote operation failed:\n" + e.getMessage(),
//                    "Remote Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void setOperationButtonsEnabled(boolean enabled) {
//        addBtn.setEnabled(enabled);
//        subBtn.setEnabled(enabled);
//        mulBtn.setEnabled(enabled);
//        divBtn.setEnabled(enabled);
//        powBtn.setEnabled(enabled);
//        sqrtBtn.setEnabled(enabled);
//    }
//}