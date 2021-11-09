DROP TABLE IF EXISTS orders;
CREATE TABLE orders(
    id bigserial,
    user_id BIGINT NOT NULL,
    price DECIMAL NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT orders_users_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items (
                             id                    bigserial,
                             number                INT NOT NULL,
                             product_id            BIGINT NOT NULL,
                             qty                   INT NOT NULL,
                             order_id              BIGINT NOT NULL,
                             PRIMARY KEY (id),
                             CONSTRAINT orderitems_orders_fk
                                 FOREIGN KEY (order_id)
                                    REFERENCES orders(id),
                             CONSTRAINT  orderitems_products_fk
                                FOREIGN KEY (product_id)
                                    REFERENCES products(id)

);

