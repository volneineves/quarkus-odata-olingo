
    create sequence t_category_SEQ start with 1 increment by 50;

    create sequence t_product_SEQ start with 1 increment by 50;

    create table t_category (
        createdAt timestamp(6) not null,
        id bigint not null,
        updatedAt timestamp(6) not null,
        name varchar(50) not null,
        description varchar(255),
        primary key (id)
    );

    create table t_product (
        price numeric(10,2) not null,
        category_id bigint not null,
        createdAt timestamp(6) not null,
        id bigint not null,
        stockQuantity bigint not null,
        updatedAt timestamp(6) not null,
        name varchar(50) not null,
        description varchar(255),
        primary key (id)
    );

    alter table if exists t_product 
       add constraint FKp17nkwpqnnxh5iax87dc58sp3 
       foreign key (category_id) 
       references t_category;
INSERT INTO t_category (id, name, description, createdAt, updatedAt) VALUES (1, 'Analgésicos', 'Medicamentos para alívio da dor', '2023-01-01', '2023-03-01');
INSERT INTO t_category (id, name, description, createdAt, updatedAt) VALUES (2, 'Antibióticos', 'Medicamentos para infecções bacterianas', '2023-01-05', '2023-03-05');
INSERT INTO t_category (id, name, description, createdAt, updatedAt) VALUES (3, 'Vitaminas', 'Suplementos e multivitaminas', '2023-01-10', '2023-03-10');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (1, 'Paracetamol', 'Analgésico e antipirético', 5.99, 100, 1, '2023-01-15', '2023-03-15');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (2, 'Amoxicilina', 'Antibiótico de amplo espectro', 12.50, 80, 2, '2023-01-20', '2023-03-20');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (3, 'Complexo B', 'Vitaminas B em comprimidos', 8.99, 150, 3, '2023-01-25', '2023-03-25');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (4, 'Ibuprofeno', 'Anti-inflamatório e analgésico', 7.99, 120, 1, '2023-02-01', '2023-04-01');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (5, 'Ciprofloxacino', 'Antibiótico para infecções urinárias', 15.99, 60, 2, '2023-02-05', '2023-04-05');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (6, 'Vitamina C', 'Suplemento para fortalecimento do sistema imunológico', 9.99, 200, 3, '2023-02-10', '2023-04-10');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (7, 'Dipirona', 'Medicação para febre e dores moderadas', 6.50, 150, 1, '2023-02-15', '2023-04-15');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (8, 'Azitromicina', 'Antibiótico de uso geral', 18.00, 70, 2, '2023-02-20', '2023-04-20');
INSERT INTO t_product (id, name, description, price, stockQuantity, category_id, createdAt, updatedAt) VALUES (9, 'Vitamina D3', 'Suplemento de vitamina D', 12.99, 160, 3, '2023-02-25', '2023-04-25');
