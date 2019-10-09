package ltd.beihu.core.tools.IdCard;

import java.util.Date;

/**
 * @Project: [CommonUtil]
 * @Package: [com.pengshu.common.IdCard]
 * @Description: [description]
 * @Author: [toming]
 * @CreateDate: [7/23/16 1:36 PM]
 * @Version: [v1.0]
 */
public interface IDCardInfo {
    String getZone();

    String getIdCard();

    void checkSex(Integer sex) throws InvalidException;

    Integer getSex();

    void checkInfo() throws InvalidException;

    Integer getBirthYear();

    Integer getBirthMonth();

    Integer getBirthDay();

    Date getBirthDate();
}
