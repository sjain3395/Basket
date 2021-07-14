
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.management.loading.ClassLoaderRepository;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DistributeOrderDSL {
    public static void main(String[] args) throws Exception {
    }

    CamelContext context = new DefaultCamelContext();

    {
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:DistributerOrderDSL")
                            .split(xpath("//order[@product='soaps']/items")).to("stream.out");
                    // .to("file:src/main/resources/order/");
                }
            });
            context.start();
            ProducerTemplate producerTemplate = context.createProducerTemplate();
            InputStream orderInputStream = new FileInputStream(ClassLoader.getSystemClassLoader()
                    .getResource("order.xml").getFile());
            producerTemplate.sendBody("direct:DistributeOrderDSL", orderInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                context.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}