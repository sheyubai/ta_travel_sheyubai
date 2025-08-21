import cn.wolfcode.wolf2w.business.controller.admin.SearchAdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class demo1 {
    @Autowired
    private SearchAdminController searchAdminController;
    @Test
    public void test() {
        searchAdminController.dataInit();
    }
}
