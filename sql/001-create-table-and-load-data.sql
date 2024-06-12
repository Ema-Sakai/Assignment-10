DROP TABLE IF EXISTS cats;

CREATE TABLE cats (
 id int unsigned AUTO_INCREMENT,
 breed VARCHAR(50) NOT NULL,
 coats VARCHAR(50) NOT NULL,
 PRIMARY KEY(id)
);

INSERT INTO cats (breed, coats) VALUES
("American Shorthair", "Short"),
("Maine Coon", "Long"),
("Scottish Fold", "Short"),
("Persian", "Long"),
("Siamese", "Short"),
("Ragdoll", "Long"),
("Russian Blue", "Short"),
("Norwegian Forest Cat", "Long"),
("Calico", "Short/Medium"),
("British Shorthair", "Short");
