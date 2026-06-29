CREATE TABLE IF NOT EXISTS users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       phone VARCHAR(20),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS expense_group (
                               id UUID PRIMARY KEY,
                               name VARCHAR(100) NOT NULL,
                               created_by UUID NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_group_creator
                                   FOREIGN KEY (created_by)
                                       REFERENCES users(id)
);


CREATE TABLE IF NOT EXISTS group_member (
                              id UUID PRIMARY KEY,
                              group_id UUID NOT NULL,
                              user_id UUID NOT NULL,
                              joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_member_group
                                  FOREIGN KEY (group_id)
                                      REFERENCES expense_group(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_member_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id),

                              CONSTRAINT uk_group_user
                                  UNIQUE(group_id, user_id)
);


CREATE TABLE IF NOT EXISTS expense (
                         id UUID PRIMARY KEY,

                         group_id UUID NOT NULL,

                         description VARCHAR(255),

                         amount NUMERIC(12,2) NOT NULL,

                         paid_by UUID NOT NULL,

                         split_type VARCHAR(20) NOT NULL,

                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_expense_group
                             FOREIGN KEY(group_id)
                                 REFERENCES expense_group(id)
                                 ON DELETE CASCADE,

                         CONSTRAINT fk_paid_by
                             FOREIGN KEY(paid_by)
                                 REFERENCES users(id),

                         CONSTRAINT chk_split_type
                             CHECK (split_type IN ('EQUAL','EXACT','PERCENTAGE'))
);


CREATE TABLE IF NOT EXISTS expense_split  (

                               id UUID PRIMARY KEY,

                               expense_id UUID NOT NULL,

                               user_id UUID NOT NULL,

                               amount NUMERIC(12,2) NOT NULL,

                               CONSTRAINT fk_split_expense
                                   FOREIGN KEY(expense_id)
                                       REFERENCES expense(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_split_user
                                   FOREIGN KEY(user_id)
                                       REFERENCES users(id)
);


CREATE TABLE IF NOT EXISTS balance  (

                         id UUID PRIMARY KEY,

                         group_id UUID NOT NULL,

                         creditor_id UUID NOT NULL,

                         debtor_id UUID NOT NULL,

                         amount NUMERIC(12,2) NOT NULL,

                         CONSTRAINT fk_balance_group
                             FOREIGN KEY(group_id)
                                 REFERENCES expense_group(id)
                                 ON DELETE CASCADE,

                         CONSTRAINT fk_creditor
                             FOREIGN KEY(creditor_id)
                                 REFERENCES users(id),

                         CONSTRAINT fk_debtor
                             FOREIGN KEY(debtor_id)
                                 REFERENCES users(id),

                         CONSTRAINT uk_balance
                             UNIQUE(group_id, creditor_id, debtor_id)
);