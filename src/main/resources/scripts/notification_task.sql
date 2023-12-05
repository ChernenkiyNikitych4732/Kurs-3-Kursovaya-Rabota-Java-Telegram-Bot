CREATE TABLE notification_task(
                                  id bigserial primary key,
                                  chat_id int8 not null,
                                  message text not null,
                                  execution_date timestamp not null
)