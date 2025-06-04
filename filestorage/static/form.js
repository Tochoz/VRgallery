const apkFileDT = new DataTransfer();
const previewDT = new DataTransfer();
const mediasDT = new DataTransfer();
const order = document.querySelector("#mediasOrder")

document.querySelectorAll('.input-file input[type=file]').forEach(
function(input) {
    let currentDt = getDtByName(input.id)
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
                fileActions.innerHTML += '<a href="#" onclick="moveFileUp(this, \'' + this.files[i].name + '\', \'' + input.id + '\'); return false;" class="input-file-list-move-up">&#8593;</a>' +
                    '<a href="#" onclick="moveFileDown(this, \'' + this.files[i].name + '\', \'' + input.id + '\'); return false;" class="input-file-list-move-down">&#8595;</a>';
            }

            fileActions.innerHTML += '<a href="#" onclick="removeFilesItem(this, \'' + this.files[i].name + '\', \'' + input.id + '\'); return false;" class="input-file-list-remove">x</a>' +
                '</div>';

            fileInfo.appendChild(fileActions);
            newFileInput.appendChild(fileInfo);
            filesList.appendChild(newFileInput);
            currentDt.items.add(this.files[i]);
        }
        this.files = currentDt.files;
        order.value = Array.from(this.files).map(file => file.name).join(",")
        console.log(order.value)
    });
});

function removeFilesItem(target, fileName, id) {
    let input = target.closest('.input-file-row').querySelector('input[type=file]');
    let currentDt = getDtByName(input.id)
    target.closest('.input-file-list-item').remove();
    for (let i = 0; i < currentDt.items.length; i++) {
        if (fileName === currentDt.items[i].getAsFile().name) {
            currentDt.items.remove(i);
            break;
        }
    }
    input.files = currentDt.files;
    order.value = Array.from(input.files).map(file => file.name).join(",")
    console.log(order.value)
}

function getDtByName(id){
    switch (id) {
        case 'apkFile':
            return apkFileDT;
        case 'preview':
            return previewDT;
        case 'medias':
            return mediasDT;
    }
    return mediasDT
}

function moveFileUp(target, fileName, id) {
    let fileItem = target.closest('.input-file-list-item');
    let prevItem = fileItem.previousElementSibling;
    if (prevItem) {
        fileItem.parentNode.insertBefore(fileItem, prevItem);
        swapFilesInDataTransfer(fileName, -1, getDtByName(id));
    }
}

function moveFileDown(target, fileName, id) {
    let fileItem = target.closest('.input-file-list-item');
    let nextItem = fileItem.nextElementSibling;
    if (nextItem) {
        fileItem.parentNode.insertBefore(nextItem, fileItem);
        swapFilesInDataTransfer(fileName, 1, getDtByName(id));
    }
}

function swapFilesInDataTransfer(fileName, direction, dt) {
    let files = Array.from(dt.files);
    let index = files.findIndex(file => file.name === fileName);
    if (index !== -1) {
        [files[index], files[index + direction]] = [files[index + direction], files[index]];
        dt.items.clear();
        files.forEach(file => dt.items.add(file));
    }
    order.value = files.map(file => file.name).join(",")
    console.log(order.value)
}