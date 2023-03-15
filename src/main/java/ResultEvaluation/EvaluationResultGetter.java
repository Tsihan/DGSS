package ResultEvaluation;

import GraphStreamSketch.GSS;
import GraphStreamSketch.ListEdge;
import GraphStreamSketch.ListGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EvaluationResultGetter {
    /**
     * 获得原始数据集去重的边集合
     */
    public static Set<String> getDeduplicatedEdges(String fileName) throws IOException {
        Set<String> DeduplicatedEdges = new HashSet<>();

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        int count = -1;
        //填充进去全部的边
        while ((line = br.readLine()) != null) {
            count++;
            //跳过首行
            if (count == 0) {
                continue;
            }
            String[] temp = line.split("\\s+");
            DeduplicatedEdges.add(temp[0] + " " + temp[1]);


        }
        br.close();

        return DeduplicatedEdges;

    }

    public static float getAverageRelativeError(GSS gss, ListGraph listgraph, String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        int count = -1;
        //填充进去全部的边
        while ((line = br.readLine()) != null) {
            count++;
            //跳过首行
            if (count == 0) {
                continue;
            }
            String[] temp = line.split("\\s+");

            gss.insert(temp[0], temp[1], Integer.parseInt(temp[2]));
            listgraph.insert(temp[0], temp[1], 1, Integer.parseInt(temp[2]));
        }
        br.close();

        Set<String> DeduplicatedEdges = getDeduplicatedEdges(fileName);

        float SumRelativeError = 0;
        float EdgeNum = DeduplicatedEdges.size();
        //最终返回结果
        float ARE = 0;

        for (String pair : DeduplicatedEdges) {
            String[] edges = pair.split("\\s+");
          //  System.out.println(edges[0]+"==="+edges[1]);
            float trueValue = listgraph.query(edges[0], edges[1], 1);
            float predicateValue = gss.edgeQuery(edges[0], edges[1]);
            SumRelativeError += Math.abs(predicateValue / trueValue - 1);
        }

        ARE = SumRelativeError / EdgeNum;

        return ARE;

    }
    //todo
//    public static float getAveragePrecision() {
//
//    }
//
//
//    public static float getCompressionRatio() {
//
//    }
//
//
//    public static float getBufferPercentage() {
//
//    }
}
