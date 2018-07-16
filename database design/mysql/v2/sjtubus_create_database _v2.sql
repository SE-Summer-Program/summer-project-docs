/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/7/2 15:34:17                            */
/*==============================================================*/

drop table if exists Appointment;

drop table if exists RideBusInfo;

drop table if exists User;

drop table if exists Administrator;

drop table if exists Bus;

drop table if exists Driver;

drop table if exists Shift;

drop table if exists Line;


/*==============================================================*/
/* Table: Appointment                                           */
/*==============================================================*/
create table Appointment
(
   appoinment_id        int not null,
   user_id              varchar(10),
   shift_id             varchar(10),
   appoint_date         date,
   line_name            varchar(50),
   isnormal             boolean,
   primary key (appoinment_id)
);

/*==============================================================*/
/* Table: Bus                                                   */
/*==============================================================*/
create table Bus
(
   bus_id               varchar(10) not null,
   driver_id            varchar(10),
   shift_id             varchar(10),
   seat_num             int check (seat_num >= 0),
   plate_num            varchar(50),
   primary key (bus_id)
);

/*==============================================================*/
/* Table: Driver                                                */
/*==============================================================*/
create table Driver
(
   driver_id            varchar(10) not null,
   username                varchar(50),
   password           varchar(50),
   primary key (driver_id)
);

/*==============================================================*/
/* Table: RideBusInfo                                           */
/*==============================================================*/
create table RideBusInfo
(
   ride_id              varchar(10) not null,
   ride_date            date,
   shift_id             varchar(10),
   bus_id				varchar(10),
   ishoilday            boolean,
   isweekday            boolean,
   reseverd_seat        int check (reserve_seat > 0),
   student_num          int check (student_num >= 0),
   teacher_num          int check (teacher_num >= 0),
   appoint_num          int check (appoint_num >= 0),
   appoint_break        int check (appoint_break >= 0),
   primary key (ride_id)
);

/*==============================================================*/
/* Table: Line                                              */
/*==============================================================*/
create table Line
(
	line_name 			varchar(50),
    primary key	(line_name)
);

/*==============================================================*/
/* Table: Shift                                                 */
/*==============================================================*/
create table Shift
(
   shift_id             varchar(10) not null,
   line_name              varchar(50),
   line_type			varchar(50),
   departure_time 		datetime,
   reseverd_seat        int check (reserve_seat > 0),
   comment     			varchar(1000),
   primary key (shift_id)
);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   user_id              varchar(10) not null,
   usrename             varchar(50),
   password             varchar(50),
   credit               int,
   isteacher            boolean,
   primary key (user_id)
);

/*==============================================================*/
/* Table: Administrator                                         */
/*==============================================================*/
create table Administrator
(
	id 					varchar(10) not null,
    a_username			varchar(50),
    a_password			varchar(50),
    primary key (id)
);



alter table Appointment add constraint FK_Relationship_10 foreign key (user_id)
      references User (user_id) on delete restrict on update restrict;

alter table Appointment add constraint FK_Relationship_11 foreign key (shift_id)
      references Shift (shift_id) on delete restrict on update restrict;

alter table Bus add constraint FK_Relationship_5 foreign key (driver_id)
       references Driver (driver_id) on delete restrict on update restrict;
 
 alter table Bus add constraint FK_Relationship_9 foreign key (shift_id)
      references Shift (shift_id) on delete restrict on update restrict;

alter table RideBusInfo add constraint FK_Relationship_7 foreign key (shift_id)
      references Shift (shift_id) on delete restrict on update restrict;
      
alter table Appointment add constraint FK_Relationship_1 foreign key (line_name)
       references Line (line_name) on delete restrict on update restrict;
       
alter table Shift add constraint FK_Relationship_2 foreign key (line_name)
       references Line (line_name) on delete restrict on update restrict;
      
alter table RideBusInfo add constraint FK_Relationship_3 foreign key (bus_id)
      references Bus (bus_id) on delete restrict on update restrict;
