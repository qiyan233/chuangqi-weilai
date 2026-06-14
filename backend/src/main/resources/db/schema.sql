-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,  -- ENTREPRENEUR, INVESTOR, ADMIN
    real_name VARCHAR(50),
    phone VARCHAR(20),
    city VARCHAR(50),
    education VARCHAR(20),
    work_years INTEGER,
    company VARCHAR(100),
    position VARCHAR(50),
    avatar_url VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 项目表
CREATE TABLE IF NOT EXISTS projects (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    industry VARCHAR(50),
    stage VARCHAR(20),
    description TEXT,
    pain_point TEXT,
    business_model TEXT,
    core_tech TEXT,
    target_market TEXT,
    demo_url VARCHAR(255),
    user_count INTEGER,
    dau_mau VARCHAR(50),
    mrr VARCHAR(50),
    arr VARCHAR(50),
    funding_round VARCHAR(20),
    funding_amount VARCHAR(50),
    valuation VARCHAR(50),
    equity_percent VARCHAR(20),
    fund_usage TEXT,
    bp_file_url VARCHAR(255),
    financial_model_url VARCHAR(255),
    status VARCHAR(20) DEFAULT 'DRAFT',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 入驻申请表
CREATE TABLE IF NOT EXISTS applications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    project_id INTEGER,
    team_members TEXT,
    equity_structure TEXT,
    team_tags TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    review_notes TEXT,
    reviewed_by INTEGER,
    reviewed_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_projects_user_id ON projects(user_id);
CREATE INDEX IF NOT EXISTS idx_projects_status ON projects(status);
CREATE INDEX IF NOT EXISTS idx_projects_industry ON projects(industry);
CREATE INDEX IF NOT EXISTS idx_applications_user_id ON applications(user_id);
CREATE INDEX IF NOT EXISTS idx_applications_status ON applications(status);
