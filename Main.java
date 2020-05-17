package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String mode = "enc";
        String data = "";
        boolean isData = false;
        String input = "";
        String output = "";
        boolean isInput = false;
        boolean isOutput = false;
        int key = 0;
        AlgorithmInvoker alg = new AlgorithmInvoker();
        String coded = "";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode":
                    mode = args[i + 1];
                    i++;
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    i++;
                    break;
                case "-data":
                    data = args[i + 1];
                    isData = true;
                    i++;
                    break;
                case "-in":
                    input = args[i + 1];
                    isInput = true;
                    i++;
                    break;
                case "-out":
                    output = args[i + 1];
                    isOutput = true;
                    i++;
                    break;
                case "-alg":
                    if ("unicode".equals(args[i + 1])) {
                        alg.setAlgorithm(new UnicodeAlgorithm());
                    } else if ("shift".equals(args[i + 1])) {
                        alg.setAlgorithm(new ShiftAlgorithm());
                    } else {
                        alg.setAlgorithm(new ShiftAlgorithm());
                    }
                    break;
            }
        }

        char[] originalArray = data.toCharArray();
        if (!isData && isInput) {
            try {
                String message = new String(Files.readAllBytes(Paths.get(input)));
                originalArray = message.toCharArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mode.equals("enc")) {
            coded = alg.encrypt(originalArray, key);
        } else if (mode.equals("dec")) {
            coded = alg.decrypt(originalArray, key);
        }

        if (isOutput) {
            try {
                FileWriter fileWriter = new FileWriter(output);
                fileWriter.write(coded);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(coded);
        }

    }
}
class AlgorithmInvoker {
    Algorithm algorithm;

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String encrypt(char[] message, int key) {
        return algorithm.encrypt(message, key);
    }

    public String decrypt(char[] message, int key) {
        return algorithm.decrypt(message, key);
    }
}

interface Algorithm {
    public String encrypt(char[] message, int key);
    public  String decrypt(char[] message, int key);
}

class ShiftAlgorithm implements Algorithm {

    @Override
    public String encrypt(char[] message, int key) {
        for (int i = 0; i < message.length; i++) {
            if ('A' <= message[i] && 'Z' >= message[i]) {
                message[i] += key;
                if ('Z' < message[i]) {
                    message[i] -= 26;
                }
            } else if ('a' <= message[i] && 'z' >= message[i]) {
                message[i] += key;
                if ('z' < message[i]) {
                    message[i] -= 26;
                }
            }
        }
        return new String(message);
    }

    @Override
    public String decrypt(char[] message, int key) {
        for (int i = 0; i < message.length; i++) {
            if ('A' <= message[i] && 'Z' >= message[i]) {
                message[i] -= key;
                if ('A' > message[i]) {
                    message[i] += 26;
                }
            } else if ('a' <= message[i] && 'z' >= message[i]) {
                message[i] -= key;
                if ('a' > message[i]) {
                    message[i] += 26;
                }
            }
        }
        return new String(message);
    }
}

class UnicodeAlgorithm implements Algorithm {

    @Override
    public String encrypt(char[] message, int key) {

        for (int i = 0; i < message.length; i++) {
                message[i] += key;
        }
        return new String(message);
    }

    @Override
    public String decrypt(char[] message, int key) {
        for (int i = 0; i < message.length; i++) {
            message[i] -= key;
        }
        return new String(message);
    }
}

