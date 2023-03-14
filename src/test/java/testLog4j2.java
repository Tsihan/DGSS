import FTPServer.FTPUtil;

import java.io.InputStream;

import static FTPServer.FTPUtil.logger;

public class testLog4j2 {
    public static void main(String[] args) {
        int ftpPort = 0;
        String ftpUserName = "";
        String ftpPassword = "";
        String ftpHost = "";
        String ftpPath = "";
        String writeTempFielPath = "";
        try {
            InputStream in = FTPUtil.class.getClassLoader().getResourceAsStream("log4j2.xml");
            if (in == null) {
                System.out.println("1111");
                logger.info("配置文件env.properties读取失败!");
            } else {
                logger.info("配置文件env.properties读取成功!");
                System.out.println("2222");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
