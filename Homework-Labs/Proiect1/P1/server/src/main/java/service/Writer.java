package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    void write(String string) {
        try (FileWriter fileWriter = new FileWriter("output.txt",true)) {
            fileWriter.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
