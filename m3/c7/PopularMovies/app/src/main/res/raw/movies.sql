BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `movies` (
	`_id`	INTEGER,
	`title`	TEXT NOT NULL,
	`plotSynopsis`	TEXT NOT NULL,
	`releaseDate`	TEXT NOT NULL,
	`posterRemoteId`	TEXT NOT NULL,
	`userRating`	REAL NOT NULL,
	`popularity`	REAL NOT NULL,
	`isFavorite`	INTEGER NOT NULL DEFAULT 0,
	`isMostPopular`	INTEGER NOT NULL DEFAULT 0,
	`isTopRated`	INTEGER NOT NULL DEFAULT 0,
	`remoteId`	TEXT NOT NULL UNIQUE,
	PRIMARY KEY(`_id`)
);
CREATE TABLE IF NOT EXISTS `videos` (
	`_id`	INTEGER,
	`title`	TEXT NOT NULL,
	`url`	TEXT NOT NULL,
	`movieId`	INTEGER NOT NULL,
	PRIMARY KEY(`_id`),
	FOREIGN KEY(`movieId`) REFERENCES `movies`(`_id`)
);
CREATE TABLE IF NOT EXISTS `reviews` (
	`_id`	INTEGER,
	`author`	TEXT NOT NULL,
	`content`	TEXT NOT NULL,
	`movieId`	INTEGER NOT NULL,
	PRIMARY KEY(`_id`),
	FOREIGN KEY(`movieId`) REFERENCES `movies`(`_id`)
);
COMMIT;