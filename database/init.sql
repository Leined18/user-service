-- User Service Database Initialization Script
-- This script creates the initial schema for the User Service

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    encoded_password VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Create user_roles table (many-to-many relationship)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(active);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles(user_id);

-- Insert sample data (optional - comment out for production)
-- Password: "password123" encoded with BCrypt
INSERT INTO users (id, email, full_name, encoded_password, active, created_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000', 
     'admin@example.com', 
     'Admin User', 
     '$2a$10$N9qo8uLOickgx2ZMRZoMye7J6YZ/JqJ5LQJHvGz/JqJ5LQJHvGz/J', 
     true, 
     NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (user_id, role)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000', 'ADMIN')
ON CONFLICT DO NOTHING;

-- Add comments to tables
COMMENT ON TABLE users IS 'Stores user account information';
COMMENT ON TABLE user_roles IS 'Stores user role assignments';
COMMENT ON COLUMN users.id IS 'Unique identifier for the user (UUID)';
COMMENT ON COLUMN users.email IS 'User email address (must be unique)';
COMMENT ON COLUMN users.encoded_password IS 'BCrypt encoded password';
