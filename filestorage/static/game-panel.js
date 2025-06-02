function showDialog() {
    // Создаем диалоговое окно
    const dialog = document.createElement('div');
    dialog.classList.add('dialog');

    // Создаем контент диалогового окна
    const content = document.createElement('div');
    content.classList.add('dialog-content');

    const paragraph = document.createElement('p');
    paragraph.textContent = 'This is a dialog window.';

    const link = document.createElement('a');
    link.href = '#';
    link.textContent = 'Click me';

    content.appendChild(paragraph);
    content.appendChild(link);

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