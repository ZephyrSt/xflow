package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import top.zephyrs.xflow.data.LockDAO;

@Mapper
public interface LockMapper extends LockDAO {

    @Override
    @Insert("insert into x_flow_lock(resource, create_time) " +
            "select #{resource}, current_timestamp() from dual " +
            "where not exists(select 1 from x_flow_lock where resource = #{resource})")
    int insert(Long resource);

    @Override
    @Delete("delete from x_flow_lock where resource=#{resource}")
    int delete(Long resource);
}
