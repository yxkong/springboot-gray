CREATE TABLE gateway.t_load_balancer_rule (
  id INT auto_increment NOT NULL,
  filed varchar(100) NOT NULL COMMENT '字段',
  operator varchar(100) NULL COMMENT '操作符，>,>=,=,<,<=,mod,',
  val varchar(100) NULL COMMENT '具体值',
  union_relation varchar(10) NULL COMMENT '级联关系,and or',
  group varchar(10) NULL COMMENT '分组',
  status tinyint NULL COMMENT '状态，1生效，0失效，同时应该只有一组状态生效',
  remark varchar(100) NULL COMMENT '备注',
)ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci
COMMENT='负载均衡规则';
