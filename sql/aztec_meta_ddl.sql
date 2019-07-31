create database `aztec_db`;

use `aztec_db`;

create table `base_meta_info`(
`id` int primary key auto_increment,
`name` varchar(200) not null, 
`content` text not null,
`type` int not null,
`desc` text,
`c_gmt` datetime,
`u_gmt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)engine='InnoDB',charset='utf8';

insert into `base_meta_info`(`name`,`content`,`type`,`desc`,`c_gmt`,`u_gmt`) values('ZK_CONNECT_INFO','{"connectString":"metacenter.aztec.org:2181","sessionTimeout":10000,"authUser":"liming:1234"}',1,'Zookeeper connection information',now(),now());


insert into `base_meta_info`(`name`,`content`,`type`,`desc`,`c_gmt`,`u_gmt`) values('REDIS_CONNECT_INFO','{"hosts":"metacenter.aztec.org","ports":"6379"}',1,'REDIS connection information',now(),now());