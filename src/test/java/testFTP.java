import FTPServer.FTPUtil;

import java.io.InputStream;
import java.util.Properties;

import static FTPServer.FTPUtil.*;

public class testFTP {
    public static void main(String[] args) {
        int ftpPort = 21;
        String ftpUserName = "qihan";
        String ftpPassword = "zpy010408";
        String ftpHost = "127.0.0.1";
        String ftpPath = "/home/qihan/GraphStreamSketch4GraduationProject/wiki_talk_ht";
        String writeTempFilePath = "E:\\DGSS\\src\\main\\resources";
        try {
            InputStream in = FTPUtil.class.getClassLoader().getResourceAsStream("log4j2.xml");
            if (in == null) {
                System.out.println("1111");
                logger.info("配置文件env.properties读取失败!");
            } else {
                logger.info("配置文件env.properties读取成功!");
                System.out.println("2222");


                String result = readFileFromFTP(ftpUserName, ftpPassword, ftpPath, ftpHost, ftpPort, "meta.wiki_talk_ht");
                System.out.println("读取配置文件结果为：" + result);


                ftpPath = ftpPath + "/" + "meta.wiki_talk_htTEST";
                upload(ftpPath, ftpUserName, ftpPassword, ftpHost, ftpPort, result, writeTempFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

