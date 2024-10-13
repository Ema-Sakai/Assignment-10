const API_BASE_URL = 'http://localhost:8080';

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
        document.getElementById('result').innerHTML = `
            <h3>予約完了</h3>
            <p>予約番号: ${data.reservationNumber}</p>
            <p>日付: ${data.reservationDate}</p>
            <p>時間: ${data.reservationTime}</p>
        `;
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
        document.getElementById('result').innerHTML = `
            <h3>予約情報</h3>
            <p>お名前: ${data.name}</p>
            <p>日付: ${data.reservationDate}</p>
            <p>時間: ${data.reservationTime}</p>
            <p>メールアドレス: ${data.email}</p>
            <p>電話番号: ${data.phone}</p>
            <button onclick="deleteReservation('${reservationNumber}')">予約をキャンセル</button>
        `;
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
        document.getElementById('result').innerHTML = `
            <h3>予約更新完了</h3>
            <p>予約番号: ${data.reservationNumber}</p>
            <p>日付: ${data.reservationDate}</p>
            <p>時間: ${data.reservationTime}</p>
        `;
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('result').innerHTML = '<p>予約の更新に失敗しました。もう一度お試しください。</p>';
    }
});

document.getElementById('deleteReservationForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const reservationNumber = document.getElementById('deleteReservationNumber').value;

    try {
        const response = await fetch(`${API_BASE_URL}/reservations/${reservationNumber}`, {
            method: 'DELETE',
        });
        const data = await response.json();
        document.getElementById('result').innerHTML = `<p>${data.message}</p>`;
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('result').innerHTML = '<p>予約のキャンセルに失敗しました。もう一度お試しください。</p>';
    }
});

async function deleteReservation(reservationNumber) {
    if (confirm('本当に予約をキャンセルしますか？')) {
        try {
            const response = await fetch(`${API_BASE_URL}/reservations/${reservationNumber}`, {
                method: 'DELETE',
            });
            const data = await response.json();
            document.getElementById('result').innerHTML = `<p>${data.message}</p>`;
        } catch (error) {
            console.error('Error:', error);
            document.getElementById('result').innerHTML = '<p>予約のキャンセルに失敗しました。もう一度お試しください。</p>';
        }
    }
}
