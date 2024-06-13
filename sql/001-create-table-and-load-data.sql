DROP TABLE IF EXISTS reservations;

CREATE TABLE reservations (
 id INT UNSIGNED AUTO_INCREMENT,
 name VARCHAR(50) NOT NULL, -- 必要なのは連絡手段・情報なので、名前はニックネームでも何でもよい。
 reservation_date DATE NOT NULL,
 reservation_time TIME NOT NULL,
 email VARCHAR(100) NOT NULL,
 phone VARCHAR(11) NOT NULL,
 PRIMARY KEY(id),
 CHECK (phone REGEXP '^[0-9]{11}$') -- スマホで予約を想定し、この形でなければ登録できないようにした。
);

INSERT INTO reservations (name, reservation_date, reservation_time, email, phone) VALUES
("名前はにゃんでも登録できちゃうにゃん太郎", "2024-06-13", "0:30", "test@example.com", "02022222222");
