package top.zephyrs.xflow.data.handers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import top.zephyrs.xflow.entity.config.Config;
import top.zephyrs.xflow.entity.users.User;

import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class UserListTypeHandler extends AbstractListJsonTypeHandler<User> {
    public UserListTypeHandler() {
        super(User.class);
    }
}
