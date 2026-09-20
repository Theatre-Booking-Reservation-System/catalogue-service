-- ============================================================
-- Seed data for catalogue-service (PostgreSQL).
-- Idempotent: fixed UUIDs + ON CONFLICT DO NOTHING, so this file
-- is safe to run on every startup against a persistent database.
-- ============================================================

INSERT INTO production (
    production_id, title_en, title_si, title_ta,
    language, genre,
    description_en, description_si, description_ta,
    base_ticket_cost, release_date, status,
    added_by, added_date
) VALUES (
    '11111111-1111-1111-1111-111111111111', 'Hamlet', 'හැම්ලෙට්', 'ஹாம்லெட்',
    'ENGLISH', 'Tragedy',
    'A young prince seeks revenge for his father''s murder in this timeless Shakespearean masterpiece.',
    'තම පියාගේ මිනීමැරුමට පළිගැනීමක් සොයන තරුණ කුමාරවරයෙකුගේ කතාව.',
    'தனது தந்தையின் கொலைக்கு பழிவாங்க முயலும் ஒரு இளம் இளவரசனின் கதை.',
    2500.00, '2026-10-15', 1,
    'admin', CURRENT_TIMESTAMP
) ON CONFLICT (production_id) DO NOTHING;

INSERT INTO production (
    production_id, title_en, title_si, title_ta,
    language, genre,
    description_en, description_si, description_ta,
    base_ticket_cost, release_date, status,
    added_by, added_date
) VALUES (
    '22222222-2222-2222-2222-222222222222', 'Romeo and Juliet', 'රොමියෝ සහ ජූලියට්', 'ரோமியோ மற்றும் ஜூலியட்',
    'ENGLISH', 'Romance',
    'Two young star-crossed lovers from rival families meet a tragic end in Verona.',
    'ශත්‍රු පවුල් දෙකකින් පැමිණෙන තරුණ ප්‍රේමවන්තයන් දෙදෙනෙකුගේ ඛේදජනක ඉරණම.',
    'எதிரி குடும்பங்களில் இருந்து வரும் இரண்டு இளம் காதலர்களின் சோகமான முடிவு.',
    3000.00, '2026-11-01', 1,
    'admin', CURRENT_TIMESTAMP
) ON CONFLICT (production_id) DO NOTHING;

INSERT INTO production (
    production_id, title_en, title_si, title_ta,
    language, genre,
    description_en, description_si, description_ta,
    base_ticket_cost, release_date, status,
    added_by, added_date
) VALUES (
    '33333333-3333-3333-3333-333333333333', 'A Midsummer Night''s Dream', 'මිහිකත රාත්‍රී සිහිනය', 'மிட்சம்மர் இரவு கனவு',
    'ENGLISH', 'Comedy',
    'A delightful comedy of love, magic, and mistaken identities set in an enchanted forest.',
    'මායාකාරී වනාන්තරයක් තුළ ප්‍රේමය, සිහිනය සහ අනන්‍යතා ව්‍යාකූලතා පිළිබඳ විනෝදජනක කතාවක්.',
    'மந்திர காட்டில் அமைந்த காதல், மாயம் மற்றும் குழப்பமான அடையாளங்களின் கதை.',
    2000.00, '2026-09-20', 9,
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
