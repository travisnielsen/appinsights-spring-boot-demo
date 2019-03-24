IF NOT EXISTS (SELECT 1 FROM sysobjects WHERE NAME='user' and XTYPE='U')
  CREATE TABLE "user" (
    id      INT           IDENTITY  PRIMARY KEY,
    firstName    VARCHAR(255),
    lastName VARCHAR(255),
    createdAt DATETIME,
    updatedAt DATETIME
  );