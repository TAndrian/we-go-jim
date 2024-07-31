CREATE TABLE IF NOT EXISTS users(
    id UUID PRIMARY KEY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS booking(
    id UUID PRIMARY KEY NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users_bookings(
    user_id UUID REFERENCES users(id),
    booking_id UUID REFERENCES booking(id),
    PRIMARY KEY(user_id, booking_id)
);

INSERT INTO users (id, first_name, last_name, email, password, role) VALUES
  ('4fab73f9-b152-4bb6-a515-ed13cef63a0c', 'John', 'Doe', 'john.doe@example.com', 'password123', 'USER'),
  ('921fa993-b38f-41c0-99bb-5555515e73a0', 'Jane', 'Smith', 'jane.smith@example.com', 'password123', 'USER');

INSERT INTO booking (id, start_time, end_time, max_participant) VALUES
  ('f255538a-8a02-4c43-adf4-aa37cce43894', '2024-06-19T12:00:00', '2024-06-19T12:30:00', 10),
  ('3fa0e077-690e-4847-89b5-b3881534af3f', '2024-06-22T14:00:00', '2024-06-22T14:30:00', 5);

INSERT INTO users_bookings (user_id, booking_id) VALUES
  ((SELECT id FROM users WHERE email = 'john.doe@example.com'), (SELECT id FROM booking WHERE start_time = '2024-06-19T12:00:00')),
  ((SELECT id FROM users WHERE email = 'jane.smith@example.com'), (SELECT id FROM booking WHERE start_time = '2024-06-22T14:00:00'));