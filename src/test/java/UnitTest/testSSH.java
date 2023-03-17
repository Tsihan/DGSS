package UnitTest;

import SSHServer.SSHUtil;

//通过测试
public class testSSH {
    public static void main(String[] args) {
       // String cmd = "ls -1";
        // String cmd = "pwd";
        String cmd = "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1yg8awl79fuu58yp9kwxm52b7.jar testSerializationDeserialization";
        //默认会在该用户目录下
        SSHUtil shell = new SSHUtil("127.0.0.1", "qihan", "zpy010408", 2222);
        String execLog = shell.execCommand(cmd);
        System.out.println(execLog);
    }
}
