import GraphStreamSketch.DataBackup;

import java.io.IOException;

public class testDataBackup {
    public static void main(String[] args) throws IOException {
        int dataNodeNum = 5;
        String[] dataNodeIps = {"", "", "", "", ""};
        ;
        String[] dataNodeFileNames = {"src/main/resources/out.wiki_talk_ht1", "src/main/resources/out.wiki_talk_ht2",
                "src/main/resources/out.wiki_talk_ht3", "src/main/resources/out.wiki_talk_ht4",
                "src/main/resources/out.wiki_talk_ht5"};

        String masterFileName = "src/main/resources/out.wiki_talk_ht";
        DataBackup test = new DataBackup(dataNodeNum, dataNodeIps, dataNodeFileNames, masterFileName);
        test.generateBackUpData();
    }
}
