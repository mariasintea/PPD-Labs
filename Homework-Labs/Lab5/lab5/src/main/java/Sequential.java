import dataStructures.Monom;
import dataStructures.OrderedLinkedList;

import java.io.*;
import java.util.*;

public class Sequential {
    private OrderedLinkedList result;
    private Queue<Monom> monoms;

    public Sequential(){
        result = new OrderedLinkedList();
        monoms = new ArrayDeque<>();
    }

    private void readPolinom(String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> nrs = Arrays.asList(line.split(" "));
                Monom monom = new Monom(Integer.parseInt(nrs.get(0)), Integer.parseInt(nrs.get(1)));
                monoms.add(monom);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void readData(){
        String fileName = "data/test1/polinom";
        for (int i = 1; i <= 10; i++)
            readPolinom(fileName + i + ".txt");
    }

    private void calculateSum(){
        while (!monoms.isEmpty()){
            Monom currentMonom = monoms.peek();
            boolean found = false;
            for (Monom monom : result.getAll()) {
                if (monom.getExponent() == currentMonom.getExponent()) {
                    monom.setCoefficient(monom.getCoefficient() + currentMonom.getCoefficient());
                    found = true;
                }
            }
            if (!found)
                result.add(currentMonom);
            monoms.poll();
        }
    }

    private void writeResult(){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter("data/result.txt", false))) {
            for (Monom monom : result.getAll())
                if (monom.getCoefficient() != 0) {
                    bW.write(monom.getCoefficient() + " " + monom.getExponent());
                    bW.newLine();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        readData();
        calculateSum();
        writeResult();
    }
}
