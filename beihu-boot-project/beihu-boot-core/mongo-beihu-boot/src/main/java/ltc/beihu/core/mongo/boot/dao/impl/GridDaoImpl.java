package ltc.beihu.core.mongo.boot.dao.impl;


import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import ltc.beihu.core.mongo.boot.dao.GridDao;
import ltc.beihu.core.mongo.boot.model.MongoFile;
import ltd.beihu.core.tools.utils.CollectionUtils;
import ltd.beihu.core.tools.utils.IDUtils;
import ltd.beihu.core.tools.utils.Md5Utils;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project: [crawler-api]
 * @Package: [com.pengshu.ops.dao.impl]
 * @Description: [GridFSB文件操作]
 * @Author: [toming]
 * @CreateDate: [2/16/17 2:37 PM]
 * @Version: [v1.0]
 */
public class GridDaoImpl implements GridDao {

    private GridFsTemplate gridFsTemplate;

    public GridDaoImpl(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public boolean exist(String gridId) {
        return gridFsTemplate.findOne(Query.query(Criteria.where("filename").is(gridId))) != null;
    }

    @Override
    public String save(MongoFile mongoFile) {
        String sMd5 = Md5Utils.md5(mongoFile.getContent()).toLowerCase();
        GridFSFile md5 = gridFsTemplate.findOne(Query.query(Criteria.where("md5").is(sMd5)));
        boolean isIdentical = md5 != null && mongoFile.getContent().length == md5.getLength();
        if (isIdentical) {
            return md5.getFilename();
        }
        mongoFile.setGridId(IDUtils.UUID());
        //region collect some fileInfo------------------------------------------
        Document fileInfo = new Document();
        fileInfo.put("filename", mongoFile.getFilename());
        fileInfo.put("contentType", mongoFile.getContentType());
        //endregion collect some fileInfo------------------------------------------

        //region store file rule return gridId------------------------------------
        gridFsTemplate.store(new ByteArrayInputStream(mongoFile.getContent()),
                mongoFile.getGridId(),//gridId as fileName
                mongoFile.getContentType(),
                fileInfo);//save fileInfo as metadata
        return mongoFile.getGridId();
        //endregion store file rule return gridId------------------------------------
    }

    @Override
    public boolean delete(String gridId) {
        try {
            gridFsTemplate.delete(Query.query(Criteria.where("filename").is(gridId)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public MongoFile read(String gridId) {
        GridFSFile gfsFile = gridFsTemplate.findOne(Query.query(Criteria.where("filename").is(gridId)));
        return gfsFile == null ? null : gfs2Mg(gfsFile);
    }

    @Override
    public List<MongoFile> query(Query query) {
        GridFSFindIterable gfsFiles = gridFsTemplate.find(query);
        return CollectionUtils.iterable2List(gfsFiles).stream().map(this::gfs2Mg).collect(Collectors.toList());
    }

    private MongoFile gfs2Mg(GridFSFile gridFSDBFile) {
        try {
            MongoFile mongoFile = new MongoFile();
            Document metaData = gridFSDBFile.getMetadata();
            mongoFile.setContentType(String.valueOf(metaData.get("contentType")));
            mongoFile.setFilename(String.valueOf(metaData.get("filename")));
            mongoFile.setCreateTime(gridFSDBFile.getUploadDate().getTime());
            mongoFile.setGridId(gridFSDBFile.getFilename());
            GridFsResource gridFsResource = gridFsTemplate.getResource(gridFSDBFile.getFilename());
            mongoFile.setContent(org.springframework.util.StreamUtils.copyToByteArray(gridFsResource.getInputStream()));
            return mongoFile;
        } catch (IOException e) {
            throw new RuntimeException("文件读取异常!");
        }
    }
}
