package cy.protocols;

import java.util.ArrayList;
import java.util.List;

public class HelloServiceImpl implements HelloService.Iface{
    public String helloString(String string) {
        StringBuffer buffer = new StringBuffer(string);

        List<Integer> a = new ArrayList<>();
        return buffer.reverse().toString();
    }
}