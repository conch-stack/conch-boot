package ltd.beihu.core.beihubootwebsample.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/8/1
 * @Version: V1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestHttpDto {

    private String client;
    private String promotionCode;
    private String deviceCode;
    private String tempUserId;
    private String phone;
    private int actionType;
    private String eventId;

    public static TestHttpDto mock() {
        TestHttpDto testHttpDto = new TestHttpDto(
                "h5",
                "00015",
                "mozilla/5.0 (macintosh; intel mac os x 10_14_5) applewebkit/537.36 (khtml, like gecko) chrome/75.0.3770.142 safari/537.36",
                "7165820641505603",
                "",
                1,
                "");
        return testHttpDto;
    }
}
