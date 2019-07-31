package ltc.beihu.core.mongo.boot.dao;

import ltc.beihu.core.mongo.boot.model.MongoFile;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @Project: [crawler-api]
 * @Package: [com.pengshu.ops.dao]
 * @Description: [Grid文件读写Dao]
 * @Author: [toming]
 * @CreateDate: [2/15/17 5:29 PM]
 * @Version: [v1.0]
 */
public interface GridDao {

    boolean exist(String gridId);

    String save(MongoFile mongoFile);

    boolean delete(String gridId);

    MongoFile read(String gridId);

    List<MongoFile> query(Query query);
}
