package com.example.xflow;

import top.zephyrs.xflow.configs.XFlowConfig;
import top.zephyrs.xflow.configs.XFlowConstants;
import top.zephyrs.xflow.entity.config.*;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.entity.users.UserFilter;
import top.zephyrs.xflow.enums.EdgeTypeEnum;
import top.zephyrs.xflow.enums.NodeTypeEnum;
import top.zephyrs.xflow.enums.VoteTypeEnum;
import top.zephyrs.xflow.utils.JSONUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestConfigBuilder {



    public Config getTestConfig(){
        Config config = new Config();
        config.setConfigId(10000000L);
        config.setConfigName("测试流程");
        config.setConfigCode("test_code");
        config.setGroupId(1000L);
        config.setRemark("测试");

        List<ConfigNode> nodes = Arrays.asList(startNode(), endNode(), approvalNode(), jointlyNode(), condition1Node(), condition2Node(), claimNode(), voteNode());

        List<ConfigEdge> edges = new ArrayList<>();
        edges.add(new ConfigEdge("1", EdgeTypeEnum.approved, "start", "approval_1", null));
        edges.add(new ConfigEdge("2", EdgeTypeEnum.approved, "approval_1", "jointly_2", null));
        edges.add(new ConfigEdge("3", EdgeTypeEnum.approved, "jointly_2", "condition_3_1", null));
        edges.add(new ConfigEdge("4", EdgeTypeEnum.approved, "jointly_2", "condition_3_2", null));
        edges.add(new ConfigEdge("5", EdgeTypeEnum.approved, "condition_3_1", "claim_4", null));
        edges.add(new ConfigEdge("6", EdgeTypeEnum.approved, "condition_3_2", "vote_5", null));
        edges.add(new ConfigEdge("8", EdgeTypeEnum.approved, "vote_5", "claim_4", null));
        edges.add(new ConfigEdge("7", EdgeTypeEnum.approved, "claim_4", "end", null));

        config.setNodes(nodes);
        config.setEdges(edges);

        return config;
    }


    private ConfigNode startNode(){
        ConfigNode start = new ConfigNode();
        start.setId("start");
        start.setType(NodeTypeEnum.start);
        ConfigNodeData nodeData = new ConfigNodeData();
        nodeData.setTitle("申请人提交");

        //指定人员
        UserFilter uf = new UserFilter();
        uf.setType(XFlowConstants.USER_FILTER_TYPE_USER);
        uf.setUsers(startUser());

        nodeData.setFilter(JSONUtils.fromJson(JSONUtils.toJson(uf), HashMap.class));
        start.setData(nodeData);
        return start;
    }

    public List<User> startUser() {
        return Arrays.asList(new User("start_user","测试申请人"));
    }

    private ConfigNode endNode(){
        ConfigNode start = new ConfigNode();
        start.setId("end");
        start.setType(NodeTypeEnum.end);
        ConfigNodeData nodeData = new ConfigNodeData();
        nodeData.setTitle("结束");
        start.setData(nodeData);
        return start;
    }

    private ConfigNode approvalNode(){
        ConfigNode node = new ConfigNode();
        node.setId("approval_1");
        node.setType(NodeTypeEnum.approval);
        ConfigNodeData nodeData = new ConfigNodeData();
        nodeData.setTitle("或签1");

        //指定人员
        UserFilter uf = new UserFilter();
        uf.setType(XFlowConstants.USER_FILTER_TYPE_USER);

        uf.setUsers(approvalUser());

        nodeData.setFilter(JSONUtils.fromJson(JSONUtils.toJson(uf), HashMap.class));
        node.setData(nodeData);
        return node;
    }

    public List<User> approvalUser() {
        List<User> users = new ArrayList<>();
        users.add(new User("approval_1_user1","或签节点审核人1"));
        users.add(new User("approval_1_user2","或签节点审核人2"));
        return users;
    }


    private ConfigNode jointlyNode(){
        ConfigNode node = new ConfigNode();
        node.setId("jointly_2");
        node.setType(NodeTypeEnum.jointly);
        ConfigNodeData nodeData = new ConfigNodeData();
        nodeData.setTitle("会签2");

        //指定人员
        UserFilter uf = new UserFilter();
        uf.setType(XFlowConstants.USER_FILTER_TYPE_USER);
        uf.setUsers(jointlyUser());

        nodeData.setFilter(JSONUtils.fromJson(JSONUtils.toJson(uf), HashMap.class));
        node.setData(nodeData);
        return node;
    }

    public List<User> jointlyUser() {
        List<User> users = new ArrayList<>();
        users.add(new User("jointly_2_user1","会签节点审核人1"));
        users.add(new User("jointly_2_user2","会签节点审核人2"));
        users.add(new User("jointly_2_user3","会签节点审核人3"));
        return users;
    }


    private ConfigNode condition1Node(){
        ConfigNode node = new ConfigNode();
        node.setId("condition_3_1");
        node.setType(NodeTypeEnum.condition);
        ConfigNodeData nodeData = new ConfigNodeData();
        nodeData.setTitle("条件分支3");
        nodeData.setConditional("age>18");
        node.setData(nodeData);
        return node;
    }

    private ConfigNode condition2Node(){
        ConfigNode node = new ConfigNode();
        node.setId("condition_3_2");
        node.setType(NodeTypeEnum.condition);
        ConfigNodeData nodeData = new ConfigNodeData();
        nodeData.setTitle("条件分支3");
        nodeData.setConditional("age<=18");
        node.setData(nodeData);
        return node;
    }

    private ConfigNode claimNode(){
        ConfigNode node = new ConfigNode();
        node.setId("claim_4");
        node.setType(NodeTypeEnum.claim);
        ConfigNodeData nodeData = new ConfigNodeData();
        nodeData.setTitle("会签2");

        //指定人员
        UserFilter uf = new UserFilter();
        uf.setType(XFlowConstants.USER_FILTER_TYPE_USER);
        uf.setUsers(claimUser());
        nodeData.setFilter(JSONUtils.fromJson(JSONUtils.toJson(uf), HashMap.class));

        node.setData(nodeData);
        return node;
    }



    public List<User> claimUser() {
        List<User> users = new ArrayList<>();
        users.add(new User("claim_4_user1","认领节点审核人1"));
        users.add(new User("claim_4_user2","认领节点审核人2"));
        users.add(new User("claim_4_user3","认领节点审核人3"));
        return users;
    }


    private ConfigNode voteNode(){
        ConfigNode node = new ConfigNode();
        node.setId("vote_5");
        node.setType(NodeTypeEnum.vote);
        ConfigNodeData nodeData = new ConfigNodeData();
        nodeData.setTitle("票签");
        nodeData.setVote(VoteTypeEnum.rate);
        nodeData.setWeight(new BigDecimal("0.6"));
        //指定人员
        UserFilter uf = new UserFilter();
        uf.setType(XFlowConstants.USER_FILTER_TYPE_USER);
        uf.setUsers(voteUser());
        nodeData.setFilter(JSONUtils.fromJson(JSONUtils.toJson(uf), HashMap.class));

        node.setData(nodeData);
        return node;
    }


    public List<User> voteUser() {
        List<User> users = new ArrayList<>();
        users.add(new User("vote_5_user1","投票节点审核人1"));
        users.add(new User("vote_5_user2","投票节点审核人2"));
        users.add(new User("vote_5_user3","投票节点审核人3"));
        return users;
    }


}
