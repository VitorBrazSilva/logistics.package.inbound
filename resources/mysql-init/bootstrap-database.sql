create table shipping_package (
                                  id binary(16) not null,
                                  created_at datetime(6) not null,
                                  delivered_at datetime(6),
                                  description varchar(255) not null,
                                  estimated_delivery_date date,
                                  fun_fact LONGTEXT,
                                  is_holiday bit,
                                  recipient varchar(255) not null,
                                  sender varchar(255) not null,
                                  status enum ('CANCELED','CREATED','DELIVERED','IN_TRANSIT') not null,
                                  updated_at datetime(6) not null,
                                  primary key (id)
) engine=InnoDB;

create table tracking_event (
                                id binary(16) not null,
                                created_at datetime(6) not null,
                                description varchar(255) not null,
                                location varchar(255) not null,
                                shipping_package_id binary(16) not null,
                                primary key (id)
) engine=InnoDB;

create index idx_sender
    on shipping_package (sender);

create index idx_recipient
    on shipping_package (recipient);

create index idx_sender_recipient
    on shipping_package (sender, recipient);

alter table tracking_event
    add constraint FKtk15m6d0oel72yyevnlttw9la
        foreign key (shipping_package_id)
            references shipping_package (id);