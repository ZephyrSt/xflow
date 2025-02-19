package top.zephyrs.xflow.handlers;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.utils.JSONUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserListTypeHandler extends BaseTypeHandler<List<User>> {

    protected final Log log = LogFactory.getLog(this.getClass());


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<User> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter == null ? null : toJson(parameter));
    }

    @Override
    public List<User> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : this.fromJson(value);
    }

    @Override
    public List<User> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : this.fromJson(value);
    }

    @Override
    public List<User> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : this.fromJson(value);
    }

    protected String toJson(Object obj) {
        return JSONUtils.toJson(obj);
    }
    protected List<User> fromJson(String json) {
        return JSONUtils.fromJsonList(json, User.class);
    }

}
