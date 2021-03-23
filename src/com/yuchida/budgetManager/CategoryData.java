package com.yuchida.budgetManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CategoryData {
    private String filename;
    private static ObservableMap<String, BreakdownItem> breakdownItems = FXCollections.observableHashMap();

    public CategoryData(String filename){
        this.filename = filename;


    }

    public static void saveData(String filename,ObservableMap<String, BreakdownItem> items ) throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{

            for(Object obj : items.entrySet()){
                ObservableMap.Entry<String, BreakdownItem> entry = (ObservableMap.Entry<String, BreakdownItem>) obj;
                BreakdownItem item = entry.getValue();

                bw.write(String.format("%s\t%s",
                        item.getName(),
                        item.getAmount()));
                bw.newLine();

            }
        } finally {
            if(bw != null){
                bw.close();
            }
        }
    }


    public static ObservableMap<String, BreakdownItem> getBreakdownItems(){
        return breakdownItems;
    }

    public static List<BreakdownItem> getItemList(){
        List list = FXCollections.observableArrayList();
        for(Object obj : breakdownItems.entrySet()){
            BreakdownItem item =((Map.Entry<String, BreakdownItem>) obj).getValue();
            list.add(item);
        }
        return list;
    }

    public void addBudgets(BreakdownItem item){
        breakdownItems.put(item.getName(),item);
    }

    public static void loadCategoryData(String filename)throws IOException{
        breakdownItems = FXCollections.observableHashMap();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);
        String input;

        try{
            while((input = br.readLine()) != null){
                String[] itemPieces = input.split("\t");

                BreakdownItem item = new BreakdownItem(itemPieces[0], Double.parseDouble(itemPieces[1]));
                breakdownItems.put(item.getName(),item);
            }
        } catch (IOException e){
            if(br != null){
                br.close();
            }
        }

    }



//    public void storeBudgets(ObservableMap<String, BreakdownItem> items) throws IOException {
//        Path path = Paths.get(this.filename);
//        BufferedWriter bw = Files.newBufferedWriter(path);
//        try{
////            Iterator<BreakdownItem> iter = items.iterator();
////            while (iter.hasNext()){
////                BreakdownItem item= iter.next();
////                bw.write(String.format("%s\t%s",
////                        item.getName(),
////                        item.getAmount()));
////                bw.newLine();
////            }
//            items.put("grocery",new BreakdownItem("grocery"));
//            System.out.println(items.toString());
//            for(Object obj : items.entrySet()){
//                ObservableMap.Entry<String, BreakdownItem> entry = (ObservableMap.Entry<String, BreakdownItem>) obj;
//                BreakdownItem item = entry.getValue();
//                System.out.println(item.toString());
//                System.out.println(item.toString());
//                bw.write(String.format("%s\t%s",
//                        item.getName(),
//                        item.getAmount()));
//                bw.newLine();
//
//            }
//        } finally {
//            if(bw != null){
//                bw.close();
//            }
//        }
//    }
}
