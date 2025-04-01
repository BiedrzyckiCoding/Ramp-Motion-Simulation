package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

//TEST FOR THE JFREECHART, GOOD SIMULATION IN RampMotionSimulation
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Line Chart Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Sample dataset
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue(1.0, "Series1", "2020");
            dataset.addValue(4.0, "Series1", "2021");
            dataset.addValue(3.0, "Series1", "2022");
            dataset.addValue(5.0, "Series1", "2023");

            // Create chart
            JFreeChart chart = ChartFactory.createLineChart(
                    "Yearly Data",       // Chart title
                    "Year",              // X-axis label
                    "Value",             // Y-axis label
                    dataset              // Dataset
            );

            ChartPanel chartPanel = new ChartPanel(chart);
            frame.setContentPane(chartPanel);
            frame.setVisible(true);
        });
    }
}
