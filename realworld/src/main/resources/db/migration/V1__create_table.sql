create table article
(
    id          bigint not null auto_increment,
    created_at  datetime(6),
    updated_at  datetime(6),
    body        varchar(255),
    description varchar(255),
    slug        varchar(255),
    title       varchar(255),
    author_id   bigint not null,
    primary key (id)
);

create table article_tags
(
    article_id bigint not null,
    tags_id    bigint not null
);

create table comment
(
    id         bigint not null auto_increment,
    created_at datetime(6),
    updated_at datetime(6),
    body       varchar(255),
    article_id bigint not null,
    author_id  bigint not null,
    primary key (id)
);

create table follow
(
    id          bigint not null auto_increment,
    followee_id bigint,
    follower_id bigint,
    primary key (id)
);

create table tag
(
    id   bigint not null auto_increment,
    name varchar(255),
    primary key (id)
);

create table users
(
    id       bigint not null auto_increment,
    bio      varchar(255),
    email    varchar(255),
    image    varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (id)
);

alter table article
    add constraint UK_lc76j4bqg2jrk06np18eve5yj unique (slug);

alter table article_tags
    add constraint UK_8cum6s2ucfosk3e4jepem69m5 unique (tags_id);

alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table article
    add constraint FKmjgtny2i22jf4dqncmd436s0u
        foreign key (author_id)
            references users (id);

alter table article_tags
    add constraint FKp6owh2p5p9yllwwrc2hn7bnxr
        foreign key (tags_id)
            references tag (id);

alter table article_tags
    add constraint FK85ph188kqbfc5u1gq0tme7flk
        foreign key (article_id)
            references article (id);

alter table comment
    add constraint FK5yx0uphgjc6ik6hb82kkw501y
        foreign key (article_id)
            references article (id);

alter table comment
    add constraint FKir20vhrx08eh4itgpbfxip0s1
        foreign key (author_id)
            references users (id);

alter table follow
    add constraint FKgf9vtbrmcrkbfmfl0p9mg4ok7
        foreign key (followee_id)
            references users (id);

alter table follow
    add constraint FKjikg34txcxnhcky26w14fvfcc
        foreign key (follower_id)
            references users (id);