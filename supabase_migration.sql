-- Sale Fulbito - Supabase Database Migration Script
-- Este script crea todas las tablas necesarias para el backend de la aplicación

-- Habilitar UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabla de perfiles de usuario
CREATE TABLE IF NOT EXISTS user_profiles (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    role VARCHAR(50) NOT NULL CHECK (role IN ('JUGADOR', 'DUEÑO')),
    position VARCHAR(100),
    level VARCHAR(100),
    complex_name VARCHAR(255),
    complex_address VARCHAR(255),
    cuit VARCHAR(50),
    balance DECIMAL(10, 2) DEFAULT 0.0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Tabla de canchas
CREATE TABLE IF NOT EXISTS courts (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('F5', 'F7', 'F9')),
    surface VARCHAR(100) NOT NULL CHECK (surface IN ('Sintético', 'Cemento', 'Césped')),
    price_per_hour DECIMAL(10, 2) NOT NULL,
    image_url TEXT,
    rating DECIMAL(3, 2) DEFAULT 4.5,
    is_favorite BOOLEAN DEFAULT FALSE,
    is_owner_court BOOLEAN DEFAULT FALSE,
    latitude DECIMAL(10, 8) DEFAULT 0.0,
    longitude DECIMAL(11, 8) DEFAULT 0.0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Tabla de reservas
CREATE TABLE IF NOT EXISTS bookings (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,
    court_name VARCHAR(255) NOT NULL,
    court_type VARCHAR(50) NOT NULL,
    date VARCHAR(50) NOT NULL,
    time_slot VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(100) NOT NULL CHECK (status IN ('Confirmado', 'Pendiente Pago', 'Seña Pagada', 'Pagado Total', 'En Juego', 'Cancha Ocupada')),
    team_name VARCHAR(255) NOT NULL,
    is_my_booking BOOLEAN DEFAULT FALSE,
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Tabla de slots de partidos
CREATE TABLE IF NOT EXISTS match_slots (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,
    court_name VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    date VARCHAR(50) NOT NULL,
    time_slot VARCHAR(50) NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    match_type VARCHAR(50) NOT NULL CHECK (match_type IN ('F5', 'F7', 'F9')),
    slots_missing INTEGER NOT NULL,
    role_needed VARCHAR(100) NOT NULL CHECK (role_needed IN ('Arquero', 'Defensor', 'Mediocampista', 'Delantero', 'Cualquiera')),
    price_per_player DECIMAL(10, 2) NOT NULL,
    joined_players TEXT,
    status VARCHAR(50) NOT NULL CHECK (status IN ('Abierto', 'Completo')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Tabla de reseñas de canchas
CREATE TABLE IF NOT EXISTS court_reviews (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,
    author VARCHAR(255) NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT NOT NULL,
    date VARCHAR(50) NOT NULL,
    visit_verified BOOLEAN DEFAULT FALSE,
    visit_date VARCHAR(50),
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Tabla de progreso de videos de usuarios
CREATE TABLE IF NOT EXISTS user_video_progress (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    date VARCHAR(50) NOT NULL, -- Format: "YYYY-MM-DD"
    videos_watched INTEGER DEFAULT 0,
    consecutive_days INTEGER DEFAULT 0,
    last_coupon_date VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Tabla de visitas a canchas
CREATE TABLE IF NOT EXISTS court_visits (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES user_profiles(id) ON DELETE CASCADE,
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,
    visit_date VARCHAR(50) NOT NULL, -- Format: "YYYY-MM-DD"
    booking_id UUID REFERENCES bookings(id) ON DELETE SET NULL,
    verified BOOLEAN DEFAULT FALSE,
    user_latitude DECIMAL(10, 8),
    user_longitude DECIMAL(11, 8),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_user_profiles_email ON user_profiles(email);
CREATE INDEX IF NOT EXISTS idx_courts_neighborhood ON courts(neighborhood);
CREATE INDEX IF NOT EXISTS idx_bookings_court_id ON bookings(court_id);
CREATE INDEX IF NOT EXISTS idx_bookings_user_id ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_match_slots_court_id ON match_slots(court_id);
CREATE INDEX IF NOT EXISTS idx_court_reviews_court_id ON court_reviews(court_id);
CREATE INDEX IF NOT EXISTS idx_court_reviews_author ON court_reviews(author);
CREATE INDEX IF NOT EXISTS idx_user_video_progress_user_id ON user_video_progress(user_id);
CREATE INDEX IF NOT EXISTS idx_user_video_progress_date ON user_video_progress(date);
CREATE INDEX IF NOT EXISTS idx_court_visits_user_id ON court_visits(user_id);
CREATE INDEX IF NOT EXISTS idx_court_visits_court_id ON court_visits(court_id);

-- Crear triggers para updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_user_profiles_updated_at BEFORE UPDATE ON user_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_courts_updated_at BEFORE UPDATE ON courts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_bookings_updated_at BEFORE UPDATE ON bookings
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_match_slots_updated_at BEFORE UPDATE ON match_slots
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_court_reviews_updated_at BEFORE UPDATE ON court_reviews
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_user_video_progress_updated_at BEFORE UPDATE ON user_video_progress
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_court_visits_updated_at BEFORE UPDATE ON court_visits
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Habilitar Row Level Security (RLS)
ALTER TABLE user_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE courts ENABLE ROW LEVEL SECURITY;
ALTER TABLE bookings ENABLE ROW LEVEL SECURITY;
ALTER TABLE match_slots ENABLE ROW LEVEL SECURITY;
ALTER TABLE court_reviews ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_video_progress ENABLE ROW LEVEL SECURITY;
ALTER TABLE court_visits ENABLE ROW LEVEL SECURITY;

-- Políticas RLS básicas (ajustar según necesidades de seguridad)
CREATE POLICY "Public read access for user_profiles" ON user_profiles
    FOR SELECT USING (true);

CREATE POLICY "User insert own profile" ON user_profiles
    FOR INSERT WITH CHECK (true);

CREATE POLICY "User update own profile" ON user_profiles
    FOR UPDATE USING (true);

CREATE POLICY "Public read access for courts" ON courts
    FOR SELECT USING (true);

CREATE POLICY "Public read access for bookings" ON bookings
    FOR SELECT USING (true);

CREATE POLICY "Public read access for match_slots" ON match_slots
    FOR SELECT USING (true);

CREATE POLICY "Public read access for court_reviews" ON court_reviews
    FOR SELECT USING (true);

CREATE POLICY "Public insert court_reviews" ON court_reviews
    FOR INSERT WITH CHECK (true);

CREATE POLICY "Public read access for user_video_progress" ON user_video_progress
    FOR SELECT USING (true);

CREATE POLICY "User insert own video_progress" ON user_video_progress
    FOR INSERT WITH CHECK (true);

CREATE POLICY "User update own video_progress" ON user_video_progress
    FOR UPDATE USING (true);

CREATE POLICY "Public read access for court_visits" ON court_visits
    FOR SELECT USING (true);

CREATE POLICY "User insert own visits" ON court_visits
    FOR INSERT WITH CHECK (true);

CREATE POLICY "User update own visits" ON court_visits
    FOR UPDATE USING (true);

-- Datos iniciales de ejemplo (opcional - para prueba cerrada)
INSERT INTO courts (name, address, neighborhood, type, surface, price_per_hour, rating, latitude, longitude) VALUES
('Complejo Tercer Tiempo', 'Av. Reyes Católicos 1450', 'Tres Cerritos', 'F5', 'Sintético', 15000.0, 4.9, -24.7854, -65.4112),
('La Loma Fulbito', 'Ruta 28 Km 2', 'La Loma', 'F9', 'Césped', 18000.0, 4.8, -24.7721, -65.4098),
('Canchas El Tribuno', 'Av. Combatientes de Malvinas 3200', 'El Tribuno', 'F7', 'Sintético', 12000.0, 4.5, -24.7890, -65.4180),
('Salta Fútbol Club', 'Av. Belgrano 840', 'Macrocentro', 'F5', 'Cemento', 13000.0, 4.4, -24.7871, -65.4163),
('Complejo El Clásico', 'Av. del Libertador 450', 'Grand Bourg', 'F5 / F7', 'Sintético', 16000.0, 4.7, -24.7915, -65.4234)
ON CONFLICT DO NOTHING;

-- Reseñas iniciales de ejemplo
INSERT INTO court_reviews (court_id, author, rating, comment, date, visit_verified, visit_date) VALUES
((SELECT id FROM courts WHERE name = 'Complejo Tercer Tiempo' LIMIT 1), 'Marcos Cabrera', 5, '¡Excelente la cancha y los vestuarios de primer nivel! El buffet tiene las mejores empanadas salteñas.', '04/07/2026', true, '04/07/2026'),
((SELECT id FROM courts WHERE name = 'Complejo Tercer Tiempo' LIMIT 1), 'Facundo Díaz', 4, 'Muy buena iluminación de noche. La pelota pica parejo.', '03/07/2026', true, '03/07/2026'),
((SELECT id FROM courts WHERE name = 'La Loma Fulbito' LIMIT 1), 'Chango Gómez', 5, 'El césped natural está impecable. Da gusto jugar F9 acá. Muy recomendado para los pibes.', '02/07/2026', true, '02/07/2026'),
((SELECT id FROM courts WHERE name = 'Canchas El Tribuno' LIMIT 1), 'Diego Torres', 4, 'Lindo complejo, el asado que te podés comer después del partido es clave.', '01/07/2026', true, '01/07/2026'),
((SELECT id FROM courts WHERE name = 'Salta Fútbol Club' LIMIT 1), 'Lucho Flores', 4, 'Cancha de cemento rápida. El barrio es muy futbolero, se arman lindos picaditos.', '30/06/2026', true, '30/06/2026'),
((SELECT id FROM courts WHERE name = 'Complejo El Clásico' LIMIT 1), 'Matias S.', 5, 'Espectacular el sintético, no te quema las rodillas. Las duchas salen hirviendo, un golazo.', '29/06/2026', true, '29/06/2026')
ON CONFLICT DO NOTHING;