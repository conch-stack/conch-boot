package ltd.beihu.core.tools.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Gson工具类
 */
public class GsonUtils {

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter())
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
        private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }

            try {
                Date date = format.parse(json.getAsString());
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }

        public JsonElement serialize(Timestamp src, Type arg1, JsonSerializationContext arg2) {
            String dateFormatAsString = format.format(new Date(src.getTime()));
            return new JsonPrimitive(dateFormatAsString);
        }
    }

    public static String toJson(Object bean) {
        try {
            return bean == null ? null : gson.toJson(bean);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJson(String str, Class<T> classOfT) {
        return gson.fromJson(str, classOfT);
    }

    public static <T> T fromJson(String str, Type classOfT) {
        return gson.fromJson(str, classOfT);
    }


    /**
     * 这里实际上,因type erasure的原因,只能解析出List，无法确定component type
     * 例如：
     * <p>
     * String json = "[1,2,3,4,5]";
     * List<String> stringList = GsonUtils.toList(json);
     * </p>
     * 此时，实际得到的stringList类型是 List<Double>
     * 如果执行        String first = stringList.get(0);
     * 将得到一个java.lang.ClassCastException
     * 所以调用此方法时,仅当可预测得到的返回类型，可以指定T
     * 否则，应使用List<Object>接受返回值
     */
    public static <T> List<T> toList(String str) {
        return gson.fromJson(str, new TypeToken<List<T>>() {
        }.getType());
    }
}
