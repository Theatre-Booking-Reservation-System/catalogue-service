INSERT INTO production (
    production_id, title_en, title_si, title_ta,
    language, genre,
    description_en, description_si, description_ta,
    base_ticket_cost, release_date, status,
    added_by, added_date
) VALUES (
    RANDOM_UUID(), 'Hamlet', 'හැම්ලෙට්', 'ஹாம்லெட்',
    'ENGLISH', 'Tragedy',
    'A young prince seeks revenge for his father''s murder in this timeless Shakespearean masterpiece.',
    'තම පියාගේ මිනීමැරුමට පළිගැනීමක් සොයන තරුණ කුමාරවරයෙකුගේ කතාව.',
    'தனது தந்தையின் கொலைக்கு பழிவாங்க முயலும் ஒரு இளம் இளவரசனின் கதை.',
    2500.00, '2026-10-15', 1,
    'admin', CURRENT_TIMESTAMP
);

INSERT INTO production (
    production_id, title_en, title_si, title_ta,
    language, genre,
    description_en, description_si, description_ta,
    base_ticket_cost, release_date, status,
    added_by, added_date
) VALUES (
    RANDOM_UUID(), 'Romeo and Juliet', 'රොමියෝ සහ ජූලියට්', 'ரோமியோ மற்றும் ஜூலியட்',
    'ENGLISH', 'Romance',
    'Two young star-crossed lovers from rival families meet a tragic end in Verona.',
    'ශත්‍රු පවුල් දෙකකින් පැමිණෙන තරුණ ප්‍රේමවන්තයන් දෙදෙනෙකුගේ ඛේදජනක ඉරණම.',
    'எதிரி குடும்பங்களில் இருந்து வரும் இரண்டு இளம் காதலர்களின் சோகமான முடிவு.',
    3000.00, '2026-11-01', 1,
    'admin', CURRENT_TIMESTAMP
);

INSERT INTO production (
    production_id, title_en, title_si, title_ta,
    language, genre,
    description_en, description_si, description_ta,
    base_ticket_cost, release_date, status,
    added_by, added_date
) VALUES (
    RANDOM_UUID(), 'A Midsummer Night''s Dream', 'මිහිකත රාත්‍රී සිහිනය', 'மிட்சம்மர் இரவு கனவு',
    'ENGLISH', 'Comedy',
    'A delightful comedy of love, magic, and mistaken identities set in an enchanted forest.',
    'මායාකාරී වනාන්තරයක් තුළ ප්‍රේමය, සිහිනය සහ අනන්‍යතා ව්‍යාකූලතා පිළිබඳ විනෝදජනක කතාවක්.',
    'மந்திர காட்டில் அமைந்த காதல், மாயம் மற்றும் குழப்பமான அடையாளங்களின் கதை.',
    2000.00, '2026-09-20', 9,
    'admin', CURRENT_TIMESTAMP
);
