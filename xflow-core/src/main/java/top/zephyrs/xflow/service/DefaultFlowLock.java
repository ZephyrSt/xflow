package top.zephyrs.xflow.service;

import top.zephyrs.xflow.data.LockDAO;
import top.zephyrs.xflow.exceptions.FlowException;

/**
 * 默认的流程状态变更的空实现
 */
public class DefaultFlowLock implements FlowLock {

    private final LockDAO lockMapper;

    public DefaultFlowLock(LockDAO lockMapper) {
        this.lockMapper = lockMapper;
    }

    @Override
    public void tryLock(Long currentId) throws RuntimeException {
        try {
            boolean isGetLock = lockMapper.insert(currentId) > 0;
            if(!isGetLock) {
                throw new FlowException("Failed to acquire the lock. Please try again.");
            }
        }catch (Exception e) {
            throw new FlowException("Failed to acquire the lock. Please try again.");
        }
    }

    @Override
    public void unLock(Long currentId) {
        lockMapper.delete(currentId);
    }

}
