import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.graalvm.compiler.lir.StandardOp.NoOp;

/**
 * @author Vincent Moeykens
 */

 /**
  * Main class
  */
class Main {
    public static void main(String[] args) {
        Knapsack knap = new Knapsack();
        File inputFile = promptFile();
        try {
            knap.parseFile(inputFile);
            knap.runAlgorithm();
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    /**
     * Prompts the user for a file and will loop until a valid filename is inetered
     * @return File object from the filename the user entered
     */
    public static File promptFile() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a filename for input file: ");
        String fileName = in.nextLine();
        File lecturesFile = new File(fileName);
        
        while(!lecturesFile.exists()){
            System.out.print("Invalid file name! Try again: ");
            fileName = in.nextLine();    
            lecturesFile = new File(fileName);
        }
        in.close();
        return lecturesFile;
    }

}

class Knapsack {
    int maxWeight; 
    ArrayList<Item> items;

    public Knapsack() {
        this.maxWeight = -1;
    }

    public void parseFile(File fileName) throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName)); 
        ArrayList<Item> out = new ArrayList<Item>();
        String line; 
        line = br.readLine();
        this.setMaxWeight(Integer.parseInt(line));
        while ((line = br.readLine()) != null)  {
            String[] numbers = line.split("\\s+");
            Item entry = new Item(Integer.parseInt(numbers[1]), 
                                  Integer.parseInt(numbers[2]), 
                                  Integer.parseInt(numbers[0]));
            out.add(entry);
        } 
        this.setItems(out);
        br.close();
    }

    public void runAlgorithm() {
        System.out.println('x');;
    }

    public int getMaxWeight() {
        return this.maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

}

class Item {
    int value;
    int weight;
    int index;

    public Item(int value, int weight, int index) {
        this.value = value;
        this.weight = weight;
        this.index = index;
    }

    public int getValue() {
        return this.value;
    }
    public int getWeight() {
        return this.weight;
    }
    
    public int getIndex() {
        return this.index;
    }

    @Override
    public String toString() {
        return "Item " + getIndex() + "(Value=" + getValue() + 
                ", Weight=" + getWeight() + ")";
    }

}