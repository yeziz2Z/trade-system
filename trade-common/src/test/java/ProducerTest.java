import com.alibaba.rocketmq.client.producer.SendResult;
import com.liuk.trade.common.exception.LiukMQException;
import com.liuk.trade.common.rocketmq.LiukMQProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by kl on 2018/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:xml/spring-mq-producer.xml")
public class ProducerTest {
    @Autowired
    private LiukMQProducer producer;

    @Test
    public void testProducer() throws LiukMQException {
        SendResult sendResult = this.producer.sendMessage("TestTopic", "order", "123456789", "this is order message");
        System.out.println(sendResult);

    }

}
