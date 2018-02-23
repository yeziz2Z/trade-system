import com.liuk.trade.common.api.IUserApi;
import com.liuk.trade.common.protocol.user.QueryUserReq;
import com.liuk.trade.common.protocol.user.QueryUserRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by kl on 2018/2/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:xml/spring-rest-client.xml")
public class SpringRestClientTest {

    @Autowired
    private IUserApi userApi;

    @Test
    public void test(){
        QueryUserReq req = new QueryUserReq();
        req.setUserId(2);
        QueryUserRes queryUserRes = userApi.queryUserById(req);
        System.out.println(queryUserRes);
    }
}
