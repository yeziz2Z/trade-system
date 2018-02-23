import com.alibaba.rocketmq.common.message.MessageExt;
import com.liuk.trade.common.rocketmq.IMessageProcessor;

/**
 * Created by kl on 2018/2/17.
 */
public class TestProcessor implements IMessageProcessor {
    public boolean handleMessage(MessageExt messageExt) {
        System.out.println("收到消息："+messageExt.toString());
        return true;
    }
}
