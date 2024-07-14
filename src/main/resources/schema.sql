DROP TABLE IF EXISTS requests, comments, bookings, items, users;


CREATE TABLE IF NOT EXISTS users (
id              BIGINT  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name            VARCHAR(300),
email           VARCHAR(300),
UNIQUE(email) );
CREATE INDEX idx_users_id ON users (id);
CREATE INDEX idx_users_name ON users (name);



CREATE TABLE IF NOT EXISTS requests (
id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
description     VARCHAR(500),
requester_id    BIGINT,
created         TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id));
CREATE INDEX idx_req_id ON requests (id);
CREATE INDEX idx_req_description ON requests (description);




CREATE TABLE IF NOT EXISTS items (
id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name            VARCHAR(300),
description     VARCHAR(300),
is_available    BOOLEAN,
owner_id        BIGINT,
request_id      BIGINT,
CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id),
CONSTRAINT fk_items_to_request FOREIGN KEY(request_id) REFERENCES requests(id) );
CREATE INDEX idx_items_id ON items (id);
CREATE INDEX idx_items_name ON items (name);


CREATE TABLE IF NOT EXISTS bookings (
id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
start_date      TIMESTAMP WITHOUT TIME ZONE,
end_date        TIMESTAMP  WITHOUT TIME ZONE,
item_id         BIGINT,
booker_id       BIGINT,
status          VARCHAR(300),
CONSTRAINT fk_booking_to_items FOREIGN KEY(item_id) REFERENCES items(id),
CONSTRAINT fk_booking_to_users FOREIGN KEY(booker_id) REFERENCES users(id) );
CREATE INDEX idx_bookings_id ON bookings (id);
CREATE INDEX idx_bookings_status ON bookings (status);


CREATE TABLE IF NOT EXISTS comments (
id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
text            VARCHAR(500),
item_id         BIGINT,
author_id       BIGINT,
created         TIMESTAMP WITHOUT TIME ZONE,
CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id),
CONSTRAINT fk_comments_to_users FOREIGN KEY(author_id) REFERENCES users(id) );
CREATE INDEX idx_comments_id ON comments (id);



