package top.zephyrs.xflow.data.handers;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import top.zephyrs.xflow.utils.JSONUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractJsonTypeHandler<T> extends BaseTypeHandler<T> {

    protected final Log log = LogFactory.getLog(this.getClass());

    private final Class<T> type;

    public AbstractJsonTypeHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter== null?null: toJson(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value==null?null: this.fromJson(value, type);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value==null?null: this.fromJson(value, type);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value==null?null: this.fromJson(value, type);
    }

    protected String toJson(Object obj){
        return JSONUtils.toJson(obj);
    }

    protected T fromJson(String json, Class<T> type) {
        return JSONUtils.fromJson(json, type);
    }
}
