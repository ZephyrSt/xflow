package top.zephyrs.xflow.service;

/**
 * 根据用户筛选信息获取用户
 */
public interface FlowLock {

    /**
     * 获取节点办理的锁
     * @param currentId 节点ID
     * @throws RuntimeException 获取锁失败
     */
    void tryLock(Long currentId) throws RuntimeException;

    /**
     * 获取节点办理的锁
     * @param currentId 节点ID
     * @throws RuntimeException 获取锁失败
     */
    void unLock(Long currentId);
}
