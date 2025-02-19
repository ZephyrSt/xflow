package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.Query;
import top.zephyrs.xflow.entity.config.Config;
import top.zephyrs.xflow.entity.config.ConfigVO;

import java.util.List;

public interface ConfigDAO {

    int insert(Config config);

    int updateById(Config config);

    Config selectById(Long configId);

    Config selectByCode(String configCode);

    List<ConfigVO> selectVOByQuery(Query query);
}
