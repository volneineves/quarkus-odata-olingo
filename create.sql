
    create table cliente (
        "Cód Cliente" float(53),
        "inRegistroAtivo" integer,
        "Data Últ Compra" timestamp(6),
        "skCliente" bigint not null,
        "Associação (Atual)" varchar(255),
        "Bairro (Atual)" varchar(255),
        "CNPJ/CPF (Atual)" varchar(255),
        "Cliente (Atual)" varchar(255),
        "Cód Grupo SAP" varchar(255),
        "Cód Grupo SAP (Atual)" varchar(255),
        "Desc Divisional (Atual)" varchar(255),
        "Desc Gerente (Atual)" varchar(255),
        "Desc Grupo Prazo Cliente (Atual)" varchar(255),
        "Desc Segmentação Cliente (Atual)" varchar(255),
        "Desc Superintendência (Atual)" varchar(255),
        "Gerente (Atual)" varchar(255),
        "Indicador Controlado (Atual)" varchar(255),
        "Meso Região (Atual)" varchar(255),
        "Micro Região (Atual)" varchar(255),
        "Rede (Atual)" varchar(255),
        "Setor (Atual)" varchar(255),
        "Supervisor (Atual)" varchar(255),
        "UF Cliente (Atual)" varchar(255),
        primary key (skCliente)
    );

    create table produto (
        "Cód Barra EAN" float(53),
        "Cód Barra EAN (Atual)" float(53),
        "Cód Fornecedor (Atual)" float(53),
        "Cód Produto" float(53),
        "Cód Grupo Preço (Atual)" bigint,
        "Cód Origem (Atual)" bigint,
        "skChaveamentoFornecedor" bigint,
        "skProduto" bigint not null,
        "Categoria Comercial (Atual)" varchar(255),
        "Comprador Atacado (Atual)" varchar(255),
        "Coordenador Atacado (Atual)" varchar(255),
        "Coordenador Varejo (Atual)" varchar(255),
        "Cód Condição Estocagem (Atual)" varchar(255),
        "Cód Curva Preço (Atual)" varchar(255),
        "Desc Categoria Comercial (Atual)" varchar(255),
        "Desc Chaveamento Fornecedor (Atual)" varchar(255),
        "Desc Marca (Atual)" varchar(255),
        "Desc Marca Arvore Mercadológica (Atual)" varchar(255),
        "Desc Produto (Atual)" varchar(255),
        "Desc Sazonalidade (Atual)" varchar(255),
        "Indicador Importado (Atual)" varchar(255),
        "Indicador Monitorado (Atual)" varchar(255),
        "Produto" varchar(255),
        "Produto (Atual)" varchar(255),
        "SSF Chaveamento Fornecedor (Atual)" varchar(255),
        "SubSortimento (Atual)" varchar(255),
        primary key (skProduto)
    );
INSERT INTO cliente (skCliente, "Cód Cliente", "Cliente (Atual)", "Rede (Atual)", "Associação (Atual)", "UF Cliente (Atual)", "Setor (Atual)", "Supervisor (Atual)", "Desc Gerente (Atual)", "Gerente (Atual)", "Desc Divisional (Atual)", "Desc Superintendência (Atual)", "Bairro (Atual)", "Data Últ Compra", "Cód Grupo SAP", "Cód Grupo SAP (Atual)", "Micro Região (Atual)", "CNPJ/CPF (Atual)", "Meso Região (Atual)", "Desc Segmentação Cliente (Atual)", "Desc Grupo Prazo Cliente (Atual)", "Indicador Controlado (Atual)", "inRegistroAtivo") VALUES (1, 123, 'Cliente A', 'Rede A', 'Associação A', 'SP', 'Setor A', 'Supervisor A', 'Gerente A', 'Gerente Atual A', 'Divisão A', 'Superintendência A', 'Bairro A', '2023-01-01', 'Grupo SAP A', 'Grupo SAP Atual A', 'Micro Região A', '12345678900', 'Meso Região A', 'Segmentação A', 'Prazo Cliente A', 'Sim', 1);
INSERT INTO cliente (skCliente, "Cód Cliente", "Cliente (Atual)", "Rede (Atual)", "Associação (Atual)", "UF Cliente (Atual)", "Setor (Atual)", "Supervisor (Atual)", "Desc Gerente (Atual)", "Gerente (Atual)", "Desc Divisional (Atual)", "Desc Superintendência (Atual)", "Bairro (Atual)", "Data Últ Compra", "Cód Grupo SAP", "Cód Grupo SAP (Atual)", "Micro Região (Atual)", "CNPJ/CPF (Atual)", "Meso Região (Atual)", "Desc Segmentação Cliente (Atual)", "Desc Grupo Prazo Cliente (Atual)", "Indicador Controlado (Atual)", "inRegistroAtivo") VALUES (2, 456, 'Cliente B', 'Rede B', 'Associação B', 'RJ', 'Setor B', 'Supervisor B', 'Gerente B', 'Gerente Atual B', 'Divisão B', 'Superintendência B', 'Bairro B', '2023-02-01', 'Grupo SAP B', 'Grupo SAP Atual B', 'Micro Região B', '98765432100', 'Meso Região B', 'Segmentação B', 'Prazo Cliente B', 'Não', 1);
INSERT INTO produto (skProduto, "Cód Produto", "Desc Produto (Atual)", "Produto", "Produto (Atual)", "SubSortimento (Atual)", "Cód Fornecedor (Atual)", "Desc Chaveamento Fornecedor (Atual)", "SSF Chaveamento Fornecedor (Atual)", "Desc Marca (Atual)", "Cód Barra EAN (Atual)", "Cód Barra EAN", "Indicador Monitorado (Atual)", "Indicador Importado (Atual)", "Cód Origem (Atual)", "Desc Categoria Comercial (Atual)", "Categoria Comercial (Atual)", "Desc Sazonalidade (Atual)", "Cód Condição Estocagem (Atual)", "Desc Marca Arvore Mercadológica (Atual)", "Comprador Atacado (Atual)", "Cód Grupo Preço (Atual)", "Cód Curva Preço (Atual)", "skChaveamentoFornecedor", "Coordenador Atacado (Atual)", "Coordenador Varejo (Atual)") VALUES (1, 12345, 'Descrição do Produto A', 'Produto A', 'Produto Atual A', 'Sortimento A', 789, 'Fornecedor A', 'SSF A', 'Marca A', 111222333, 444555666, 'Sim', 'Não', 101, 'Categoria A', 'Categoria Atual A', 'Sazonalidade A', 'Condição A', 'Marca Árvore A', 'Comprador A', 102, 'Curva A', 201, 'Coordenador A', 'Coordenador Varejo A');
INSERT INTO produto (skProduto, "Cód Produto", "Desc Produto (Atual)", "Produto", "Produto (Atual)", "SubSortimento (Atual)", "Cód Fornecedor (Atual)", "Desc Chaveamento Fornecedor (Atual)", "SSF Chaveamento Fornecedor (Atual)", "Desc Marca (Atual)", "Cód Barra EAN (Atual)", "Cód Barra EAN", "Indicador Monitorado (Atual)", "Indicador Importado (Atual)", "Cód Origem (Atual)", "Desc Categoria Comercial (Atual)", "Categoria Comercial (Atual)", "Desc Sazonalidade (Atual)", "Cód Condição Estocagem (Atual)", "Desc Marca Arvore Mercadológica (Atual)", "Comprador Atacado (Atual)", "Cód Grupo Preço (Atual)", "Cód Curva Preço (Atual)", "skChaveamentoFornecedor", "Coordenador Atacado (Atual)", "Coordenador Varejo (Atual)") VALUES (2, 67890, 'Descrição do Produto B', 'Produto B', 'Produto Atual B', 'Sortimento B', 890, 'Fornecedor B', 'SSF B', 'Marca B', 777888999, 000111222, 'Não', 'Sim', 202, 'Categoria B', 'Categoria Atual B', 'Sazonalidade B', 'Condição B', 'Marca Árvore B', 'Comprador B', 103, 'Curva B', 202, 'Coordenador B', 'Coordenador Varejo B');
INSERT INTO produto (skProduto, "Cód Produto", "Desc Produto (Atual)", "Produto", "Produto (Atual)", "SubSortimento (Atual)", "Cód Fornecedor (Atual)", "Desc Chaveamento Fornecedor (Atual)", "SSF Chaveamento Fornecedor (Atual)", "Desc Marca (Atual)", "Cód Barra EAN (Atual)", "Cód Barra EAN", "Indicador Monitorado (Atual)", "Indicador Importado (Atual)", "Cód Origem (Atual)", "Desc Categoria Comercial (Atual)", "Categoria Comercial (Atual)", "Desc Sazonalidade (Atual)", "Cód Condição Estocagem (Atual)", "Desc Marca Arvore Mercadológica (Atual)", "Comprador Atacado (Atual)", "Cód Grupo Preço (Atual)", "Cód Curva Preço (Atual)", "skChaveamentoFornecedor", "Coordenador Atacado (Atual)", "Coordenador Varejo (Atual)") VALUES (3, 13579, 'Descrição do Produto C', 'Produto C', 'Produto Atual C', 'Sortimento C', 123, 'Fornecedor C', 'SSF C', 'Marca C', 333444555, 666777888, 'Sim', 'Não', 303, 'Categoria C', 'Categoria Atual C', 'Sazonalidade C', 'Condição C', 'Marca Árvore C', 'Comprador C', 104, 'Curva C', 203, 'Coordenador C', 'Coordenador Varejo C');
