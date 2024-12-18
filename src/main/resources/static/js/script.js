const API_BASE_URL = 'http://localhost:8080';

// フィールド名のマッピング
const fieldNames = {
    'insert.reservationRequest.name': '予約氏名',
    'insert.reservationRequest.phone': '電話番号',
    'insert.reservationRequest.reservationDate': '予約日',
    'insert.reservationRequest.reservationTime': '予約時間',
    'insert.reservationRequest.email': 'メールアドレス',
    'getReservation.reservationNumber': '予約番号',
    'updateReservation.reservationNumber': '予約番号',
    'updateReservation.reservationDate': '予約日',
    'updateReservation.reservationTime': '予約時間',
    'deleteReservation.reservationNumber': '予約番号',
    'reservationDate': '予約日'
};

document.getElementById('reservationForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = {
        name: document.getElementById('name').value,
        reservationDate: document.getElementById('reservationDate').value,
        reservationTime: document.getElementById('reservationTime').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/reservations/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        });
        const data = await response.json();

        if (!response.ok) {
            let errorMessage = '入力内容に誤りがあります。';
            if (data.message) {
                errorMessage = data.message;
            }
            if (data.errors) {
                for (const [key, value] of Object.entries(data.errors)) {
                    const fieldName = fieldNames[key] || key;
                    errorMessage += `<br>${fieldName}: ${value}`;
                }
            }
            document.getElementById('result').innerHTML = `<p>${errorMessage}</p>`;
        } else {
            const nameWithSuffix = `${data.name}  様`;
            document.getElementById('result').innerHTML = `
                <h3>以下のとおり予約が完了しました。</h3>
                <p>予約番号: ${data.reservationNumber}</p>
                <p>お名前: ${nameWithSuffix}</p>
                <p>日付: ${data.reservationDate}</p>
                <p>時間: ${data.reservationTime}</p>
                <p>メールアドレス: ${data.email}</p>
                <p>電話番号: ${data.phone}</p>
                <h3>にゃんこスタッフ一同、ご来店を楽しみにしております！</h3>
                <h4>※予約番号は予約の確認・変更・キャンセルに必要となりますため、番号をひかえていただきますようお願いいたします。</h4>
            `;
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('result').innerHTML = '<p>予約に失敗しました。もう一度お試しください。</p>';
    }
});

document.getElementById('checkReservationForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const reservationNumber = document.getElementById('reservationNumber').value;

    try {
        const response = await fetch(`${API_BASE_URL}/reservations/${reservationNumber}`);
        const data = await response.json();

        if (!response.ok) {
            let errorMessage = '予約情報の取得に失敗しました。';
            if (data.message) {
                errorMessage = data.message;
            }
            if (data.errors) {
                for (const [key, value] of Object.entries(data.errors)) {
                    const fieldName = fieldNames[key] || key;
                    errorMessage += `<br>${fieldName}: ${value}`;
                }
            }
            document.getElementById('result').innerHTML = `<p>${errorMessage}</p>`;
        } else {
            const nameWithSuffix = `${data.name}  様`;
            document.getElementById('result').innerHTML = `
                <h3>予約情報</h3>
                <p>お名前: ${nameWithSuffix}</p>
                <p>日付: ${data.reservationDate}</p>
                <p>時間: ${data.reservationTime}</p>
                <p>メールアドレス: ${data.email}</p>
                <p>電話番号: ${data.phone}</p>
                <button onclick="location.href='/update-reservation.html'">予約日時を変更する</button>
                <button onclick="location.href='/delete-reservation.html'">予約をキャンセルする</button>
            `;
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('result').innerHTML = '<p>予約情報の取得に失敗しました。予約番号を確認してください。</p>';
    }
});

document.getElementById('updateReservationForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const reservationNumber = document.getElementById('updateReservationNumber').value;
    const formData = {
        reservationDate: document.getElementById('newReservationDate').value,
        reservationTime: document.getElementById('newReservationTime').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/reservations/${reservationNumber}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        });
        const data = await response.json();

        if (!response.ok) {
            let errorMessage = '予約の更新に失敗しました。';
            if (data.message) {
                errorMessage = data.message;
            }
            if (data.errors) {
                for (const [key, value] of Object.entries(data.errors)) {
                    const fieldName = fieldNames[key] || key;
                    errorMessage += `<br>${fieldName}: ${value}`;
                }
            }
            document.getElementById('result').innerHTML = `<p>${errorMessage}</p>`;
        } else {
            document.getElementById('result').innerHTML = `
                <h3>以下のとおり予約日時の更新が完了しました。</h3>
                <p>予約番号: ${data.reservationNumber}</p>
                <p>日付: ${data.reservationDate}</p>
                <p>時間: ${data.reservationTime}</p>
                <button onclick="location.href='/check-reservation.html'">予約日時を確認する</button>
                <button onclick="location.href='/delete-reservation.html'">予約をキャンセルする</button>
            `;
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('result').innerHTML = '<p>予約の更新に失敗しました。もう一度お試しください。</p>';
    }
});

document.getElementById('deleteReservationForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const reservationNumber = document.getElementById('deleteReservationNumber').value;

    if (confirm('本当にキャンセルしますか？')) {
        try {
            const response = await fetch(`${API_BASE_URL}/reservations/${reservationNumber}`, {
                method: 'DELETE',
            });
            const data = await response.json();

            if (!response.ok) {
                let errorMessage = '予約のキャンセルに失敗しました。';
                if (data.message) {
                    errorMessage = data.message;
                }
                if (data.errors) {
                    for (const [key, value] of Object.entries(data.errors)) {
                        const fieldName = fieldNames[key] || key;
                        errorMessage += `<br>${fieldName}: ${value}`;
                    }
                }
                document.getElementById('result').innerHTML = `<p>${errorMessage}</p>`;
            } else {
                document.getElementById('result').innerHTML = `<p>${data.message}</p>`;
            }
        } catch (error) {
            console.error('Error:', error);
            document.getElementById('result').innerHTML = '<p>予約のキャンセルに失敗しました。もう一度お試しください。</p>';
        }
    }
});

function setupFormValidation() {
    const form = document.getElementById('reservationForm');
    const nameInput = document.getElementById('name');
    const phoneInput = document.getElementById('phone');

    if (form) {  // フォームが存在する場合のみ実行
        form.addEventListener('submit', function(e) {
            let isValid = true;
            let errorMessage = '';

            // 名前のバリデーション
            if (nameInput.value.length > 50) {
                isValid = false;
                errorMessage += '名前は50文字以内で入力してください。\n';
            }

            // 電話番号のバリデーション
            if (!/^[0-9]{11}$/.test(phoneInput.value)) {
                isValid = false;
                errorMessage += '電話番号はハイフンなしの11桁で入力してください。\n';
            }

            if (!isValid) {
                e.preventDefault(); // フォームの送信を防止
                alert(errorMessage); // エラーメッセージを表示
            }
        });
    }
}

// DOMContentLoadedイベントで全ての初期化関数を呼び出す
document.addEventListener('DOMContentLoaded', function() {
    setupDateInputs();
    setupFormValidation();
});
