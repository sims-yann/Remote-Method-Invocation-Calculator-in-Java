package org.server;

import org.interfaces.CalculatorInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImpl extends UnicastRemoteObject implements CalculatorInterface {

    public CalculatorImpl() throws RemoteException {
        super();
    }

    @Override
    public double add(double a, double b) throws RemoteException {
        System.out.println("Server: Performing addition: " + a + " + " + b);
        return a + b;
    }

    @Override
    public double subtract(double a, double b) throws RemoteException {
        System.out.println("Server: Performing subtraction: " + a + " - " + b);
        return a - b;
    }

    @Override
    public double multiply(double a, double b) throws RemoteException {
        System.out.println("Server: Performing multiplication: " + a + " * " + b);
        return a * b;
    }

    @Override
    public double divide(double a, double b) throws RemoteException {
        if (b == 0) {
            throw new RemoteException("Division by zero is not allowed");
        }
        System.out.println("Server: Performing division: " + a + " / " + b);
        return a / b;
    }

    @Override
    public double power(double base, double exponent) throws RemoteException {
        System.out.println("Server: Performing power: " + base + " ^ " + exponent);
        return Math.pow(base, exponent);
    }

    @Override
    public double sqrt(double number) throws RemoteException {
        if (number < 0) {
            throw new RemoteException("Square root of negative number is not allowed");
        }
        System.out.println("Server: Performing square root of: " + number);
        return Math.sqrt(number);
    }
}