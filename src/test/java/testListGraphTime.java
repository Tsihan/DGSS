import GraphStreamSketch.ListGraph;

import java.io.*;

public class testListGraphTime {

    public static void main(String[] args) throws IOException {
        ListGraph myListGraph = new ListGraph();
       // String fileName = "src/main/resources/out.wiki_talk_ht";
        //换更大的数据集
        String fileName = "src/main/resources/out.wiki_talk_lv";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        int count = -1;
        // 开始时间
        long stime1 = System.currentTimeMillis();

        while ((line = br.readLine()) != null) {
            count++;
            //跳过首行
            if (count == 0) {
                continue;
            }
            String[] temp = line.split("\\s+");
            myListGraph.insert(temp[0], temp[1], 1, Integer.parseInt(temp[2]));

        }

        // 结束时间
        long etime1 = System.currentTimeMillis();
        // 计算执行时间
        long gap1 = etime1 - stime1;
        System.out.println("将原始文件通过ListGraph压缩执行时长为" + gap1 + "毫秒.");


        br.close();
        //输出总共的有效行数
        System.out.println("the number of lines: " + count);
        // 开始时间
        long stime2 = System.currentTimeMillis();
        System.out.println(myListGraph.nodequery("444", 1, 0));
        // 结束时间
        long etime2 = System.currentTimeMillis();
        // 计算执行时间
        long gap2 = etime2 - stime2;
        System.out.println("ListGraph返回对于特定节点某一标签出方向/入方向的度数时长为" + gap2 + "毫秒.");


        System.out.println(myListGraph.nodequery("444", 1, 1));
        // 开始时间
        long stime3 = System.currentTimeMillis();
        System.out.println(myListGraph.nodeValueQuery("444", 1, 0));
        // 结束时间
        long etime3 = System.currentTimeMillis();
        // 计算执行时间
        long gap3 = etime3 - stime3;
        System.out.println("ListGraph返回对于特定节点某一标签出方向/入方向权重之和时长为" + gap3 + "毫秒.");



        System.out.println(myListGraph.nodeValueQuery("444", 1, 1));


    }
}
