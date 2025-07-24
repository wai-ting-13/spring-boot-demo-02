INSERT INTO sbq.app_role (id, role_name) VALUES (0, 'ADMIN');
INSERT INTO sbq.app_role (id, role_name) VALUES (1, 'MANAGER');
INSERT INTO sbq.app_role (id, role_name) VALUES (2, 'USER');

INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'A Military History Of The Western World, Vol. I: From The Earliest Times To The Battle Of Lepanto', '978-0306803048', '2025-01-01');
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'A Military History Of The Western World, Vol. III: From the American Civil War to the End of World War II', '978-0306803062', '2025-01-01');
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'Generalship of Alexander the Great', '978-0306813306', '2025-01-01');
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'A Military History Of The Western World, Vol. II: From The Defeat Of The Spanish Armada To The Battle Of Waterloo', '978-0306803055', '2025-01-01');
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'Rust for Rustaceans', '978-1718501850', CURRENT_DATE);
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'Strategy: Second Revised Edition', '978-0452010710', '2025-01-01');
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'Panzer Leader', '978-0306811012', '2025-01-01');
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'Scipio Africanus: Greater Than Napoleon', '978-0306813634', '2025-01-01');
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'Spring Boot in Action. First Edition Edition', '978-1617292545', CURRENT_DATE);
INSERT INTO  sbq.book (id, title, isbn, created_at)  VALUES (GEN_RANDOM_UUID(), 'Elixir in Action. First Edition', '978-1617292019', CURRENT_DATE);