create table volcano.booking
(
    id             INT GENERATED ALWAYS AS IDENTITY,
    booking_id     varchar(36)   not null,
    name           varchar(256)  not null,
    email          varchar(1024) not null,
    arrival_date   DATE          not null,
    departure_date DATE          not null,
    is_cancelled   bool,
    created_on     timestamptz   not null,
    last_modified  timestamptz   not null
);

create
unique index booking_idx on volcano.booking(booking_id);