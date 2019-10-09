package ltd.beihu.core.beihubootmongosample;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/7/31
 * @Version: V1.0.0
 */
@Service
public class TestService {

    @Resource
    private AppliedTreasureDao appliedTreasureDao;

    @PostConstruct
    public void test() {
        long count = appliedTreasureDao.count();
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/------------------------------count:: " + count);

        AppliedTreasure appliedTreasure = new AppliedTreasure();
        appliedTreasure.setAn("test");
        appliedTreasure.setC("dddddddd");
        appliedTreasureDao.save(appliedTreasure);

        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/------------------------------saved");

        count = appliedTreasureDao.count();
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/---------------------------------------------------------------------------------------------");
        System.out.println("/------------------------------count:: " + count);
    }
}
