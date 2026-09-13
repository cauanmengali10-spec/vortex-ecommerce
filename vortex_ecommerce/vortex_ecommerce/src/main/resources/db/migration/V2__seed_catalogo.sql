-- ============================================================
-- V2__seed_catalogo.sql  |  Dados de exemplo p/ desenvolver
-- ============================================================

-- Categorias
INSERT INTO categorias (id, nome, slug, ativo) VALUES
    ('11111111-1111-1111-1111-111111111101', 'Mouses',    'mouses',    TRUE),
    ('11111111-1111-1111-1111-111111111102', 'Teclados',  'teclados',  TRUE),
    ('11111111-1111-1111-1111-111111111103', 'Mousepads', 'mousepads', TRUE),
    ('11111111-1111-1111-1111-111111111104', 'Headsets',  'headsets',  TRUE);

-- Fornecedor
INSERT INTO fornecedores (id, nome, cnpj, nome_contato, email, telefone, prazo_reposicao_dias) VALUES
    ('22222222-2222-2222-2222-222222222201', 'Vortex Distribuidora de Periféricos', '12.345.678/0001-90',
     'Carlos Andrade', 'compras@vortexdist.com.br', '(11) 4002-8922', 15);

-- Produtos
INSERT INTO produtos (id, categoria_id, fornecedor_id, nome, slug, descricao_curta, marca, destaque, ativo) VALUES
    ('33333333-3333-3333-3333-333333333301', '11111111-1111-1111-1111-111111111101', '22222222-2222-2222-2222-222222222201',
     'Mouse Gamer Sem Fio Vortex X1', 'mouse-gamer-sem-fio-vortex-x1', 'Conexão 2.4GHz, 16.000 DPI, bateria de 80h', 'Vortex', TRUE, TRUE),
    ('33333333-3333-3333-3333-333333333302', '11111111-1111-1111-1111-111111111101', '22222222-2222-2222-2222-222222222201',
     'Mouse Gamer Letal Pro', 'mouse-gamer-letal-pro', 'Sensor óptico 26.000 DPI, 8 botões', 'Letal Gear', FALSE, TRUE),
    ('33333333-3333-3333-3333-333333333303', '11111111-1111-1111-1111-111111111102', '22222222-2222-2222-2222-222222222201',
     'Teclado Mecânico RGB 60%', 'teclado-mecanico-rgb-60', 'Switches hot-swap, RGB, ABNT2', 'Vortex', TRUE, TRUE),
    ('33333333-3333-3333-3333-333333333304', '11111111-1111-1111-1111-111111111102', '22222222-2222-2222-2222-222222222201',
     'Teclado Compacto Office', 'teclado-compacto-office', 'Membrana silenciosa, USB-C', 'OfficeKey', FALSE, TRUE),
    ('33333333-3333-3333-3333-333333333305', '11111111-1111-1111-1111-111111111103', '22222222-2222-2222-2222-222222222201',
     'Mousepad Speed XL', 'mousepad-speed-xl', 'Superfície de alta velocidade 400x450mm', 'Vortex', FALSE, TRUE),
    ('33333333-3333-3333-3333-333333333306', '11111111-1111-1111-1111-111111111103', '22222222-2222-2222-2222-222222222201',
     'Mousepad Control Gamer XXL', 'mousepad-control-gamer-xxl', 'Base antiderrapante 900x400mm', 'Vortex', TRUE, TRUE),
    ('33333333-3333-3333-3333-333333333307', '11111111-1111-1111-1111-111111111104', '22222222-2222-2222-2222-222222222201',
     'Headset Gamer 7.1 Surround', 'headset-gamer-7-1-surround', 'Som 7.1, microfone c/ cancelamento', 'Vortex', TRUE, TRUE),
    ('33333333-3333-3333-3333-333333333308', '11111111-1111-1111-1111-111111111104', '22222222-2222-2222-2222-222222222201',
     'Headset Bluetooth', 'headset-bluetooth', 'Bluetooth 5.3, bateria 40h', 'SoundMax', FALSE, TRUE);

-- Variações: preco = "de", preco_promocional = "por"
INSERT INTO variacoes_produto (id, produto_id, sku, nome, preco, preco_promocional, preco_custo, quantidade_estoque, estoque_minimo_alerta) VALUES
    ('44444444-4444-4444-4444-444444444401', '33333333-3333-3333-3333-333333333301', 'X1-PRETO',  'Preto',      199.90, 149.90, 98.00,  42, 5),
    ('44444444-4444-4444-4444-444444444402', '33333333-3333-3333-3333-333333333301', 'X1-BRANCO', 'Branco',     199.90, 149.90, 98.00,   3, 5),   -- ABAIXO do ponto de reposição
    ('44444444-4444-4444-4444-444444444403', '33333333-3333-3333-3333-333333333302', 'LP-PRETO',  'Preto',      349.90,  NULL,  205.00,  18, 5),
    ('44444444-4444-4444-4444-444444444404', '33333333-3333-3333-3333-333333333303', 'TC60-R',    'Switch Red', 449.90, 379.90, 260.00,  12, 5),
    ('44444444-4444-4444-4444-444444444405', '33333333-3333-3333-3333-333333333303', 'TC60-B',    'Switch Blue',449.90, 379.90, 260.00,   0, 5),   -- ESGOTADO: testa o "avise-me"
    ('44444444-4444-4444-4444-444444444406', '33333333-3333-3333-3333-333333333304', 'TOFF-USB',  'Preto',      129.90,  99.90,  58.00,  30, 5),
    ('44444444-4444-4444-4444-444444444407', '33333333-3333-3333-3333-333333333305', 'SP-XL',     'Preto',       89.90,  69.90,  40.00,  25, 5),
    ('44444444-4444-4444-4444-444444444408', '33333333-3333-3333-3333-333333333306', 'CT-XXL',    'Preto',      119.90,  NULL,   55.00,   8, 10),  -- estoque baixo (8 < 10)
    ('44444444-4444-4444-4444-444444444409', '33333333-3333-3333-3333-333333333307', 'HS71',      'Preto',      299.90, 249.90, 160.00,  15, 5),
    ('44444444-4444-4444-4444-444444444410', '33333333-3333-3333-3333-333333333308', 'HSBT',      'Preto',      259.90,  NULL,  140.00,  20, 5);