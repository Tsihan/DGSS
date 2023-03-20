package ResultEvaluation;

import FTPServer.FTPUtil;
import GraphStreamSketch.GSS;
import GraphStreamSketch.ListGraph;
import org.apache.lucene.util.RamUsageEstimator;

import java.io.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.math.BigIntegerMath.factorial;


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


    /**
     * 获得原始数据集去重的节点ID
     */
    public static Set<String> getDeduplicatedNodes(String fileName) throws IOException {
        Set<String> DeduplicatedNodes = new HashSet<>();

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
            DeduplicatedNodes.add(temp[0]);
            DeduplicatedNodes.add(temp[1]);

        }
        br.close();

//        for (String node: DeduplicatedNodes) {
//            System.out.println(node);
//        }
//        System.out.println(DeduplicatedNodes.size());

        return DeduplicatedNodes;

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

            //  gss.insert(temp[0], temp[1], Integer.parseInt(temp[2]));
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

    public static float getAveragePrecision(GSS gss, ListGraph listgraph, String fileName) throws IOException {
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

        Set<String> DeduplicatedNodes = getDeduplicatedNodes(fileName);

        float SumPrecision = 0;
        float NodeNum = DeduplicatedNodes.size();
        //最终返回结果
        float AP = 0;

        for (String node : DeduplicatedNodes) {

            //  System.out.println(edges[0]+"==="+edges[1]);
            int trueValue = listgraph.nodequery(node, 1, 0);

            int predicateValue = gss.nodeDegreeQuery(node, 0);

            //避免出现除数为0的情况
            int usedTrueValue = trueValue + 1;
            int usedPredicateValue = predicateValue + 1;
            if (usedPredicateValue >= usedTrueValue) {
                SumPrecision += (float) usedTrueValue / (float) usedPredicateValue;
            } else {
                SumPrecision += (float) usedPredicateValue / (float) usedTrueValue;
            }

//            if (trueValue == 0) {
//                if (predicateValue == 0) {
//                    SumPrecision += 1;
//                }
//            } else {
//                SumPrecision += (float) trueValue / (float) predicateValue;
//            }

        }

        AP = SumPrecision / NodeNum;

        return AP;
    }


    public static float getCompressionRatio(GSS gss, ListGraph listgraph, String fileName) throws IOException {
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

            //  gss.insert(temp[0], temp[1], Integer.parseInt(temp[2]));
            listgraph.insert(temp[0], temp[1], 1, Integer.parseInt(temp[2]));
        }
        br.close();
        String size4gss = RamUsageEstimator.humanSizeOf(gss);
        long size4gss1 = RamUsageEstimator.sizeOf(gss);

        String size4listgraph = RamUsageEstimator.humanSizeOf(listgraph);
        long size4listgraph1 = RamUsageEstimator.sizeOf(listgraph);

        System.out.println(size4gss);
        System.out.println(size4listgraph);
        System.out.println(1 - ((float) size4gss1 / (float) size4listgraph1));

        return 1 - ((float) size4gss1 / (float) size4listgraph1);
    }


    public static float getBufferPercentage(GSS gss, String fileName) throws IOException {
        // File file = new File(fileName);
        //FileInputStream fis = new FileInputStream(file);
        // InputStreamReader isr = new InputStreamReader(fis);
//        BufferedReader br = new BufferedReader(isr);

        String line;
        int count = -1;
        //填充进去全部的边
//        while ((line = br.readLine()) != null) {
//            count++;
//            //跳过首行
//            if (count == 0) {
//                continue;
//            }
//            String[] temp = line.split("\\s+");
//
//            gss.insert(temp[0], temp[1], Integer.parseInt(temp[2]));
//
//        }
//        br.close();

        int Num4DeduplicatedEdges = getDeduplicatedEdges(fileName).size();
        int Num4BufferedEdges = gss.buffer.size();
        System.out.println(Num4DeduplicatedEdges);
        System.out.println(Num4BufferedEdges);

        System.out.println((float) Num4BufferedEdges / (float) Num4DeduplicatedEdges);
        return (float) Num4BufferedEdges / (float) Num4DeduplicatedEdges;

    }

    public static float combination(int n, int m) {
        return (factorial(n).divide(factorial(m).multiply(factorial(n - m)))).floatValue();
    }

    public static float getRevisionFactor(int dataNodeNum) {
        int m = dataNodeNum;
        int n = dataNodeNum / 2 + (dataNodeNum % 2 != 0 ? 1 : 0);
        float sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i * combination(n, i) * combination(m, n - i) / combination(m, i);
        }

        return sum;

    }

    public static void getAllDistributedResultsTxt(ArrayList<FTPUtil> ftputils, ListGraph listgraph, String fileName, int dataNodeNum) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        //备份数目，恰过半数
        int copyNum = dataNodeNum / 2 + (dataNodeNum % 2 != 0 ? 1 : 0);

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

            //gss.insert(temp[0], temp[1], Integer.parseInt(temp[2]));
            listgraph.insert(temp[0], temp[1], 1, Integer.parseInt(temp[2]));

        }
        br.close();


        System.out.println("====After revision====");


        float SumPrecision = 0;
        float SumRelativeError = 0;

        System.out.println("======AP=====");
        Set<String> DeduplicatedNodes = getDeduplicatedNodes(fileName);

        float NodeNum = DeduplicatedNodes.size();
        //最终返回结果
        float AP;
        ArrayList<ArrayList<String>> resultTxts = new ArrayList<>();
        for (int i = 0; i < copyNum; i++) {
            File fileResult = new File(ftputils.get(i).DeserializedLocalFileName);
            FileInputStream fisResult = new FileInputStream(fileResult);
            InputStreamReader isrResult = new InputStreamReader(fisResult);
            BufferedReader brResult = new BufferedReader(isrResult);

            ArrayList<String> tempArrayList = new ArrayList<>();

            while ((line = brResult.readLine()) != null) {
                String[] temp = line.split("\\s+");
                tempArrayList.addAll(Arrays.asList(temp));

            }
            resultTxts.add(tempArrayList);
            brResult.close();
        }
        //反序列化到内存

        //定位NodePredicatedValues和EdgePredicatedValues的位置
        int NodePredicatedValuesPosition = -1;
        int EdgePredicatedValuesPosition = -1;
        for (int i = 0; i < resultTxts.get(0).size(); i++) {
            if (resultTxts.get(0).get(i).equals("NodePredicatedValues")) {
                NodePredicatedValuesPosition = i;
            }

            if (resultTxts.get(0).get(i).equals("EdgePredicatedValues")) {
                EdgePredicatedValuesPosition = i;
            }

        }

        // k控制文本具体的内容
        int k = 1;
        for (String node : DeduplicatedNodes) {

            //  System.out.println(edges[0]+"==="+edges[1]);
            int trueValue = listgraph.nodequery(node, 1, 0);
            //====预测值使用平均值的向上取整
            float predicateValue = 0;
            //小循环 取平均值向上取整


            for (ArrayList<String> resultTxt : resultTxts) {
                float tempValue;
                tempValue = Float.parseFloat(resultTxt.get(NodePredicatedValuesPosition + k));

                predicateValue += tempValue / getRevisionFactor(dataNodeNum);
            }

            k = k + 1;


            //避免出现除数为0的情况
            int usedTrueValue = trueValue + 1;

            float usedPredicateValue = predicateValue + 1;

            if (usedPredicateValue >= usedTrueValue) {
                SumPrecision += (float) usedTrueValue / (float) usedPredicateValue;
            } else {
                SumPrecision += (float) usedPredicateValue / (float) usedTrueValue;
            }

        }
        AP = SumPrecision / NodeNum;
        System.out.println(AP);


        System.out.println("======ARE=====");
        Set<String> DeduplicatedEdges = getDeduplicatedEdges(fileName);

        float EdgeNum = DeduplicatedEdges.size();
        //最终返回结果
        float ARE = 0;
        //重新设定k
        k = 1;
        for (String pair : DeduplicatedEdges) {
            String[] edges = pair.split("\\s+");
            //  System.out.println(edges[0]+"==="+edges[1]);
            float trueValue = listgraph.query(edges[0], edges[1], 1);

            float predicateValue = 0;
            //小循环


            for (ArrayList<String> resultTxt : resultTxts) {
                float tempValue;
                tempValue = Float.parseFloat(resultTxt.get(EdgePredicatedValuesPosition + k));

                predicateValue += tempValue;
            }
            k = k + 1;
            predicateValue = (int) Math.ceil(predicateValue / (float) copyNum);
            // predicateValue = predicateValue / getRevisionFactor(dataNodeNum);


            SumRelativeError += Math.abs((predicateValue + 1) / (trueValue + 1) - 1);
        }
        ARE = SumRelativeError / EdgeNum;
        System.out.println(ARE);


    }

    public static void getAllDistributedResults(ArrayList<GSS> gsses, ListGraph listgraph, String fileName, int dataNodeNum) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        //备份数目，恰过半数
        int copyNum = dataNodeNum / 2 + (dataNodeNum % 2 != 0 ? 1 : 0);

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

            //gss.insert(temp[0], temp[1], Integer.parseInt(temp[2]));
            listgraph.insert(temp[0], temp[1], 1, Integer.parseInt(temp[2]));

        }
        br.close();


        for (GSS gss : gsses) {
            int Num4DeduplicatedEdges = getDeduplicatedEdges(fileName).size();
            int Num4BufferedEdges = gss.buffer.size();
            System.out.println("======buffer usage=====");
            System.out.println(Num4DeduplicatedEdges);
            System.out.println(Num4BufferedEdges);
            System.out.println((float) Num4BufferedEdges / (float) Num4DeduplicatedEdges);
            System.out.println("======Compression ratio=====");
            String size4gss = RamUsageEstimator.humanSizeOf(gss);
            long size4gss1 = RamUsageEstimator.sizeOf(gss);
            String size4listgraph = RamUsageEstimator.humanSizeOf(listgraph);
            long size4listgraph1 = RamUsageEstimator.sizeOf(listgraph);
            System.out.println(size4gss);
            System.out.println(size4listgraph);
            System.out.println(1 - ((float) size4gss1 / (float) size4listgraph1));
            System.out.println("======AP=====");
            Set<String> DeduplicatedNodes = getDeduplicatedNodes(fileName);
            float SumPrecision = 0;
            float NodeNum = DeduplicatedNodes.size();
            //最终返回结果
            float AP = 0;
            for (String node : DeduplicatedNodes) {

                //  System.out.println(edges[0]+"==="+edges[1]);
                int trueValue = listgraph.nodequery(node, 1, 0);

                int predicateValue = gss.nodeDegreeQuery(node, 0);
                //避免出现除数为0的情况
                int usedTrueValue = trueValue + 1;
                int usedPredicateValue = predicateValue + 1;

                if (usedPredicateValue >= usedTrueValue) {
                    SumPrecision += (float) usedTrueValue / (float) usedPredicateValue;
                } else {
                    SumPrecision += (float) usedPredicateValue / (float) usedTrueValue;
                }

            }
            AP = SumPrecision / NodeNum;
            System.out.println(AP);
            System.out.println("======ARE=====");
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
                SumRelativeError += Math.abs((predicateValue + 1) / (trueValue + 1) - 1);
            }
            ARE = SumRelativeError / EdgeNum;
            System.out.println(ARE);
        }


        //=======修正ARE以及AP之后的结果=======
        System.out.println("====After revision====");


        float SumPrecision = 0;
        float SumRelativeError = 0;

        System.out.println("======AP=====");
        Set<String> DeduplicatedNodes = getDeduplicatedNodes(fileName);

        float NodeNum = DeduplicatedNodes.size();
        //最终返回结果
        float AP = 0;
        for (String node : DeduplicatedNodes) {

            //  System.out.println(edges[0]+"==="+edges[1]);
            int trueValue = listgraph.nodequery(node, 1, 0);
            //====预测值使用平均值的向上取整
            float predicateValue = 0;
            //小循环 取平均值向上取整
            for (GSS temp : gsses) {
                predicateValue += (float) temp.nodeDegreeQuery(node, 0) / getRevisionFactor(dataNodeNum);
            }
            //似乎不用取平均
            //predicateValue = predicateValue/(float)gsses.size() ;
            //predicateValue = (int) Math.ceil((float) predicateValue / (float) copyNum);

            //避免出现除数为0的情况
            int usedTrueValue = trueValue + 1;

            float usedPredicateValue = predicateValue + 1;

            if (usedPredicateValue >= usedTrueValue) {
                SumPrecision += (float) usedTrueValue / (float) usedPredicateValue;
            } else {
                SumPrecision += (float) usedPredicateValue / (float) usedTrueValue;
            }

        }
        AP = SumPrecision / NodeNum;
        System.out.println(AP);


        System.out.println("======ARE=====");
        Set<String> DeduplicatedEdges = getDeduplicatedEdges(fileName);

        float EdgeNum = DeduplicatedEdges.size();
        //最终返回结果
        float ARE = 0;
        for (String pair : DeduplicatedEdges) {
            String[] edges = pair.split("\\s+");
            //  System.out.println(edges[0]+"==="+edges[1]);
            float trueValue = listgraph.query(edges[0], edges[1], 1);

            float predicateValue = 0;
            //小循环
            for (GSS temp : gsses) {
                predicateValue += temp.edgeQuery(edges[0], edges[1]);
            }
            predicateValue = (int) Math.ceil((float) predicateValue / (float) copyNum);
            // predicateValue = predicateValue / getRevisionFactor(dataNodeNum);


            SumRelativeError += Math.abs((predicateValue + 1) / (trueValue + 1) - 1);
        }
        ARE = SumRelativeError / EdgeNum;
        System.out.println(ARE);


    }


    public static void getAllResults(GSS gss, ListGraph listgraph, String fileName) throws IOException {
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

        int Num4DeduplicatedEdges = getDeduplicatedEdges(fileName).size();
        int Num4BufferedEdges = gss.buffer.size();
        System.out.println("======buffer usage=====");
        System.out.println(Num4DeduplicatedEdges);
        System.out.println(Num4BufferedEdges);
        System.out.println((float) Num4BufferedEdges / (float) Num4DeduplicatedEdges);
        System.out.println("======Compression ratio=====");
        String size4gss = RamUsageEstimator.humanSizeOf(gss);
        long size4gss1 = RamUsageEstimator.sizeOf(gss);
        String size4listgraph = RamUsageEstimator.humanSizeOf(listgraph);
        long size4listgraph1 = RamUsageEstimator.sizeOf(listgraph);
        System.out.println(size4gss);
        System.out.println(size4listgraph);
        System.out.println(1 - ((float) size4gss1 / (float) size4listgraph1));
        System.out.println("======AP=====");
        Set<String> DeduplicatedNodes = getDeduplicatedNodes(fileName);
        float SumPrecision = 0;
        float NodeNum = DeduplicatedNodes.size();
        //最终返回结果
        float AP = 0;
        for (String node : DeduplicatedNodes) {

            //  System.out.println(edges[0]+"==="+edges[1]);
            int trueValue = listgraph.nodequery(node, 1, 0);

            int predicateValue = gss.nodeDegreeQuery(node, 0);
            //避免出现除数为0的情况
            int usedTrueValue = trueValue + 1;
            int usedPredicateValue = predicateValue + 1;

            if (usedPredicateValue >= usedTrueValue) {
                SumPrecision += (float) usedTrueValue / (float) usedPredicateValue;
            } else {
                SumPrecision += (float) usedPredicateValue / (float) usedTrueValue;
            }


        }
        AP = SumPrecision / NodeNum;
        System.out.println(AP);
        System.out.println("======ARE=====");
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
            SumRelativeError += Math.abs((predicateValue + 1) / (trueValue + 1) - 1);
        }
        ARE = SumRelativeError / EdgeNum;
        System.out.println(ARE);


    }
}
