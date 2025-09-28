package org.server;

import org.interfaces.CalculatorInterface;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CalculatorServer {
    public static void main(String[] args) {
        try {
            // Create and start RMI registry on port 1099
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry started on port 1099");

            // Create calculator implementation
            CalculatorInterface calculator = new CalculatorImpl();

            // Bind the calculator object to the registry
            Naming.rebind("//localhost:1099/Calculator", calculator);

            System.out.println("Calculator Server is ready and waiting for clients...");
            System.out.println("Calculator service bound to: //localhost:1099/Calculator");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}