import com.liuk.trade.entity.TradeUser;
import com.liuk.trade.mapper.TradeUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by kl on 2018/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:xml/spring-dao.xml")
public class TestDao {

    @Autowired
    private TradeUserMapper userMapper;

    @Test
    public void test(){
        TradeUser user = new TradeUser();
        user.setUserName("李四");
        user.setUserPassword("123456");
        int insert = userMapper.insert(user);
        System.out.println(user.getUserId());
    }

}
