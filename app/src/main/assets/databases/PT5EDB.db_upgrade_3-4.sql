-- version 4 adds stuff for party tracking!

CREATE TABLE `parties` (
	`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`NAME`	TEXT
);

CREATE TABLE `playercharacters` (
	`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`CHARACTERNAME`	TEXT,
	`PLAYERNAME`	TEXT,
	`XPCURRENT`	INTEGER,
	`XPTONEXTLEVEL`	INTEGER,
	`LEVEL`	INTEGER
);