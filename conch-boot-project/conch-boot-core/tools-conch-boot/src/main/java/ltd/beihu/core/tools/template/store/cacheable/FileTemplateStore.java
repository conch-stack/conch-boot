package ltd.beihu.core.tools.template.store.cacheable;

import ltd.beihu.core.tools.properties.PropertiesUtil;
import ltd.beihu.core.tools.utils.FileUtils;
import ltd.beihu.core.tools.utils.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project: [ops]
 * @Description: [从文件中读取模板]
 * @Author: [toming]
 * @CreateDate: [9/19/16 11:22 AM]
 * @Version: [v1.0]
 */
public class FileTemplateStore extends AbstractCacheTemplateStore {

    /**
     * 存储文件信息
     */
    private Map<String, String> namePathMap = new ConcurrentHashMap<>();

    public void setDir(String... dirPath) {
        //todo:扫描文件夹，生成模板
        for (String dir : dirPath) {
            try (DirectoryStream<Path> ds = PropertiesUtil.getDir(dir)) {
                for (Path p : ds) {
                    File file = p.toFile();
                    namePathMap.put(FileUtils.getName(file.getName()), file.getCanonicalPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void cache(String name, String content) {
        //不允许写文件,至少暂时用不上,so～
        throw new UnsupportedOperationException("FileTemplateStore cannot cache tl");
    }

    @Override
    protected String getInSelf(String name) {
        String path = namePathMap.get(name);
        String result = null;
        try (InputStream in = new FileInputStream(new File(path))) {
            result = StreamUtils.readFromInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected boolean containInSelf(String name) {
        return namePathMap.containsKey(name);
    }
}
