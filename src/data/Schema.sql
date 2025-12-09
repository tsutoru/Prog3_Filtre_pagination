CREATE TABLE product_category (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL,
                                  product_id  INTEGER REFERENCES product (id)
);

CREATE TABLE product
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(255)             NOT NULL,
    price             NUMERIC(12, 2)           NOT NULL,
    creation_datetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_product_name_ilike ON product (lower(name));
CREATE INDEX idx_category_name_ilike ON product_category (lower(name));
CREATE INDEX idx_product_creation_datetime ON product (creation_datetime);
