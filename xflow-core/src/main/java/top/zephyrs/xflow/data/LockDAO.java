package top.zephyrs.xflow.data;

public interface LockDAO {

    int insert(Long resource);

    int delete(Long resource);
}
