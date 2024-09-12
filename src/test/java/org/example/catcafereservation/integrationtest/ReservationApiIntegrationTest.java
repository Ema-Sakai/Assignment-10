package org.example.catcafereservation.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Nested
    class CreateTests {

        @Test
        @DataSet(value = "datasets/reservations.yml")
        @ExpectedDataSet(value = "datasets/expected_reservations_after_create.yml", ignoreCols = {"id", "reservation_id", "reservation_number"})
        @Transactional
        void 新しい予約情報を作成できて予約情報がレスポンスされること() throws Exception {
            // Arrange
            String newReservationJson = """
                    {
                        "name": "新しい予約のにゃん太郎",
                        "reservationDate": "2024-10-10",
                        "reservationTime": "12:00",
                        "email": "new@example.com",
                        "phone": "09012345678"
                    }
                    """;

            // Act & Assert
            mockMvc.perform(post("/reservations/")
                            .contentType("application/json")
                            .content(newReservationJson))
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            content().json("""
                                    {
                                        "message": "以下の通り予約が完了しました。",
                                        "reservationDate": "2024年10月10日",
                                        "reservationTime": "12時00分",
                                        "name": "新しい予約のにゃん太郎",
                                        "email": "new@example.com",
                                        "phone": "09012345678"
                                    }
                                    """)
                    )
                    .andExpect(result -> {
                        String content = result.getResponse().getContentAsString();
                        JSONObject jsonObject = new JSONObject(content);
                        String reservationNumber = jsonObject.getString("reservationNumber");

                        assertAll(
                                () -> assertThat(reservationNumber).isNotNull(),
                                () -> assertThat(reservationNumber).hasSize(26),
                                () -> assertThat(reservationNumber).matches("[A-Z0-9]{26}")
                        );
                    });
        }

        @Test
        @Transactional
        void バリデーションに引っかかる内容でリクエストするとBadRequestと入力規則が返ること() throws Exception {
            // Arrange
            String invalidReservationJson = """
                    {
                        "name": "00000000000000000000000000000000000000000_50文字以上の名前",
                        "reservationDate": "2024-09-09",
                        "reservationTime": "10:00",
                        "email": "invalid-email",
                        "phone": "123"
                    }
                    """;

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.post("/reservations/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidReservationJson))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("入力内容に誤りがあります。"),
                            jsonPath("$.errors['insert.reservationRequest.name']").value("予約名は50文字以内で入力してください。"),
                            jsonPath("$.errors['insert.reservationRequest.reservationDate']").value("明日以降のご希望日を選択してください。"),
                            jsonPath("$.errors['insert.reservationRequest.reservationTime']").value("予約時間は11:00から14:00までの間で、30分単位で選択してください。（例: 11:00、11:30）"),
                            jsonPath("$.errors['insert.reservationRequest.email']").value("有効なメールアドレスを入力してください。"),
                            jsonPath("$.errors['insert.reservationRequest.phone']").value("電話番号を数字のみ11桁で入力してください。（例：09012345678）")
                    );
        }

        @Test
        @Transactional
        void 空文字でリクエストするとBadRequestとエラーメッセージが返ること() throws Exception {
            // Arrange
            String invalidReservationJson = """
                    {
                        "name": "",
                        "reservationDate": "",
                        "reservationTime": "",
                        "email": "",
                        "phone": ""
                    }
                    """;

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.post("/reservations/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidReservationJson))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("入力内容に誤りがあります。"),
                            jsonPath("$.errors['insert.reservationRequest.name']").value("予約名の入力は必須です。"),
                            jsonPath("$.errors['insert.reservationRequest.reservationDate']").value("ご希望の予約日選択は必須です。"),
                            jsonPath("$.errors['insert.reservationRequest.reservationTime']").value("ご希望の予約時間を選択してください。"),
                            jsonPath("$.errors['insert.reservationRequest.email']").value("メールアドレスの入力は必須です。"),
                            jsonPath("$.errors['insert.reservationRequest.phone']").value("電話番号の入力は必須です。")
                    );
        }
    }
}
