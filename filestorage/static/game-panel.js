function showDialog() {
    // Создаем диалоговое окно
    const dialog = document.createElement('div');
    dialog.classList.add('dialog');

    // Создаем контент диалогового окна
    const content = document.createElement('div');
    content.classList.add('dialog-content');

    content.innerHTML = `
    <p>Для того, чтобы поиграть в игру на своём шлеме Oculus Quest, скачайте <a href="https://sidequestvr.com/download" target="_blank">десктопное приложение SideQuest</a> на свой компьютер.</p>
    <p>Подключите гарнитуру к компьютеру и установите скачанный .apk файл игры выбрав соответствующий раздел в программе.<br>Готово! Вы можете запустить игру на вашем шлеме!</p>
    `

    // Создаем кнопку закрытия
    const closeButton = document.createElement('button');
    closeButton.classList.add('close-button');
    closeButton.textContent = 'X';
    closeButton.addEventListener('click', () => {
        dialog.remove();
    });

    content.appendChild(closeButton);
    dialog.appendChild(content);

    // Добавляем диалоговое окно на страницу
    document.body.appendChild(dialog);

    // Центрируем диалоговое окно
    const dialogRect = dialog.getBoundingClientRect();
    dialog.style.top = `${window.innerHeight / 2 - dialogRect.height / 2}px`;
    dialog.style.left = `${window.innerWidth / 2 - dialogRect.width / 2}px`;
}