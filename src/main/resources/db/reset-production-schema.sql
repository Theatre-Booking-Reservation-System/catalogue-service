-- ============================================================
-- Manual schema reset for catalogue-service (PostgreSQL).
--
-- Use this ONLY when you intend to drop and recreate the production and
-- performance tables after the schema changes: production's single `title`
-- and `description` columns (replacing the per-language variants) plus the
-- new duration / age_restriction / cast_crew columns, and performance losing
-- its release_date / early_access_opens_at / is_early_access_active columns.
--
-- This script is NOT run automatically. Run it by hand against
-- catalogue_db, e.g.:
--   psql -h localhost -U <user> -d catalogue_db -f reset-production-schema.sql
--
-- After running it, start catalogue-service with spring.jpa.hibernate.ddl-auto
-- = update (the default) so Hibernate recreates the tables from the JPA
-- entities, then data.sql reseeds them.
-- ============================================================

-- performance has a FK to production, so drop it first (or use CASCADE).
DROP TABLE IF EXISTS performance CASCADE;
DROP TABLE IF EXISTS production CASCADE;

-- Optional: recreate the tables explicitly to match the current entity
-- mappings. If you rely on Hibernate ddl-auto=update you can skip these
-- CREATE statements and let the app build the schema on startup.
CREATE TABLE production (
    production_id     UUID PRIMARY KEY,
    title             VARCHAR(255)   NOT NULL,
    language          VARCHAR(20)    NOT NULL,
    genre             VARCHAR(255),
    description       TEXT,
    base_ticket_cost  NUMERIC(10, 2) NOT NULL,
    duration          VARCHAR(255),
    age_restriction   VARCHAR(255),
    cast_crew         TEXT,
    release_date      DATE           NOT NULL,
    end_date          DATE,
    poster_image_url  TEXT,
    status            INTEGER        NOT NULL,
    added_by          VARCHAR(255),
    added_date        TIMESTAMP,
    modified_by       VARCHAR(255),
    modified_date     TIMESTAMP
);

CREATE TABLE performance (
    performance_id    UUID PRIMARY KEY,
    production_id     UUID           NOT NULL,
    date              DATE           NOT NULL,
    time              TIME           NOT NULL,
    session_type      VARCHAR(10)    NOT NULL,
    status            INTEGER        NOT NULL,
    added_by          VARCHAR(255),
    added_date        TIMESTAMP,
    modified_by       VARCHAR(255),
    modified_date     TIMESTAMP,
    CONSTRAINT fk_performance_production
        FOREIGN KEY (production_id) REFERENCES production (production_id)
);

-- ============================================================
-- Mock data (mirrors src/main/resources/data.sql). Idempotent via
-- fixed UUIDs + ON CONFLICT DO NOTHING.
-- ============================================================

INSERT INTO production (
    production_id, title,
    language, genre, description,
    base_ticket_cost, duration, age_restriction, cast_crew,
    release_date, end_date, poster_image_url, status,
    added_by, added_date
) VALUES (
    '11111111-1111-1111-1111-111111111111', 'Hamlet',
    'ENGLISH', 'Tragedy',
    'A young prince seeks revenge for his father''s murder in this timeless Shakespearean masterpiece.',
    2500.00, '2h 45m', '12+', '[{"key":"Director","value":"Ranjan Perera"},{"key":"Lead Actor","value":"Sanath Gunathilake"},{"key":"Lead Actress","value":"Malani Fonseka"}]',
    '2026-10-15', '2026-10-31', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==', 1,
    'admin', CURRENT_TIMESTAMP
) ON CONFLICT (production_id) DO NOTHING;

INSERT INTO production (
    production_id, title,
    language, genre, description,
    base_ticket_cost, duration, age_restriction, cast_crew,
    release_date, end_date, poster_image_url, status,
    added_by, added_date
) VALUES (
    '22222222-2222-2222-2222-222222222222', 'Romeo and Juliet',
    'ENGLISH', 'Romance',
    'Two young star-crossed lovers from rival families meet a tragic end in Verona.',
    3000.00, '2h 15m', '12+', '[{"key":"Director","value":"Nadeeka Silva"},{"key":"Lead Actor","value":"Pubudu Chathuranga"},{"key":"Lead Actress","value":"Semini Iddamalgoda"}]',
    '2026-11-01', '2026-11-20', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==', 1,
    'admin', CURRENT_TIMESTAMP
) ON CONFLICT (production_id) DO NOTHING;

INSERT INTO production (
    production_id, title,
    language, genre, description,
    base_ticket_cost, duration, age_restriction, cast_crew,
    release_date, end_date, poster_image_url, status,
    added_by, added_date
) VALUES (
    '33333333-3333-3333-3333-333333333333', 'A Midsummer Night''s Dream',
    'ENGLISH', 'Comedy',
    'A delightful comedy of love, magic, and mistaken identities set in an enchanted forest.',
    2000.00, '2h 00m', 'All Ages', '[{"key":"Director","value":"Kaushalya Fernando"},{"key":"Lead Actor","value":"Jackson Anthony"},{"key":"Lead Actress","value":"Damayanthi Fonseka"}]',
    '2026-09-20', '2026-10-05', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==', 9,
    'admin', CURRENT_TIMESTAMP
) ON CONFLICT (production_id) DO NOTHING;

-- Performances for 'Hamlet' (production 1)
-- NOTE: the first performance uses a fixed UUID that seat-service references
-- as the demo performance for its seeded performance_seat rows.
INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f1', '11111111-1111-1111-1111-111111111111', '2026-10-15', '19:30:00', 'EVENING',
    1, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;

INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f2', '11111111-1111-1111-1111-111111111111', '2026-10-18', '14:00:00', 'MATINEE',
    1, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;

INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f3', '11111111-1111-1111-1111-111111111111', '2026-10-20', '19:30:00', 'EVENING',
    9, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;

-- Performances for 'Romeo and Juliet' (production 2)
INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f4', '22222222-2222-2222-2222-222222222222', '2026-11-01', '19:00:00', 'EVENING',
    1, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;

INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f5', '22222222-2222-2222-2222-222222222222', '2026-11-02', '15:00:00', 'MATINEE',
    1, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;
