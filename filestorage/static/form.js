var dt = new DataTransfer();

document.querySelectorAll('.input-file input[type=file]').forEach(function(input) {
    input.addEventListener('change', function() {
        let filesList = this.closest('.input-file').nextElementSibling;
        filesList.innerHTML = '';

        let isMultiple = this.hasAttribute('multiple');

        for (var i = 0; i < this.files.length; i++) {
            let newFileInput = document.createElement('div');
            newFileInput.classList.add('input-file-list-item');

            let filePreview = document.createElement('div');
            filePreview.classList.add('input-file-list-preview');

            if (this.files[i].type.startsWith('image/')) {
                let img = document.createElement('img');
                img.src = URL.createObjectURL(this.files[i]);
                img.alt = this.files[i].name;
                filePreview.appendChild(img);
            } else {
                filePreview.textContent = this.files[i].name.split('.').pop().toUpperCase();
            }

            newFileInput.appendChild(filePreview);

            let fileInfo = document.createElement('div');
            fileInfo.classList.add('input-file-list-info');
            fileInfo.innerHTML = '<span class="input-file-list-name">' + this.files[i].name + '</span>';
            let fileActions = document.createElement('div');
            fileActions.classList.add('input-file-list-actions');


            if (isMultiple) {
                fileActions.innerHTML += '<a href="#" onclick="moveFileUp(this, \'' + this.files[i].name + '\'); return false;" class="input-file-list-move-up">&#8593;</a>' +
                    '<a href="#" onclick="moveFileDown(this, \'' + this.files[i].name + '\'); return false;" class="input-file-list-move-down">&#8595;</a>';
            }

            fileActions.innerHTML += '<a href="#" onclick="removeFilesItem(this, \'' + this.files[i].name + '\'); return false;" class="input-file-list-remove">x</a>' +
                '</div>';

            fileInfo.appendChild(fileActions);
            newFileInput.appendChild(fileInfo);
            filesList.appendChild(newFileInput);
            dt.items.add(this.files[i]);
        }
        this.files = dt.files;
    });
});

function removeFilesItem(target, fileName) {
    let input = target.closest('.input-file-row').querySelector('input[type=file]');
    target.closest('.input-file-list-item').remove();
    for (let i = 0; i < dt.items.length; i++) {
        if (fileName === dt.items[i].getAsFile().name) {
            dt.items.remove(i);
            break;
        }
    }
    input.files = dt.files;
}

function moveFileUp(target, fileName) {
    let fileItem = target.closest('.input-file-list-item');
    let prevItem = fileItem.previousElementSibling;
    if (prevItem) {
        fileItem.parentNode.insertBefore(fileItem, prevItem);
        swapFilesInDataTransfer(fileName, -1);
    }
}

function moveFileDown(target, fileName) {
    let fileItem = target.closest('.input-file-list-item');
    let nextItem = fileItem.nextElementSibling;
    if (nextItem) {
        fileItem.parentNode.insertBefore(nextItem, fileItem);
        swapFilesInDataTransfer(fileName, 1);
    }
}

function swapFilesInDataTransfer(fileName, direction) {
    let files = Array.from(dt.files);
    let index = files.findIndex(file => file.name === fileName);
    if (index !== -1) {
        [files[index], files[index + direction]] = [files[index + direction], files[index]];
        dt.items.clear();
        files.forEach(file => dt.items.add(file));
    }
}