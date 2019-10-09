package ltd.beihu.core.tools.code;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public abstract class AbstractServiceCode implements ServiceCode{

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (ServiceCode.class.isAssignableFrom(obj.getClass())) {
            return this.getCode() == ((ServiceCode) obj).getCode();
        } else {
            return false;
        }
    }
}
