import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class testGetIP {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        System.out.println(Arrays.toString(address.getAddress()));
        //需要这个作为GSS的成员变量
        System.out.println(address.getHostAddress());


        System.out.println(address);
    }
}
