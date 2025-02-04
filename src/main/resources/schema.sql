create table if not exists fpv_drone (
fpv_drone_id bigint auto_increment,
fpv_craft_name varchar(255) not null,
fpv_serial_number varchar(255) not null,
fpv_model enum ('BOMBER','KAMIKAZE','PPO') not null,
primary key (fpv_drone_id)
);

create table if not exists fpv_pilot (
fpv_pilot_id bigint auto_increment,
name varchar(255) not null,
sur_name varchar(255) not null,
primary key (fpv_pilot_id)
);

create table if not exists fpv_report (
fpv_report_id bigint auto_increment,
fpv_drone_id bigint unique,
date_time_flight timestamp(6) not null,
fpv_pilot_id bigint unique,
is_lostfpvdue_toreb boolean,
is_on_targetfpv boolean,
coordinatesmgrs varchar(255) not null,
additional_info varchar(255) not null,
primary key (fpv_report_id)
);

alter table if exists fpv_report add constraint FK_fpv_drone foreign key (fpv_drone_id) references fpv_drone;
alter table if exists fpv_report add constraint FK_fpv_pilot foreign key (fpv_pilot_id) references fpv_pilot;
