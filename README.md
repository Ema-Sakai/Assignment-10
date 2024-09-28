# ねこカフェ予約管理システムについて

![ねこカフェ予約サイト](https://github.com/user-attachments/assets/b206680f-2e73-4ac5-8124-344b3b1aa154)
<br />
<br />

- ## 作成背景（Background）

RaiseTechの最終課題内容が「CRUD機能をもつREST APIの作成、及びREST
APIに対するJUnitを用いたテストコードの作成」となっているため、大好きなにゃんこを顧客の使用背景に織り交ぜたREST
APIを作成しました。<br />

ちなみにGitHubのアイコン及びXのアイコンは、最強に可愛い私の愛猫です。[![badge](https://img.shields.io/badge/%40beginning0401（私のXアカウントに飛びます）-%23000000?style=social&logo=x)](https://x.com/beginning0401)
<br />
<br />

- ## サービス概要 (Service Overview)

このプロジェクトは、とあるねこカフェを利用したい方向けの予約サービスです。</br>
CRUD機能をもつがログイン機能は持たないシンプルながらも使いやすいことを目指した、先ずは小規模層の利用へ向けた実装内容となっています。
<br />
<br />

- ## 開発環境一覧（Development Environment）

### 使用技術（Technologies Used）

![badge](https://img.shields.io/badge/language-Java_17-%23007396)
![badge](https://img.shields.io/badge/springboot-3.2.6-%236DB33F?logo=spring)
![badge](https://img.shields.io/badge/MySQL-%234479A1?logo=mysql&logoColor=white)
![badge](https://img.shields.io/badge/MyBatis-%23DC382D?logoColor=white)
![badge](https://img.shields.io/badge/Junit5-%2325A162?logo=junit5&logoColor=white)
![badge](https://img.shields.io/badge/JSON-%23000000?logo=json&logoColor=white)
<br />
<br />

### 使用ツール（Tools Used）

![badge](https://img.shields.io/badge/Postman-%23FF6C37?logo=postman&logoColor=white)
![badge](https://img.shields.io/badge/Docker-%232496ED?logo=docker&logoColor=white)
![badge](https://img.shields.io/badge/Sonar_Cloud-%23F3702A?logo=sonarcloud&logoColor=white)
![badge](https://img.shields.io/badge/IntelliJ_IDEA-%23000000?logo=intellijidea&logoColor=white)
![badge](https://img.shields.io/badge/GitHub-%23181717?logo=github&logoColor=white)
![badge](https://img.shields.io/badge/GitHub_Actions-%232088FF?logo=githubactions&logoColor=white)
![badge](https://img.shields.io/badge/Canva-%2300C4CC?logo=canva&logoColor=white)
![badge](https://img.shields.io/badge/Swagger-%2385EA2D?logo=swagger&logoColor=white)
![badge](https://img.shields.io/badge/Shields.io-%23000000?logo=shieldsdotio&logoColor=white)
<br />
<br />

- ## 機能一覧 (Feature List)

- **予約情報の作成**：翌日以降の予約可能時間内で、新しい予約を作成します。
- **予約情報の取得**：予約番号を使用して、予約情報を取得します。
- **予約情報の更新**：予約番号を使用して、予約日時を更新します。
- **予約情報の削除**：予約番号を使用して、予約情報を削除します。
  <br />
  <br />

- ## ER図（Entity Relationship Diagram）

今回の設計は、1つの予約情報（`RESERVATIONS`）に対して1つの予約番号（`RESERVATIONS_NUMBERS`）が対応するという、1対1の関係となっています。

```mermaid
erDiagram
    RESERVATIONS {
        INT id PK
        VARCHAR name
        DATE reservation_date
        TIME reservation_time
        VARCHAR email
        VARCHAR phone
    }
    RESERVATIONS_NUMBERS {
        VARCHAR reservation_number PK
        INT reservation_id FK
    }
    RESERVATIONS ||--|| RESERVATIONS_NUMBERS: "has"
```

<br />
<br />

- ## シーケンス図（Sequence Diagram）

```mermaid
sequenceDiagram
    actor User
    participant API as Spring Boot API
    participant DB as Database

    rect rgb(200, 220, 240)
        Note right of User: 予約情報の作成フロー
        User ->> API: POST /reservations (予約情報)
        API ->> API: 入力データ検証
        API ->> DB: INSERT予約データ
        DB -->> API: 予約IDを返す
        API ->> API: 予約番号生成
        API ->> DB: INSERT予約番号
        API -->> User: 201 Created (作成された予約情報詳細が返る)
    end

    rect rgb(220, 240, 200)
        Note right of User: 予約情報の取得フロー
        User ->> API: GET /reservations/{reservationNumber}
        API ->> DB: SELECT予約データ
        alt 予約番号が存在する場合
            DB -->> API: 予約データ
            API -->> User: 200 OK (更新された予約情報詳細が返る)
        else 予約番号が存在しない場合
            DB -->> API: データなし
            API -->> User: 404 Not Found
        end
    end

    rect rgb(240, 220, 200)
        Note right of User: 予約情報の更新フロー
        User ->> API: PUT /reservations/{reservationNumber}
        API ->> DB: SELECT現在の予約データ
        alt 予約番号が存在する場合
            DB -->> API: 現在の予約データ
            API ->> API: 更新データ検証
            API ->> DB: UPDATE予約データ
            DB -->> API: 更新確認
            API -->> User: 200 OK (更新された予約情報詳細が返る)
        else 予約番号が存在しない場合
            DB -->> API: データなし
            API -->> User: 404 Not Found
        end
    end

    rect rgb(240, 200, 220)
        Note right of User: 予約情報の削除フロー
        User ->> API: DELETE /reservations/{reservationNumber}
        API ->> DB: SELECT予約データ
        alt 予約番号が存在する場合
            DB -->> API: 予約データ
            API ->> DB: DELETE予約データ
            DB -->> API: 削除確認
            API -->> User: 200 OK (予約情報の削除完了報告が返る)
        else 予約番号が存在しない場合
            DB -->> API: データなし
            API -->> User: 404 Not Found
        end
    end
```

<br />
<br />

- ## API仕様書（API Specification）

[SwaggerによるAPI仕様書（Ver1.0.0）](https://ema-sakai.github.io/Assignment-10/)

<br />
<br />

- ## テスト結果一覧（Test Results）

ここにGitHubActionsからとってきたバッジも、テストごとに載せたい。
それからテスト結果もなんとか載せられないか？
<br />
<br />

- ## 力を入れたところ（Key Focus Areas）

バリデーション頑張ったところ書きたい。
インターフェースで定義されたグループ順序に従って、バリデーションの実行順序を制御したところとか。
あとはテストケース
<br />
<br />

- ## 今後の課題（Future Improvements）

- デプロイ対応。
- ねこカフェ運営側の予約状況を確認する画面の実装。
- 予約作成完了時にメールアドレスへの予約情報転送機能の実装。
- アカウント作成・ログイン機能の実装。
  <br />
  <br />
