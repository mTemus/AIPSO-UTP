package operations;

import swarmCore.Particle;
import swarmCore.SwarmAlgorithm;

import java.util.Scanner;

public class StartingOperations {

    public static void main (String[] args) throws InterruptedException {
        if (args.length == 1 && args[0].equals("-p")) {
            menu(true);
        } else {
            System.out.print("Use the parameter '-p' to change the inertia, ");
            System.out.println("cognitive and social components.");
            System.out.println("Otherwise the default values will be: ");
            System.out.println("Inertia:             " + SwarmAlgorithm.DEFAULT_INERTIA);
            System.out.println("Cognitive Component: " + SwarmAlgorithm.DEFAULT_COGNITIVE);
            System.out.println("Social Component:    " + SwarmAlgorithm.DEFAULT_SOCIAL);
            menu(false);
        }
    }

    private static void menu (boolean flag) throws InterruptedException {
        SwarmAlgorithm swarm;
        Particle.FunctionType function;
        int particles, epochs;
        double inertia, cognitive, social;

        function = getFunction();
        particles = getUserInt("Particles: ");
        epochs = getUserInt("Epochs:    ");

        if (flag) {
            inertia = getUserDouble("Inertia:   ");
            cognitive = getUserDouble("Cognitive: ");
            social = getUserDouble("Social:    ");
            swarm = new SwarmAlgorithm(function, particles, epochs, inertia, cognitive, social);
        } else {
            swarm = new SwarmAlgorithm(function, particles, epochs);

        }

        swarm.run();
    }

    private static Particle.FunctionType getFunction () {
        Particle.FunctionType function = null;
        do {
            Scanner sc = new Scanner(System.in);
            printMenu();

            if (sc.hasNextInt()) {
                function = getFunction(sc.nextInt());
            } else {
                System.out.println("Invalid input.");
            }

        } while (function == null);
        return function;
    }

    private static int getUserInt (String msg) {
        int input;
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print(msg);

            if (sc.hasNextInt()) {
                input = sc.nextInt();

                if (input <= 0) {
                    System.out.println("Number must be positive.");
                } else {
                    break;
                }

            } else {
                System.out.println("Invalid input.");
            }
        }
        return input;
    }

    private static double getUserDouble (String msg) {
        double input;
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print(msg);

            if (sc.hasNextDouble()) {
                input = sc.nextDouble();

                if (input <= 0) {
                    System.out.println("Number must be positive.");
                } else {
                    break;
                }

            } else {
                System.out.println("Invalid input.");
            }
        }
        return input;
    }

    private static void printMenu () {
        System.out.println("----------------------------MENU----------------------------");
        System.out.println("Select a function:");
        System.out.println("1. (x^4)-2(x^3)");
        System.out.println("2. Ackley's Function");
        System.out.println("3. Booth's Function");
        System.out.println("4. Three Hump Camel Function");
        System.out.print("Function:  ");
    }

    private static Particle.FunctionType getFunction (int input) {
        if (input == 1)         return Particle.FunctionType.FunctionA;
        else if (input == 2)    return Particle.FunctionType.Ackleys;
        else if (input == 3)    return Particle.FunctionType.Booths;
        else if (input == 4)    return Particle.FunctionType.ThreeHumpCamel;
        System.out.println("Invalid Input.");
        return null;
    }


}
