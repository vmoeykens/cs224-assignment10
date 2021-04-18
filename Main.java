import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
            knap.computeOptimal();
        } catch (Exception e) {
            System.out.println(e);
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
    MemoizationTable memTab;

    public Knapsack() {
        this.maxWeight = -1;
    }

    public void parseFile(File fileName) throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName)); 
        ArrayList<Item> out = new ArrayList<Item>();
        String line; 
        line = br.readLine();
        this.setMaxWeight(Integer.parseInt(line));
        out.add(new Item(0, 0, 0));
        while ((line = br.readLine()) != null)  {
            String[] numbers = line.split("\\s+");
            Item entry = new Item(Integer.parseInt(numbers[1]), 
                                  Integer.parseInt(numbers[2]), 
                                  Integer.parseInt(numbers[0]));
            out.add(entry);
        } 
        this.setItems(out);
        this.memTab = new MemoizationTable(maxWeight, this.getItems().size());
        br.close();
    }

    public void runAlgorithm() {
        System.out.println("\nSolving knapsack weight capacity " + this.getMaxWeight() + ", with " + Integer.toString(this.getItems().size() - 1) + " items");
        System.out.println("\n");
        for (int item = 0; item < this.getItems().size(); item++) {
            for (int weight = 0; weight <= this.getMaxWeight(); weight++) {
                if (item == 0) {
                    this.memTab.setItem(weight, item, new MemItem(0, false));
                } else if(this.getItems().get(item).getWeight() > weight) {
                    this.memTab.setItem(weight, item, new MemItem(this.memTab.getItem(weight, item - 1).getVal(), false));
                } else {
                    this.memTab.setItem(weight, item, new MemItem(Math.max(this.memTab.getItem(weight, item - 1).getVal(), 
                                                                this.getItems().get(item).getValue() + this.memTab.getItem(weight - this.getItems().get(item).getWeight(), item - 1).getVal()), true));
                }
            }
            System.out.println("Memoization table, Row " + item + " completed");
            System.out.println(this.memTab);
        }
    }

    public void computeOptimal() {
        ArrayList<Item> optimal = new ArrayList<Item>();
        int numItems = this.getItems().size() - 1;
        int maxWeight = this.getMaxWeight();
        while (numItems > 0 && maxWeight > 0) {
            if (this.memTab.getItem(maxWeight, numItems).getVal() > this.memTab.getItem(maxWeight, numItems - 1).getVal()){
                optimal.add(this.items.get(numItems));
                maxWeight -= this.items.get(numItems).getWeight();
            } 
            numItems -= 1;
        }

        String contentsString = "";
        int optimalVal = 0;

        for (int i = 0; i < optimal.size(); i++) {
            contentsString += optimal.get(i) + "\n";
            optimalVal += optimal.get(i).getValue();
        }
        System.out.println("Knapsack with weight capacity " + this.getMaxWeight() + " has optimal value: " + optimalVal);
        System.out.println("\nKnapsack contains: \n" + contentsString);
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
        return "Item " + getIndex() + " (Value=" + getValue() + 
                ", Weight=" + getWeight() + ")";
    }

}

class MemoizationTable {
    ArrayList<ArrayList<MemItem>> table;
    
    MemoizationTable(int maxWeight, int numItems) {
        this.table = new ArrayList<ArrayList<MemItem>>();
        for (int weight = 0; weight <= maxWeight; weight++) {
            ArrayList<MemItem> temp = new ArrayList<>();
            for (int item = 0; item <= numItems; item++) {
                temp.add(new MemItem(-1, false));
            }
            this.table.add(temp);
        }
    }

    void setItem(int weight, int item, MemItem value) {
        this.table.get(weight).set(item, value);
    }

    MemItem getItem(int weight, int item) {
        return this.table.get(weight).get(item);
    }


    @Override
    public String toString() {
        String tableString = "";
        String tableDelim = "\n";
        for (int weight = 0; weight < this.table.size(); weight++) {
            tableString += "\t" + "| " + Integer.toString(weight);
            tableDelim += "--------";
        }
        tableDelim += "--------\n";
        tableString += tableDelim;
        for (int item = 0; item < this.table.get(0).size() - 1; item++) {
            tableString += Integer.toString(item);
            String rowString = "";
            for (int weight = 0; weight < this.table.size(); weight++) {
                rowString += "\t" + "| " + this.getItem(weight, item);
            }    
            tableString += rowString + "\n";
        }
        return tableString;
    }

}

class MemItem { 
    int val;
    Boolean include; 


    public MemItem(int val, Boolean include) {
        this.val = val;
        this.include = include;
    }

    public int getVal() {
        return this.val;
    }

    public Boolean isInclude() {
        return this.include;
    }

    public Boolean getInclude() {
        return this.include;
    }


    @Override
    public String toString() {
        return Integer.toString(getVal());
    }
    
}