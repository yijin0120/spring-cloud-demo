import cy.protocols.HelloService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ThriftTest
 * @Auther: yijin
 * @Date: 2019-09-26 17:26
 * @Description:
 */
@Service
public class ThriftTest{
    public static final String SERVER_IP = "192.168.10.131";
    public static final int SERVER_PORT = 9000;
    public static final int TIMEOUT = 3000000;

    public static void main(String[] args) {
        TTransport tTransport = new TSocket(SERVER_IP,SERVER_PORT,TIMEOUT);
        TProtocol tProtocol = new TBinaryProtocol(tTransport);
        HelloService.Client client = new HelloService.Client(tProtocol);
        try {
            tTransport.open();
            System.out.println(client.helloString("阿里斯柯达"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            tTransport.close();
        }
    }

}
