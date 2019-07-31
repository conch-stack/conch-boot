package ltd.beihu.core.beihubootmongosample;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author: LiQiang
 * @CreateDate: 2019-03-07.
 * @Description: 应用宝
 * @Version: 1.0.0
 */
@Data
@Document
public class AppliedTreasure {

    @Id
    protected String _id;
    protected String c;     //  机构名称    company
    protected String an;    //  app名称	AppName
    protected String sn;    //  简称  short_name
    protected Integer d;    //  下载量   downloads
    protected Double sc;    //  评分	Score
    protected String t;     //  分类	type
    protected String v;     //  版本号	version
    protected Double s;     //  大小	size
    protected Long vu;      //  版本更新时间	version_updatetime
    protected String logo;  //  logo
    protected String du;    //  download_url
    protected List<String> img;     //  截图	images
    protected String info;  //  应用信息	info
    private Integer state;  //  数据状态    0：脏数据  1：正常
    protected Long ct;      //  创建时间    create_time
    protected Long ut;      //  修改时间    update_time
}
