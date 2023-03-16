package GraphStreamSketch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataBackup {
    int DataNodeNum;
    String[] DataNodeIps;
    String[] DataNodeFileNames;
    String MasterFileName;

    public DataBackup(int dataNodeNum, String[] dataNodeIps, String[] dataNodeFileNames, String masterFileName) {
        DataNodeNum = dataNodeNum;
        DataNodeIps = dataNodeIps;
        DataNodeFileNames = dataNodeFileNames;
        MasterFileName = masterFileName;

    }

    //暂时数据是平均分配的 通过测试
    public void generateBackUpData() throws IOException {
        //确保过半数的节点拥有数据副本
        int copyNum = DataNodeNum / 2 + (DataNodeNum % 2 != 0 ? 1 : 0);

        File file = new File(MasterFileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        //  OutputStream[copyNum] output = new FileOutputStream(localFileName);

        ArrayList<FileOutputStream> outputs = new ArrayList<>();
        for (int i = 0; i < DataNodeNum; i++) {
            outputs.add( new FileOutputStream(DataNodeFileNames[i]));
        }


        String line;

        int count = -1;
        //用于控制轮转
        int[] rotates = new int[DataNodeNum];
        for (int i = 0; i < DataNodeNum; i++) {
            rotates[i] = i;
        }
        //用于控制对于rotates的指向
        int point = 0;

        while ((line = br.readLine()) != null) {
            count++;
            //跳过首行
            if (count == 0) {
                continue;
            }
            //采用轮转方式每次选择恰过半数数据节点，如有5个节点：0，1，2； 1，2，3； 2，3，4； 3，4，0； 4，0，1；......
            for (int i = 0; i < copyNum; i++) {
                outputs.get(rotates[point]).write((line + "\r\n").getBytes(StandardCharsets.UTF_8) );
                point = (point + 1) % DataNodeNum;
            }


        }
        br.close();
        //输出总共的有效行数
      //  System.out.println("the number of lines: " + count);


    }


}
