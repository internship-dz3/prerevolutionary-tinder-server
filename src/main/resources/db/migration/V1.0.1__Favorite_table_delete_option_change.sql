alter table tinder.favorite
    drop constraint "user-favorite_user_id_fk";

alter table tinder.favorite
    add constraint "user-favorite_user_id_fk"
        foreign key (user_id) references tinder."user"
            on delete cascade;

alter table tinder.favorite
    drop constraint "user-favorite_fav_id_fk";

alter table tinder.favorite
    add constraint "user-favorite_fav_id_fk"
        foreign key (favorite_id) references tinder."user"
            on delete cascade;

alter table tinder.dislike
    drop constraint dislike_user_id_fk;

alter table tinder.dislike
    add constraint dislike_user_id_fk
        foreign key (user_id) references tinder."user"
            on delete cascade;

alter table tinder.dislike
    drop constraint dislike_user_id_fk_2;

alter table tinder.dislike
    add constraint dislike_user_id_fk_2
        foreign key (disliked_user_id) references tinder."user"
            on delete cascade;

