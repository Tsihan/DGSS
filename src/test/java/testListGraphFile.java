import GraphStreamSketch.ListGraph;

import java.io.*;

public class testListGraphFile {
    public static void main(String[] args) throws IOException {
        ListGraph myListGraph = new ListGraph();
        String fileName = "src/main/resources/out.wiki_talk_ht";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        int count = -1;
        while ((line = br.readLine()) != null) {
            count++;
            //跳过首行
            if (count == 0) {
                continue;
            }
            String[] temp = line.split(" ");
            myListGraph.insert(temp[0], temp[1], 1, Integer.parseInt(temp[2]));

        }
        br.close();
        //输出总共的有效行数
        System.out.println("the number of lines: " + count);

        System.out.println(myListGraph.nodequery("444", 1, 0));
        System.out.println(myListGraph.nodequery("444", 1, 1));
        System.out.println(myListGraph.nodeValueQuery("444", 1, 0));
        System.out.println(myListGraph.nodeValueQuery("444", 1, 1));


    }
}
