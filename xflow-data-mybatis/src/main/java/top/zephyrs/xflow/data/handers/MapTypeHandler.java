package top.zephyrs.xflow.data.handers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import top.zephyrs.xflow.entity.config.Config;

import java.util.Map;

@MappedTypes(Map.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MapTypeHandler extends AbstractJsonTypeHandler<Map> {

    public MapTypeHandler() {
        super(Map.class);
    }

}
