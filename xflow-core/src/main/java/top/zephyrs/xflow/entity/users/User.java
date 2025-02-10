package top.zephyrs.xflow.entity.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 处理流程的人员信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userId;
    private String userName;
}
