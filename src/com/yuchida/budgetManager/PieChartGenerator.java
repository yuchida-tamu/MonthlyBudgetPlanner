package com.yuchida.budgetManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartGenerator {
    private HashMap<String, Integer> dataList;
    private ObservableList<PieChart.Data> chartData;
    private PieChart chart;

    public PieChartGenerator(){
        this.chartData = FXCollections.observableArrayList();
        this.dataList = new HashMap<>();
        this.chart = new PieChart();
    }

    public PieChartGenerator(PieChart.Data data){
        this();

        this.chartData.add(data);
        this.dataList.put(data.getName(), this.chartData.indexOf(data));

    }

    public PieChartGenerator(ObservableList<PieChart.Data> list){
        this();
        this.chartData = list;
        for(PieChart.Data data : list){
            this.dataList.put(data.getName(), this.chartData.indexOf(data));
        }
    }

    public ObservableList<PieChart.Data> getChartData() {
        return chartData;
    }



    public Map<String, Integer> getDataList(){
        return this.dataList;
    }

    public void addToDataList(String label, PieChart.Data data){
        chartData.add(data);
        dataList.put(data.getName(), this.chartData.indexOf(data));
        System.out.println("label:" + label);
    }

    public void updateDataItem(String label, int num){
        int i = dataList.get(label);
        PieChart.Data data = new PieChart.Data(label, num);
        chartData.get(i).setPieValue(num);
    }

    public PieChart.Data getData(String name){
        int i = dataList.get(name);
        return chartData.get(i);
    }


    public void generatePieChart(Pane pane, String categoryName){
        chart.setData(chartData);
        chart.setTitle(categoryName);
        pane.getChildren().setAll(chart);
    }

    public void updateChart(String name){
        Pane pane = (Pane) chart.getParent();
        pane.getChildren().clear();
        generatePieChart(pane, name);
    }
}
