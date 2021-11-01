CREATE TABLE IF NOT EXISTS buyers (
    id serial,
    name VARCHAR(255),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS products (
    id serial,
    title varchar(255),
    price decimal,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS buyers_products(
    buyer_id bigint,
    product_id bigint,
    CONSTRAINT fk_buy FOREIGN KEY (buyer_id) REFERENCES buyers(id),
    CONSTRAINT fk_prod FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT INTO products(title,price) VALUES ('Narsil',15000), ('Double sabers', 7500), ('Axe', 11000),
                                         ('Sting', 21000), ('Stuff', 13000), ('Bow',11000),('Sword', 6000),('Mordor''s bow', 17000),
                                         ('Ring of power', 99999);
