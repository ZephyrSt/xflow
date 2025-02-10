package top.zephyrs.xflow.data.handers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.utils.JSONUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public abstract class AbstractListJsonTypeHandler<T> extends BaseTypeHandler<List<T>> {

    private final Class<T> type;

    public AbstractListJsonTypeHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter== null?null: toJson(parameter));
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value==null?null: this.fromJson(value, type);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value==null?null: this.fromJson(value, type);
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value==null?null: this.fromJson(value, type);
    }

    protected String toJson(List<T> obj){
        return JSONUtils.toJson(obj);
    }

    protected List<T> fromJson(String json, Class<T> type) {
        return JSONUtils.fromJsonList(json, type);
    }
}
