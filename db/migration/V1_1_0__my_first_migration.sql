
CREATE TABLE IF NOT EXISTS products (
    id serial,
    title varchar(255),
    price decimal,
    PRIMARY KEY(id)
);


INSERT INTO products(title,price) VALUES ('Narsil',15000.99), ('Double sabers', 7500), ('Axe', 11000),
                                         ('Sting', 21000), ('Stuff', 13000.27), ('Bow',11000),('Sword', 6000),('Mordor''s bow', 17000),
                                         ('Ring of power', 99999), ('Palantir', 30000), ('Crown of kings', 6000), ('Elven light', 21000),
                                         ('Ring of white sorcerer', 70000), ('Archenstone', 45000);
