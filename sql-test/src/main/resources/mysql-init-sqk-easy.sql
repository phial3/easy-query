
CREATE DATABASE IF NOT EXISTS easy-query-test CHARACTER SET 'utf8mb4';
create table t_blog
(
    id varchar(32) not null comment '主键ID'primary key,
    deleted tinyint(1) default 0 not null comment '是否删除',
    create_by varchar(32) not null comment '创建人',
    create_time datetime not null comment '创建时间',
    update_by varchar(32) not null comment '更新人',
    update_time datetime not null comment '更新时间',
    title varchar(50) not null comment '标题',
    content varchar(256) null comment '内容',
    url varchar(128) null comment '博客链接',
    star int not null comment '点赞数',
    publish_time datetime null comment '发布时间',
    score decimal(18, 2) not null comment '评分',
    status int not null comment '状态',
    `order` decimal(18, 2) not null comment '排序',
    is_top tinyint(1) not null comment '是否置顶',
    top tinyint(1) not null comment '是否置顶'
)comment '博客表';

create table t_topic
(
    id varchar(32) not null comment '主键ID'primary key,
    stars int not null comment '点赞数',
    title varchar(50) null comment '标题',
    create_time datetime not null comment '创建时间'
)comment '主题表';


create table t_topic_auto
(
    id int  not null comment '主键ID'primary key auto_increment,
    stars int not null comment '点赞数',
    title varchar(50) not null comment '标题',
    create_time datetime not null comment '创建时间'
)comment '主题表id自增';



create table t_sys_user
(
    id varchar(32) not null comment '主键ID'primary key,
    username varchar(50) null comment '姓名',
    phone varchar(250) null comment '手机号加密',
    id_card varchar(500) null comment '身份证编号',
    address text null comment '地址',
    create_time datetime not null comment '创建时间'
)comment '用户表';



create table t_logic_del_topic
(
    id varchar(32) not null comment '主键ID'primary key,
    stars int not null comment '点赞数',
    title varchar(50) null comment '标题',
    deleted tinyint(1)  not null comment '是否删除',
    create_time datetime not null comment '创建时间'
)comment '逻辑删除主题表';
create table t_logic_del_topic_custom
(
    id varchar(32) not null comment '主键ID'primary key,
    stars int not null comment '点赞数',
    title varchar(50) null comment '标题',
    deleted_at datetime   null comment '删除时间',
    deleted_user varchar(50)   null comment '删除人',
    create_time datetime not null comment '创建时间'
)comment '自定义逻辑删除主题表';


create table t_topic_interceptor
(
    id varchar(32) not null comment '主键ID'primary key,
    stars int not null comment '点赞数',
    title varchar(50) null comment '标题',
    create_time datetime not null comment '创建时间',
    create_by varchar(50) not null comment '创建人',
    update_time datetime not null comment '修改时间',
    update_by varchar(50) not null comment '修改人',
    tenant_id varchar(50) not null comment '租户'
)comment '拦截器主题表';


create table t_sys_user_encryption
(
    id varchar(32) not null comment '主键ID'primary key,
    name varchar(32) not null comment '名称',
    phone_not_support_like varchar(100) null comment '不支持like的手机号',
    phone_support_like varchar(200) null comment '支持like的手机号',
    address_not_support_like varchar(1024)  null comment '支持like的地址',
    address_support_like varchar(1024)  null comment '不支持like的地址'
)comment '用户字段加密表';


create table t_sys_user_track
(
    id varchar(32) not null comment '主键ID'primary key,
    username varchar(50) null comment '姓名',
    phone varchar(250) null comment '手机号加密',
    id_card varchar(500) null comment '身份证编号',
    address text null comment '地址',
    create_time datetime not null comment '创建时间'
)comment '用户追踪表';
create table t_sys_user_version
(
    id varchar(32) not null comment '主键ID'primary key,
    username varchar(50) null comment '姓名',
    phone varchar(250) null comment '手机号加密',
    id_card varchar(500) null comment '身份证编号',
    address text null comment '地址',
    version bigint not null comment '行版本',
    create_time datetime not null comment '创建时间'
)comment '用户版本表';
create table t_sys_user_version_del
(
    id varchar(32) not null comment '主键ID'primary key,
    username varchar(50) null comment '姓名',
    phone varchar(250) null comment '手机号加密',
    id_card varchar(500) null comment '身份证编号',
    address text null comment '地址',
    version bigint not null comment '行版本',
    create_time datetime not null comment '创建时间',
    deleted tinyint(1) not null comment '是否删除'
)comment '用户版本逻辑删除表';




