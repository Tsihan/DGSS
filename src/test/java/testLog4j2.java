import FTPServer.FTPUtil;

import java.io.InputStream;



public class testLog4j2 {
    public static void main(String[] args) {
        int ftpPort = 21;
        String ftpUserName = "qihan";
        String ftpPassword = "zpy010408";
        String ftpHost = "";

        FTPUtil testFTPUtil = new FTPUtil(ftpHost,ftpPort,ftpUserName,ftpPassword);
        try {
            InputStream in = FTPUtil.class.getClassLoader().getResourceAsStream("log4j2.xml");
            if (in == null) {
                System.out.println("1111");
                testFTPUtil.logger.info("配置文件env.properties读取失败!");
            } else {
                testFTPUtil.logger.info("配置文件env.properties读取成功!");
                System.out.println("2222");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
