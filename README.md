# にゃんこカフェ予約管理システムについて
![ねこカフェ予約サイト](https://github.com/user-attachments/assets/b206680f-2e73-4ac5-8124-344b3b1aa154)

## 目的（Purpose）

にゃんこカフェを利用したい方の、予約サイトを作成したい。  
（にゃんこカフェ運営側の予約履歴閲覧サイトではない）

## 要件（Requirements）

- 氏名（ニックネームでも可）、予約日時、連絡先（電話番号とメアド）を管理できること。
    - すべて入力必須としたい。
- 同じ人が連続で予約をとっても、別IDとしたい。

## 技術要件（Technical Requirements）

- Javaで開発をする。
- SpringBootを使用して開発する。

### 開発環境

![badge](https://img.shields.io/badge/language-Java_17-%23007396)
![badge](https://img.shields.io/badge/springboot-3.2.6-%236DB33F?logo=spring)
![badge](https://img.shields.io/badge/MySQL-%234479A1?logo=mysql&logoColor=white)
![badge](https://img.shields.io/badge/MyBatis-%23DC382D?logoColor=white)
![badge](https://img.shields.io/badge/Postman-%23FF6C37?logo=postman&logoColor=white)
![badge](https://img.shields.io/badge/Docker-%232496ED?logo=docker&logoColor=white)
![badge](https://img.shields.io/badge/Junit5-%2325A162?logo=junit5&logoColor=white)
![badge](https://img.shields.io/badge/JSON-%23000000?logo=json&logoColor=white)
![badge](https://img.shields.io/badge/Sonar_Cloud-%23F3702A?logo=sonarcloud&logoColor=white)
![badge](https://img.shields.io/badge/IntelliJ_IDEA-%23000000?logo=intellijidea&logoColor=white)
![badge](https://img.shields.io/badge/GitHub-%23181717?logo=github&logoColor=white)
![badge](https://img.shields.io/badge/GitHub_Actions-%232088FF?logo=githubactions&logoColor=white)
![badge](https://img.shields.io/badge/Canva-%2300C4CC?logo=canva&logoColor=white)
![badge](https://img.shields.io/badge/Swagger-%2385EA2D?logo=swagger&logoColor=white)
![badge](https://img.shields.io/badge/Shields.io-%23000000?logo=shieldsdotio&logoColor=white)
[![badge](https://img.shields.io/badge/%40beginning0401-%23000000?style=social&logo=x)](@beginning0401)
![badge]()



## 機能（Features）

- CRUD処理ができる
    - 登録機能について。
        - 予約時の項目をすべて埋めることで予約が可能となる。
    - 検索機能について。
        - 予約番号を入力して予約内容を検索し表示する。
        - ただし1件ずつの表示とする。
    - 更新機能について。
        - 予約番号を使って予約内容を1件変更する機能。
        - 変更後は予約内容に準じた新しい予約番号が発行され、古い情報は予約番号ごと破棄される。
    - 削除機能について。
        - 予約番号を使って予約情報を削除する機能。
- 情報をフロント側に連携できるように、画面にデータを返す。
- 予約番号の生成は下記のとおりとする。
    - 予約が完了した際に、特定のフォーマットで予約番号を生成する。
    - 具体的には「AA～ZZからなるランダムな2文字+予約受付完了時間の下4桁+自動採番したID」を予約番号とする。
- 予約時間は11：00～16：00が営業時間なので、14：00が最終選択可能時間。
    - 選択できる時間は30分単位で制限。
- 予約完了時、登録メールアドレスに必ず予約内容が転送される。
- 予約完了時、スマートフォンのカレンダーに任意で予約日時を登録できる。

## 仕様（Specifications）

### 予約時の項目定義

以下の項目はすべて必須項目とする。

- 氏名（ニックネーム可）
- 予約日
- 予約時間
- 電話番号
    - 桁は「000-0000-0000」のハイフンなしの形のみ登録可能（計11桁）。
- メールアドレス
    - キャリアは問わず登録可能。

### DB設計

- 予約情報テーブル（reservations）
- 予約完了時に作成された予約番号テーブル（reservations_numbers）
