/*
DROP TABLE IF EXISTS USR CASCADE;
DROP TABLE IF EXISTS ROLE_R CASCADE;
DROP TABLE IF EXISTS ASSIGNED_ROLE;
DROP TABLE IF EXISTS PROBLEM CASCADE;
DROP TABLE IF EXISTS PROBLEM_STATUS;
DROP TABLE IF EXISTS PROBLEM_MESSAGE;
DROP TABLE IF EXISTS SUITABILITY;
DROP TABLE IF EXISTS DESIRABLE_SCHEDULE CASCADE;
DROP TABLE IF EXISTS LEVEL;
DROP TABLE IF EXISTS COURSE_STATUS;
DROP TABLE IF EXISTS COURSE CASCADE;
DROP TABLE IF EXISTS USR_GROUP CASCADE;
DROP TABLE IF EXISTS GRUP CASCADE;
DROP TABLE IF EXISTS ATTACHMENT;
DROP TABLE IF EXISTS LESSON_ATTACHMENT CASCADE;
DROP TABLE IF EXISTS LESSON CASCADE;
DROP TABLE IF EXISTS NOTIFICATION CASCADE;
DROP TABLE IF EXISTS EVENT;
DROP TABLE IF EXISTS FEEDBACK CASCADE;
DROP TABLE IF EXISTS CHAT CASCADE;
DROP TABLE IF EXISTS CHAT_USER CASCADE;
*/

CREATE TABLE USR
(
    ID                 SERIAL PRIMARY KEY,
    EMAIL              VARCHAR(50) NOT NULL UNIQUE,
    PASSWORD           VARCHAR(50) NOT NULL,
    FIRST_NAME         VARCHAR(50) NOT NULL,
    LAST_NAME          VARCHAR(50),
    TOKEN              VARCHAR(256),
    CREATED            TIMESTAMP with time zone,
    MANAGER_ID         INTEGER REFERENCES USR (ID),
    IMAGE_URL          VARCHAR(256),
    IS_ACTIVE          BOOLEAN,
    IS_ON_LANDING_PAGE BOOLEAN,
    DESCRIPTION        TEXT
);

CREATE TABLE ROLE_R
(
    ID   SERIAL PRIMARY KEY,
    NAME VARCHAR(15) NOT NULL
);

CREATE TABLE ASSIGNED_ROLE
(
    USER_ID INTEGER REFERENCES USR (ID),
    ROLE_ID INTEGER REFERENCES ROLE_R (ID)
);

CREATE TABLE PROBLEM_STATUS
(
    ID          SERIAL PRIMARY KEY,
    TITLE       VARCHAR(50) NOT NULL,
    DESCRIPTION TEXT
);

CREATE TABLE PROBLEM
(
    ID                SERIAL PRIMARY KEY,
    USER_ID           INTEGER REFERENCES USR (ID),
    TITLE             VARCHAR(50) NOT NULL,
    PROBLEM_STATUS_ID INTEGER REFERENCES PROBLEM_STATUS (ID),
    DESCRIPTION       TEXT
);

CREATE TABLE PROBLEM_MESSAGE
(
    ID         SERIAL PRIMARY KEY,
    PROBLEM_ID INTEGER REFERENCES PROBLEM (ID),
    USER_ID    INTEGER REFERENCES USR (ID),
    MESSAGE    TEXT NOT NULL,
    DATE_TIME  TIMESTAMP with time zone
);

CREATE TABLE COURSE
(
    ID                 SERIAL PRIMARY KEY,
    NAME               VARCHAR(150) NOT NULL,
    LEVEL              INTEGER REFERENCES LEVEL (ID),
    COURSE_STATUS_ID   INTEGER REFERENCES COURSE_STATUS (ID),
    USER_ID            INTEGER REFERENCES USR (ID),
    IMAGE_URL          VARCHAR(256),
    START_DATE         DATE,
    END_DATE           DATE,
    IS_ON_LANDING_PAGE BOOLEAN,
    DESCRIPTION        TEXT
);

CREATE TABLE COURSE_STATUS
(
    ID          SERIAL PRIMARY KEY,
    NAME        VARCHAR(50) NOT NULL,
    DESCRIPTION TEXT
);

CREATE TABLE LEVEL
(
    ID   SERIAL PRIMARY KEY,
    NAME VARCHAR(50) NOT NULL
);


CREATE TABLE SUITABILITY
(
    ID       SERIAL PRIMARY KEY,
    TITLE    VARCHAR(150) NOT NULL,
    PRIORITY INTEGER
);


CREATE TABLE DESIRABLE_SCHEDULE
(
    ID            SERIAL PRIMARY KEY,
    USER_ID       INTEGER REFERENCES USR (ID),
    COURSE_ID     INTEGER REFERENCES COURSE (ID),
    CRON_INTERVAL VARCHAR(50),
    SUITABILITY   INTEGER REFERENCES SUITABILITY (ID)
);

CREATE TABLE GRUP
(
    ID        SERIAL PRIMARY KEY,
    COURSE_ID INTEGER REFERENCES COURSE (ID),
    TITLE     VARCHAR(150) NOT NULL
);

CREATE TABLE USR_GROUP
(
    ID           SERIAL PRIMARY KEY,
    USER_ID      INTEGER REFERENCES USR (ID),
    GROUP_ID     INTEGER REFERENCES GRUP (ID),
    IS_ATTENDING BOOLEAN
);

CREATE TABLE ATTACHMENT
(
    ID          SERIAL PRIMARY KEY,
    URL         TEXT,
    DESCRIPTION TEXT
);
CREATE TABLE LESSON
(
    ID        SERIAL PRIMARY KEY,
    GROUP_ID  INTEGER REFERENCES GRUP (ID),
    TOPIC     VARCHAR(150) NOT NULL,
    USER_ID   INTEGER REFERENCES USR (ID),
    TIME_DATE TIMESTAMP WITH TIME ZONE
);

CREATE TABLE LESSON_ATTACHMENT
(
    LESSON_ID     INTEGER REFERENCES LESSON (ID),
    ATTACHMENT_ID INTEGER REFERENCES ATTACHMENT (ID)
);

CREATE TABLE EVENT
(
    ID                    SERIAL PRIMARY KEY,
    TITLE                 VARCHAR(150) NOT NULL,
    NOTIFICATION_TEMPLATE VARCHAR(100) NOT NULL
);

CREATE TABLE NOTIFICATION
(
    ID        SERIAL PRIMARY KEY,
    EVENT_ID  INTEGER REFERENCES EVENT (ID),
    USER_ID   INTEGER REFERENCES USR (ID),
    DATE_TIME TIMESTAMP WITH TIME ZONE NOT NULL,
    IS_READ   BOOLEAN,
    TEXT      VARCHAR(150)
);

CREATE TABLE FEEDBACK
(
    ID         SERIAL PRIMARY KEY,
    USER_ID    INTEGER REFERENCES USR (ID),
    TRAINER_ID INTEGER REFERENCES USR (ID),
    COURSE_ID  INTEGER REFERENCES COURSE (ID),
    TEXT       TEXT,
    DATE_TIME  TIMESTAMP WITH TIME ZONE
);


CREATE TABLE CHAT_MESSAGE
(
    ID        SERIAL PRIMARY KEY,
    CHAT_ID   INTEGER REFERENCES CHAT (ID),
    USER_ID   INTEGER REFERENCES USR (ID),
    TIME_DATE TIMESTAMP WITH TIME ZONE,
    TEXT      TEXT
);

CREATE TABLE CHAT
(
    ID        SERIAL PRIMARY KEY,
    NAME      VARCHAR(50) NOT NULL,
    TIME_DATE TIMESTAMP WITH TIME ZONE,
    GROUP_ID  INTEGER REFERENCES GRUP (ID)
);

CREATE TABLE CHAT_USER
(
    CHAT_ID INTEGER REFERENCES CHAT (ID),
    USER_ID INTEGER REFERENCES USR (ID)
);