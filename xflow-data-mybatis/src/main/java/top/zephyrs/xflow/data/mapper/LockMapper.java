package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import top.zephyrs.xflow.data.LockDAO;

@Mapper
public interface LockMapper extends LockDAO {

    @Insert("insert into x_flow_lock(resource, create_time) " +
            "select #{resource}, current_timestamp() from dual " +
            "where not exists(select 1 from x_flow_lock where resource = #{resource})")
    @Override
    int insert(Long resource);

    @Delete("delete from x_flow_lock where resource=#{resource}")
    @Override
    int delete(Long resource);
}
