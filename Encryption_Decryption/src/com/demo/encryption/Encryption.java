package com.demo.encryption;

        import java.io.FileWriter;
        import java.io.IOException;
        import java.io.File;
        import java.util.Scanner;
        import java.util.Arrays;

class Main {
    //A list of the main use variables
    public static void main(String[] args) {
        String mode = "enc";
        int key = 0;
        String text = "";
        boolean outPut = false;
        String path = "";
        String alg = "shift";
        
        //this for loop changes the value of the argumenent taken depending on the user input
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-mode")) {
                mode = args[i + 1];
            }
            if (args[i].equals("-in")) {
                File file = new File(args[i + 1]);
                text = getText(file);
            } else if (args[i].equals("-data")) {
                text = args[i + 1];
            }
            if (args[i].equals("-key")) {
                key = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("-out")) {
                outPut = true;
                path = args[i + 1];
                //System.out.println("Works at this point");

            }
            if (args[i].equals("-alg")) {
                alg = args[i + 1];
            }
        }
            //This if statement switches between encoding or decoding depending on eariler input from user
            //Note in retrospect a swtich statement might be a better solution
        if (mode.equals("enc")) {
            if (!outPut) {
                System.out.println(Encrypt(text, key, alg));
            } else if (outPut) {
                File outFile = new File(path);
                try (FileWriter writer = new FileWriter(outFile)) {
                    writer.write(Encrypt(text, key, alg));
                } catch (IOException e) {
                    System.out.println("Error " + e.getMessage());
                }
            }
        } else if (mode.equals("dec")) {
                //This option calls the decrypt method 
            if (!outPut) {
                System.out.println(Decrypt(text, key, alg));
            } else if (outPut) {
                File outFile = new File(path);
                try (FileWriter writer = new FileWriter(outFile)) {
                    writer.write(Decrypt(text, key, alg));
                    System.out.println(text);
                    System.out.println(Decrypt(text, key, alg));
                } catch (IOException e) {
                    System.out.println("Error " + e.getMessage());
                }
            }
        }
    }

    //This is a method to call a file and turn it to text the program can read
    public static String getText(File file) {
        String fileText = "";
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                fileText = fileText + scan.nextLine();
            }
        } catch (IOException e) {
            System.out.println("Error Cannot read file " + e.getMessage());
        }
        return fileText;
    }
  
    // This method takes in a String called text, an int called key, and a String called alg
    // and returns an encrypted version of the text input.
    // Depending on if the values are below 127 or above 32 the encrpytion uses a shift cypher to make the
    //encrpyted text, the shift is the value of the key
    public static String Encrypt(String text, int key, String alg) {
        String output = new String();
        if (alg.equals("unicode")) {
            for (int i = 0; i < text.length(); i++) {
                int num = text.charAt(i);
                if (num < 127 && num > 31) {
                    if (num + key < 127) {
                        // Create a new char by casting the sum of the Unicode value and the key to a char
                        char newChar = (char) (num + key);
                        output = output + newChar;
                    } else if (num + key > 126) {
                        int newNum = 32 + (num + key - 127);
                        char newChar = (char) newNum;
                        output = output + newChar;
                    }
                } else {
                    char oldChar = (char) num;
                    output = output + oldChar;
                }
            }
        } else if (alg.equals("shift")) {
            for (int i = 0; i < text.length(); i++) {
                int num = text.charAt(i);
                if (num > 96 && num < 123) {
                    if (num + key < 123) {
                        char newChar = (char) (num + key);
                        output = output + newChar;
                    } else if (num + key > 122) {
                        int newNum = 97 + (num + key - 123);
                        char newChar = (char) newNum;
                        output = output + newChar;
                    }
                } else if (num > 64 && num < 91) {
                    if (num + key < 91) {
                        char newChar = (char) (num + key);
                        output = output + newChar;
                    } else if (num + key > 90) {
                        int newNum = 65 + (num + key - 91);
                        char newChar = (char) newNum;
                        output = output + newChar;
                    }
                } else {
                    char oldChar = (char) num;
                    output = output + oldChar;
                }
            }
        }
        return output;
    }
        
    //32 = space
    // 126 = ~
        
    //This is the method to decrypt the string taking the text, the key and algorithm as arguments
    //returns the decrypted message using the reverse shift cypher
    // the shift is dependent on the Unicode value of the characters (above 32 below 127) and the key
    public static String Decrypt(String text, int key, String alg) {
        String output = new String();
        if (alg.equals("unicode")) {
            for (int i = 0; i < text.length(); i++) {
                int num = text.charAt(i);
                if (num < 127 && num > 31) {
                    if (num - key > 31) {
                        char newChar = (char) (num - key);
                        output = output + newChar;
                    } else if (num - key < 32) {
                        int newNum = 126 - (num - key + 32);
                        char newChar = (char) newNum;
                        output = output + newChar;
                    }
                }
            }
        } else if (alg.equals("shift")) {
            for (int i = 0; i < text.length(); i++) {
                int num = text.charAt(i);
                if (num < 123 && num > 96) {
                    if (num - key > 96) {
                        char newChar = (char) (num - key);
                        output = output + newChar;
                    } else if (num - key < 97) {
                        int newNum = 122 - (96 - (num - key));
                        char newChar = (char) newNum;
                        output = output + newChar;
                    }
                } else if (num < 91 && num > 64) {
                    if (num - key > 64) {
                        char newChar = (char) (num - key);
                        output = output + newChar;
                    } else if (num - key < 65) {
                        int newNum = 90 - (64 - (num - key));
                        char newChar = (char) newNum;
                        output = output + newChar;
                    }
                } else {
                    char oldChar = (char) num;
                    output = output + oldChar;
                }
            }
        }
        return output;
    }
}

