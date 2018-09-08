/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/7/2 15:34:17                            */
/*==============================================================*/

drop table if exists Appointment;

drop table if exists RideBusInfo;

drop table if exists Collection;

drop table if exists Users;

drop table if exists JaccountUsers;

drop table if exists Administrator;

drop table if exists Shift;

drop table if exists Bus;

drop table if exists Driver;

drop table if exists Line;


/*==============================================================*/
/* Table: Appointment                                           */
/*==============================================================*/
create table Appointment
(
   appointment_id       int not null auto_increment,#id
   user_id              int,#用户的id（没有用）
   shift_id             varchar(20),#shift id
   username				varchar(50),#预约用户的用户名
   realname				varchar(50),#用户真实姓名
   user_role			varchar(50),#用户身份“user” | "jaccountuser"
   appoint_date         date,#预约日期
   line_name            varchar(50),#线路名称
   line_name_cn			varchar(50),#线路名称中文
   submit_time			datetime,#提交预约的时间
   isnormal             boolean,#是否上车 true:已经上车 false:没上车
   comment				varchar(256),#备注信息				
   primary key (appointment_id)
);

/*==============================================================*/
/* Table: Bus                                                   */
/*==============================================================*/
create table Bus
(
   bus_id               int not null auto_increment,#bus_id
   driver_id            int,#司机的id
   seat_num             int check (seat_num >= 0),#车上的实际座位数
   plate_num            varchar(50),#车牌号
   primary key (bus_id)
);

/*==============================================================*/
/* Table: Driver                                                */
/*==============================================================*/
create table Driver
(
   driver_id            int not null auto_increment,#driver_id
   username                varchar(50),#司机用户名
   password           varchar(50),#司机密码
   phone 				varchar(50),#司机电话
   primary key (driver_id)
);

/*==============================================================*/
/* Table: RideBusInfo                                           */
/*==============================================================*/
create table RideBusInfo
(
   ride_id              varchar(20),# 格式（ride_date:shift_id)
   ride_date            date,#出行日期
   shift_id             varchar(20), 
   line_type        	varchar(20),#工作日/非工作日 假期/非假期
   bus_plate			varchar(20),#车牌号
   reserve_seat         int check (reserve_seat > 0),#可预约座位数
   student_num          int check (student_num >= 0),#非教工人数
   teacher_num          int check (teacher_num >= 0),#教工人数
   appoint_num          int check (appoint_num >= 0),#预约人数
   appoint_break        int check (appoint_break >= 0),#预约违约人数
   seat_num             int check (seat_num >= 0),#车上座位数
   primary key (ride_id)
);

/*==============================================================*/
/* Table: Shift                                                 */
/*==============================================================*/
create table Shift
(
   shift_id             varchar(20) not null,
   bus_id 				int,
   line_name            varchar(50),
   line_name_cn         varchar(50),
   line_type			varchar(50),
   departure_time 		time,
   arrive_time			time,
   reserve_seat        int check (reserve_seat > 0),
   comment     			varchar(256),
   primary key (shift_id)
);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table Users
(
   user_id              int not null auto_increment, #用户id
   username             varchar(50),#用户名
   password             varchar(50),#密码
   credit               int,#信用积分
   isteacher            boolean,#是不是教工
   phone 				varchar(50),#用户电话号码
   realname				varchar(50),#用户真实姓名
   student_number		varchar(50),#用户学号
   primary key (user_id)
);

/*==============================================================*/
/* Table: JaccountUsers                                         */
/*==============================================================*/
create table JaccountUsers
(
   user_id              int not null auto_increment, 
   username             varchar(50),#Jaccount用户名
   credit               int,#信用积分
   isteacher            boolean,#是不是教工
   phone 				varchar(50),#用户电话号码
   realname				varchar(50),#用户真实姓名
   student_number		varchar(50),#用户学号
   primary key (user_id)
);

/*==============================================================*/
/* Table: Administrator                                         */
/*==============================================================*/
create table Administrator
(
	id 					int not null auto_increment,
    username			varchar(50),
    password			varchar(50),
    primary key (id)
);

/*==============================================================*/
/* Table: Collection                                            */
/*==============================================================*/
create table Collection
(
	id 					int not null auto_increment,
    user_id				int,
    username  			varchar(50),
    shift_id			varchar(20),
    frequence			int,
    primary key (id)
);


alter table Appointment add constraint FK_Relationship_1 foreign key (shift_id)
      references Shift (shift_id) on delete restrict on update restrict;

alter table Bus add constraint FK_Relationship_2 foreign key (driver_id)
       references Driver (driver_id) on delete restrict on update restrict;
 
alter table Shift add constraint FK_Relationship_3 foreign key (bus_id)
      references Bus (bus_id) on delete restrict on update restrict;

alter table RideBusInfo add constraint FK_Relationship_4 foreign key (shift_id)
      references Shift (shift_id) on delete restrict on update restrict;
      
alter table Collection add constraint FK_Relationship_7 foreign key (shift_id)
      references Shift (shift_id) on delete restrict on update restrict;
