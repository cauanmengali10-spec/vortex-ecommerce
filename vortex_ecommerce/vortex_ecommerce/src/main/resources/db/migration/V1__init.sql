-- ============================================================
-- V1__init.sql  |  Ecommerce de periféricos - esqueleto do domínio
-- ============================================================

-- Módulo Catálogo
-- ------------------------------------------------------------------

CREATE TABLE categorias (
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(90)  NOT NULL,
    slug       VARCHAR(110) NOT NULL UNIQUE,   -- usado na URL: /mouses
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE,  -- soft delete: esconde sem apagar
    criado_em  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE fornecedores (
    id                   BIGSERIAL PRIMARY KEY,
    nome                 VARCHAR(120) NOT NULL,
    cnpj                 VARCHAR(18) UNIQUE,
    nome_contato         VARCHAR(120),
    email                VARCHAR(190),
    telefone             VARCHAR(20),
    prazo_reposicao_dias INT NOT NULL DEFAULT 3,  -- prazo médio p/ repor estoque
    ativo                BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE produtos (
    id                BIGSERIAL PRIMARY KEY,
    categoria_id      BIGINT REFERENCES categorias (id),
    fornecedor_id     BIGINT REFERENCES fornecedores (id),  -- 1 fornecedor por produto (simplificação)
    nome              VARCHAR(160) NOT NULL,
    slug              VARCHAR(190) NOT NULL UNIQUE,         -- também pode ser gerado do nome
    descricao_curta   VARCHAR(255),
    descricao         TEXT,
    marca             VARCHAR(90),
    url_imagem        TEXT,
    destaque          BOOLEAN NOT NULL DEFAULT FALSE,       -- destaque da home
    ativo             BOOLEAN NOT NULL DEFAULT TRUE,        -- soft delete do produto
    criado_em         TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em     TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- "preço de/por": preco = de, preco_promocional = por
CREATE TABLE variacoes_produto (
    id                BIGSERIAL PRIMARY KEY,
    produto_id        BIGINT NOT NULL REFERENCES produtos (id) ON DELETE CASCADE,
    sku               VARCHAR(50) NOT NULL UNIQUE,          -- código único de estoque
    nome              VARCHAR(120) NOT NULL,                -- ex.: "Preto", "Switch Red", "XL"
    preco             NUMERIC(12,2) NOT NULL CHECK (preco >= 0),
    preco_promocional NUMERIC(12,2) CHECK (preco_promocional IS NULL OR preco_promocional < preco),
    preco_custo       NUMERIC(12,2) CHECK (preco_custo IS NULL OR preco_custo >= 0),
    quantidade_estoque INT NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0),
    estoque_minimo_alerta INT NOT NULL DEFAULT 5,           -- ponto de reposição p/ admin
    ativo             BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em         TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em     TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Módulo Cliente
-- ------------------------------------------------------------------

CREATE TABLE clientes (
    id                  BIGSERIAL PRIMARY KEY,
    nome                VARCHAR(120) NOT NULL,
    email               VARCHAR(190) NOT NULL UNIQUE,       -- identidade do login OTP
    telefone            VARCHAR(20),
    eh_admin            BOOLEAN NOT NULL DEFAULT FALSE,     -- separa vendedor de cliente
    aceita_notificacoes BOOLEAN NOT NULL DEFAULT TRUE,      -- aceita aviso de reposição
    criado_em           TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE enderecos (
    id           BIGSERIAL PRIMARY KEY,
    cliente_id   BIGINT NOT NULL REFERENCES clientes (id) ON DELETE CASCADE,
    rotulo       VARCHAR(60),             -- ex.: "Casa", "Trabalho"
    destinatario VARCHAR(120) NOT NULL,   -- nome de quem recebe
    logradouro   VARCHAR(160) NOT NULL,
    numero       VARCHAR(20)  NOT NULL,
    complemento  VARCHAR(120),
    bairro       VARCHAR(120) NOT NULL,
    cidade       VARCHAR(120) NOT NULL,
    uf           VARCHAR(2)   NOT NULL,   -- 2 letras (SP, RJ...)
    cep          VARCHAR(9)   NOT NULL,
    eh_padrao    BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Módulo Auth (login sem senha)
-- ------------------------------------------------------------------

CREATE TABLE codigos_otp (
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(190) NOT NULL,
    codigo     VARCHAR(6)   NOT NULL,     -- os 6 dígitos
    finalidade VARCHAR(30)  NOT NULL DEFAULT 'LOGIN',  -- LOGIN / CRIAR_CONTA
    expira_em  TIMESTAMPTZ  NOT NULL,     -- validade curta (a definir na Task de auth)
    tentativas INT NOT NULL DEFAULT 0,    -- conta tentativas erradas (anti-abuso)
    usado      BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Módulo Pedido
-- ------------------------------------------------------------------

CREATE TABLE pedidos (
    id              BIGSERIAL PRIMARY KEY,
    codigo          VARCHAR(24) NOT NULL UNIQUE,  -- código de rastreio p/ cliente: ex. VTX-0001
    cliente_id      BIGINT NOT NULL REFERENCES clientes (id),
    status          VARCHAR(30) NOT NULL DEFAULT 'PENDENTE_PAGAMENTO',
    forma_pagamento VARCHAR(20) NOT NULL,         -- PIX ou CARTAO
    parcelas        INT,                          -- parcelas (só cartão)
    subtotal        NUMERIC(12,2) NOT NULL,
    desconto        NUMERIC(12,2) NOT NULL DEFAULT 0,   -- desconto Pix etc.
    frete           NUMERIC(12,2) NOT NULL DEFAULT 0,   -- 0 quando frete grátis
    total           NUMERIC(12,2) NOT NULL,
    frete_gratis    BOOLEAN NOT NULL DEFAULT FALSE,
    endereco_id     BIGINT,                       -- snapshot do endereço (não quebra se o cliente editar)
    criado_em       TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em   TIMESTAMPTZ NOT NULL DEFAULT now(),
    pago_em         TIMESTAMPTZ,
    enviado_em      TIMESTAMPTZ,
    entregue_em     TIMESTAMPTZ
);

CREATE TABLE itens_pedido (
    id             BIGSERIAL PRIMARY KEY,
    pedido_id      BIGINT NOT NULL REFERENCES pedidos (id) ON DELETE CASCADE,
    variacao_id    BIGINT NOT NULL REFERENCES variacoes_produto (id),
    nome_produto   VARCHAR(160) NOT NULL,   -- snapshot do nome (se o produto mudar, o pedido não muda)
    nome_variacao  VARCHAR(120) NOT NULL,
    sku            VARCHAR(50)  NOT NULL,
    url_imagem     TEXT,
    preco_unitario NUMERIC(12,2) NOT NULL,
    quantidade     INT NOT NULL CHECK (quantidade > 0),
    total          NUMERIC(12,2) NOT NULL
);

CREATE TABLE pagamentos (
    id                     BIGSERIAL PRIMARY KEY,
    pedido_id              BIGINT NOT NULL UNIQUE REFERENCES pedidos (id) ON DELETE CASCADE,  -- 1 pedido = 1 pagamento
    metodo                 VARCHAR(20) NOT NULL,      -- PIX / CARTAO
    provedor               VARCHAR(40),               -- gateway: Mercado Pago, Pagar.me...
    id_transacao_gateway   VARCHAR(100),
    status                 VARCHAR(30) NOT NULL DEFAULT 'PENDENTE',
    valor                  NUMERIC(12,2) NOT NULL,
    desconto               NUMERIC(12,2) NOT NULL DEFAULT 0,
    parcelas               INT,
    ultimos_digitos_cartao VARCHAR(4),                -- nunca guardar número inteiro do cartão
    codigo_qr_pix          TEXT,                      -- imagem base64
    copia_cola_pix         TEXT,                      -- texto p/ copiar e colar
    criado_em              TIMESTAMPTZ NOT NULL DEFAULT now(),
    pago_em                TIMESTAMPTZ
);

-- Rastreamento: cada mudança de status vira uma linha (informação explícita p/ o cliente)
CREATE TABLE eventos_rastreamento (
    id          BIGSERIAL PRIMARY KEY,
    pedido_id   BIGINT NOT NULL REFERENCES pedidos (id) ON DELETE CASCADE,
    status      VARCHAR(30) NOT NULL,
    descricao   VARCHAR(255) NOT NULL,   -- ex.: "Pedido confirmado", "Em transporte p/ CEP 01310-100"
    localizacao VARCHAR(120),            -- cidade/UF do evento de logística
    criado_em   TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Módulo Estoque
-- ------------------------------------------------------------------

-- Toda movimentação é registrada (auditoria): ENTRADA, SAIDA, RESERVADO, LIBERADO, AJUSTE
CREATE TABLE movimentos_estoque (
    id           BIGSERIAL PRIMARY KEY,
    variacao_id  BIGINT NOT NULL REFERENCES variacoes_produto (id) ON DELETE CASCADE,
    tipo         VARCHAR(20) NOT NULL,
    quantidade   INT NOT NULL,           -- sempre positiva; o tipo diz se entrou ou saiu
    pedido_id    BIGINT REFERENCES pedidos (id) ON DELETE SET NULL,
    observacao   VARCHAR(255),           -- ex.: "Reposição fornecedor", "Estorno pedido"
    criado_em    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- "Esgotado — avise-me"
CREATE TABLE avisos_reposicao (
    id           BIGSERIAL PRIMARY KEY,
    variacao_id  BIGINT NOT NULL REFERENCES variacoes_produto (id) ON DELETE CASCADE,
    email        VARCHAR(190) NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',  -- PENDENTE / NOTIFICADO / EXPIRADO
    notificado_em TIMESTAMPTZ,
    criado_em    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Módulo Avaliação
-- ------------------------------------------------------------------

CREATE TABLE avaliacoes (
    id          BIGSERIAL PRIMARY KEY,
    produto_id  BIGINT NOT NULL REFERENCES produtos (id) ON DELETE CASCADE,
    cliente_id  BIGINT NOT NULL REFERENCES clientes (id),
    pedido_id   BIGINT NOT NULL REFERENCES pedidos (id),   -- prova de compra
    nota        INT NOT NULL CHECK (nota BETWEEN 1 AND 5),
    comentario  TEXT,
    criado_em   TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_avaliacao_pedido UNIQUE (produto_id, pedido_id)  -- só 1 avaliação por produto+pedido
);

-- ============================================================
-- Índices (toda FK que é pesquisada vira índice)
-- ============================================================
CREATE INDEX idx_produtos_categoria   ON produtos (categoria_id);
CREATE INDEX idx_produtos_fornecedor  ON produtos (fornecedor_id);
CREATE INDEX idx_variacoes_produto    ON variacoes_produto (produto_id);
CREATE INDEX idx_enderecos_cliente    ON enderecos (cliente_id);
CREATE INDEX idx_pedidos_cliente      ON pedidos (cliente_id);
CREATE INDEX idx_pedidos_status       ON pedidos (status);
CREATE INDEX idx_itens_pedido_pedido  ON itens_pedido (pedido_id);
CREATE INDEX idx_pagamentos_pedido    ON pagamentos (pedido_id);
CREATE INDEX idx_eventos_pedido       ON eventos_rastreamento (pedido_id);
CREATE INDEX idx_movimentos_variacao  ON movimentos_estoque (variacao_id);
CREATE INDEX idx_avisos_variacao      ON avisos_reposicao (variacao_id);
CREATE INDEX idx_avaliacoes_produto   ON avaliacoes (produto_id);
CREATE INDEX idx_otp_email            ON codigos_otp (email);

-- Índice parcial: ajuda a listar "estoque abaixo do ponto de reposição" (painel admin)
CREATE INDEX idx_variacoes_estoque_baixo
    ON variacoes_produto (quantidade_estoque)
    WHERE quantidade_estoque <= estoque_minimo_alerta;