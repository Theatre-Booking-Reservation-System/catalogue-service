-- ============================================================
-- Seed data for catalogue-service (PostgreSQL).
-- Idempotent: fixed UUIDs + ON CONFLICT DO NOTHING, so this file
-- is safe to run on every startup against a persistent database.
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
    2500.00, '2h 45m', 12, 'Dir. Ranjan Perera; Cast: Sanath Gunathilake, Malani Fonseka',
    '2026-10-15', '2026-10-31', 'https://cdn.sapumaltheatre.lk/posters/hamlet.jpg', 1,
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
    3000.00, '2h 15m', 12, 'Dir. Nadeeka Silva; Cast: Pubudu Chathuranga, Semini Iddamalgoda',
    '2026-11-01', '2026-11-20', 'https://cdn.sapumaltheatre.lk/posters/romeo-and-juliet.jpg', 1,
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
    2000.00, '2h 00m', 0, 'Dir. Kaushalya Fernando; Cast: Jackson Anthony, Damayanthi Fonseka',
    '2026-09-20', '2026-10-05', 'https://cdn.sapumaltheatre.lk/posters/midsummer-nights-dream.jpg', 9,
    'admin', CURRENT_TIMESTAMP
) ON CONFLICT (production_id) DO NOTHING;

-- Performances for 'Hamlet' (production 1)
-- NOTE: the first performance uses a fixed UUID that seat-service references
-- as the demo performance for its seeded performance_seat rows.
INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    release_date, early_access_opens_at, is_early_access_active,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f1', '11111111-1111-1111-1111-111111111111', '2026-10-15', '19:30:00', 'EVENING',
    '2026-10-15', '2026-10-08', TRUE,
    1, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;

INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    release_date, early_access_opens_at, is_early_access_active,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f2', '11111111-1111-1111-1111-111111111111', '2026-10-18', '14:00:00', 'MATINEE',
    '2026-10-18', '2026-10-11', FALSE,
    1, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;

INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    release_date, early_access_opens_at, is_early_access_active,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f3', '11111111-1111-1111-1111-111111111111', '2026-10-20', '19:30:00', 'EVENING',
    '2026-10-20', '2026-10-13', FALSE,
    9, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;

-- Performances for 'Romeo and Juliet' (production 2)
INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    release_date, early_access_opens_at, is_early_access_active,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f4', '22222222-2222-2222-2222-222222222222', '2026-11-01', '19:00:00', 'EVENING',
    '2026-11-01', '2026-10-25', TRUE,
    1, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;

INSERT INTO performance (
    performance_id, production_id, date, time, session_type,
    release_date, early_access_opens_at, is_early_access_active,
    status, added_by, added_date
) VALUES (
    'c0000000-0000-0000-0000-0000000000f5', '22222222-2222-2222-2222-222222222222', '2026-11-02', '15:00:00', 'MATINEE',
    '2026-11-02', '2026-10-26', FALSE,
    1, 'admin', CURRENT_TIMESTAMP
) ON CONFLICT (performance_id) DO NOTHING;
