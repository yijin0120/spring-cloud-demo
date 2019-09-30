package cy.protocols;

import lombok.SneakyThrows;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

public class ThriftServer {

    /**
     * 启动thrift服务器
     * @param args
     */
    public final static int SERVER_PORT = 9000;
    public static void startServer() {
            System.out.println("服务端开启....");
            TProcessor tprocessor = new HelloService.Processor<HelloService.Iface>(new HelloServiceImpl());
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT,30000);
            TServer.Args args = new TServer.Args(serverTransport);
            TBinaryProtocol.Factory portFactory = new TBinaryProtocol.Factory(true, true);
            args.processor(tprocessor);
            args.protocolFactory(portFactory);
            TServer server = new TSimpleServer(args);
            server.serve();
    }

    public static void main(String[] args) {
        ThriftServer server = new ThriftServer();
        server.startServer();
    }

}