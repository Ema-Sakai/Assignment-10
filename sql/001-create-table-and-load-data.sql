DROP TABLE IF EXISTS reservations;

CREATE TABLE reservations (
 id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(50) NOT NULL, -- 必要なのは連絡手段・情報なので、名前はニックネームでも何でもよい。
 reservation_date DATE NOT NULL,
 reservation_time TIME NOT NULL,
 CHECK (
     reservation_time >= '11:00:00' AND
     reservation_time <= '14:00:00' AND
     MINUTE(reservation_time) IN (0, 30)
   ),
 email VARCHAR(100) NOT NULL,
 phone VARCHAR(11) NOT NULL,
 CHECK (phone REGEXP '^[0-9]{11}$') -- スマホで予約を想定し、この形でなければ登録できないようにした。
);

INSERT INTO reservations (name, reservation_date, reservation_time, email, phone) VALUES
("名前はにゃんでも登録できちゃうにゃん太郎", "2024-06-13", "0:30", "test@example.com", "02022222222");
