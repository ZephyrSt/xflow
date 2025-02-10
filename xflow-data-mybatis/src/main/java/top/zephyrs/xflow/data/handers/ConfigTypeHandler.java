package top.zephyrs.xflow.data.handers;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import top.zephyrs.xflow.entity.config.Config;
import top.zephyrs.xflow.utils.JSONUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Config.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ConfigTypeHandler extends AbstractJsonTypeHandler<Config> {

    public ConfigTypeHandler() {
        super(Config.class);
    }

}
