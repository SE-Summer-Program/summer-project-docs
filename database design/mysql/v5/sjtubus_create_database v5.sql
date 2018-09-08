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
   appointment_id        int not null auto_increment,
   user_id              int,
   shift_id             varchar(20),
   username				varchar(50),
   realname				varchar(50),
   user_code			varchar(50),
   appoint_date         date,
   line_name            varchar(50),
   line_name_cn			varchar(50),
   submit_time			datetime,
   isnormal             boolean,
   primary key (appointment_id)
);

/*==============================================================*/
/* Table: Bus                                                   */
/*==============================================================*/
create table Bus
(
   bus_id               int not null auto_increment,
   driver_id            int,
   seat_num             int check (seat_num >= 0),
   plate_num            varchar(50),
   primary key (bus_id)
);

/*==============================================================*/
/* Table: Driver                                                */
/*==============================================================*/
create table Driver
(
   driver_id            int not null auto_increment,
   username                varchar(50),
   password           varchar(50),
   phone 				varchar(50),
   primary key (driver_id)
);

/*==============================================================*/
/* Table: RideBusInfo                                           */
/*==============================================================*/
create table RideBusInfo
(
   ride_id              int not null auto_increment,
   ride_date            date,
   shift_id             varchar(20), 
   bus_id			int,
   ishoilday            boolean,
   isweekday            boolean,
   reserve_seat        int check (reserve_seat > 0),
   student_num          int check (student_num >= 0),
   teacher_num          int check (teacher_num >= 0),
   appoint_num          int check (appoint_num >= 0),
   appoint_break        int check (appoint_break >= 0),
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
   user_id              int not null auto_increment, 
   username             varchar(50),
   password             varchar(50),
   credit               int,
   isteacher            boolean,
   phone 				varchar(50),
   realname				varchar(50),
   student_number		varchar(50),
   primary key (user_id)
);

/*==============================================================*/
/* Table: JaccountUsers                                         */
/*==============================================================*/
create table JaccountUsers
(
   user_id              int not null auto_increment, 
   username             varchar(50),
   credit               int,
   isteacher            boolean,
   phone 				varchar(50),
   realname				varchar(50),
   student_number		varchar(50),
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
      
alter table RideBusInfo add constraint FK_Relationship_5 foreign key (bus_id)
      references Bus (bus_id) on delete restrict on update restrict;
      
alter table Collection add constraint FK_Relationship_6 foreign key (user_id)
      references Users (user_id) on delete restrict on update restrict;
      
alter table Collection add constraint FK_Relationship_7 foreign key (shift_id)
      references Shift (shift_id) on delete restrict on update restrict;
