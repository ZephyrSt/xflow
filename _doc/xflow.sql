drop table if exists x_flow_group;
drop table if exists x_flow_config;
drop table if exists x_flow_config_publish;

drop table if exists x_flow;
drop table if exists x_flow_node_current;
drop table if exists x_flow_node_current_log;
drop table if exists x_flow_task;
drop table if exists x_flow_task_log;
drop table if exists x_flow_lock;

drop table if exists x_flow_u_user_info;
drop table if exists x_flow_u_user_role;
drop table if exists x_flow_u_user_dept;
drop table if exists x_flow_u_role;
drop table if exists x_flow_u_dept;

create table if not exists x_flow_group
(
	group_id   bigint      not null comment '所属分组ID',
	group_name varchar(50) not null comment '名称',
	parent_id  bigint comment '上级ID',
	remark     varchar(50) comment '说明',
	primary key (group_id)
) comment '流程分组';

create table if not exists x_flow_config
(
	config_id   bigint      not null comment 'ID',
	config_name varchar(32) not null comment '流程定义名称',
	config_code varchar(32) not null comment '流程定义编码',
	group_id    bigint      not null comment '所属分组',
	remark      varchar(255) comment '说明',
	create_time datetime default current_timestamp comment '创建时间',
	modify_time datetime default current_timestamp on update current_timestamp comment '最后修改时间',
	nodes       json comment '节点信息',
	edges       json comment '连接线信息',
	primary key (config_id)
) comment '流程定义';

create table if not exists x_flow_config_publish
(
	publish_id  bigint not null comment 'ID',
	version		varchar(20) not null comment '版本号',
	is_active   tinyint(1) default 1 comment '状态：是否生效',
	config      json   not null comment '流程配置信息',
	config_id   bigint not null comment '流程定义ID',
	create_time datetime   default current_timestamp comment '创建时间',
	primary key (publish_id)
) comment '已发布流程信息';

create table if not exists x_flow
(
	flow_id     bigint      not null comment 'ID',
	biz_id      varchar(32) not null comment '业务唯一标识',
	publish_id  varchar(32) not null comment '使用的已发布流程ID',
	config_id   bigint      not null comment '流程ID',
	status      varchar(20) comment '状态',
	create_time datetime default current_timestamp comment '创建时间',
	finish_time datetime comment '完成时间',
	primary key (flow_id)
) comment '业务流程记录表';


create table if not exists x_flow_node_current
(
	current_id  bigint not null comment 'ID',
	prev_id		bigint comment '上一步ID',
	flow_id     bigint not null comment '业务流程ID',
	node_id     varchar(64) not null comment '节点标识',
	prev_node_id varchar(64) comment '上一节点标识',
	node_title  varchar(20) comment '节点标题',
	node_type   varchar(20) comment '节点类型',
	status      varchar(20) comment '状态： 0：办理中 1：已办结 2：已驳回 -1: 待领取',
	ticket_total tinyint comment '合计票数（票选节点使用）',
	finish_time datetime comment '（节点）完成时间',
	create_time datetime default current_timestamp comment '创建时间',
	primary key (current_id)
) comment '业务流程当前在办节点';

create table x_flow_node_current_log like x_flow_node_current;
alter table x_flow_node_current_log
	comment '业务流程节点记录';

create table if not exists x_flow_task
(
	task_id      bigint      not null comment 'ID',
	flow_id      bigint      not null comment '业务流程ID',
	current_id   bigint      not null comment 'ID',

	user_id      varchar(32) comment '操作人UUID',
	user_name    varchar(20) comment '操作人姓名',
	action       varchar(50) comment '处理动作：0：待处理 1提交 2审核 3委托 4转办 5退回 6终止',

	remark       varchar(255) comment '备注',
	candidates   json comment '候选人',
	data         json comment '附加数据',
	prev_id	 	 bigint comment '上一任务ID(转办，移送时使用)',
	type         varchar(20) comment '待办类型/待办来源类型',

	receive_time datetime default current_timestamp comment '接收任务时间',
	finish_time  datetime default null comment '完成任务时间',
	primary key (task_id)
) comment '业务流程待办任务';

create table if not exists x_flow_task_log like x_flow_task;
alter table x_flow_task_log
	comment '业务流程处理日志';

create table if not exists x_flow_lock(
    resource bigint not null comment '锁的资源ID，一般为currentId',
    create_time datetime default current_timestamp() comment '创建时间',
    primary key (resource)
) comment '活动节点锁';

create table if not exists x_flow_u_user_info
(
	user_id   varchar(50) not null comment 'ID',
	user_name varchar(50) not null comment '用户名称',
	primary key (user_id)
) comment '流程参与人员';

create table if not exists x_flow_u_user_role
(
	user_id   varchar(50) not null comment '人员ID',
	role_id   varchar(20) not null comment '角色ID',
	primary key (user_id, role_id)
) comment '流程参与人员-角色';

create table if not exists x_flow_u_user_dept
(
	user_id   varchar(50) not null comment '人员ID',
	dept_id   varchar(20) not null comment '部门ID',
	primary key (user_id, dept_id)
) comment '流程参与人员-角色';

create table if not exists x_flow_u_role
(
	role_id   varchar(20) not null comment 'ID',
	role_name varchar(50) not null comment '角色名称',
	remark    varchar(100) comment '说明',
	primary key (role_id)
) comment '流程角色';

create table if not exists x_flow_u_dept
(
	dept_id   varchar(20) not null comment 'ID',
	dept_name varchar(50) not null comment '部门名称',
	parent_id varchar(20) default 0 comment '父级ID',
	remark    varchar(100) comment '说明',
	primary key (dept_id)
) comment '流程部门';
