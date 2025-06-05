function openGame(id) {
    window.open("/games/"+id, '_blank').focus();
}

function like(gameId, event) {
    event.stopPropagation(); // Prevent the event from reaching the parent
    // alert("like clicked!");
    const card = document.querySelector(`card#game-${gameId}`);
    let endPoint = `/games/${gameId}/like`
    if (card.hasAttribute("liked"))
        endPoint = `/games/${gameId}/dislike`
    else
        endPoint = `/games/${gameId}/like`
    fetch(endPoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            // Update the number of likes on the page
            card.querySelector(".likesCount").innerHTML = data.likes;

            if (data.liked) {
                card.setAttribute("liked", "")
                card.querySelector(".likesPanel img").src = "/files/static/icons/like_filled.svg"
            }
            else {
                card.removeAttribute("liked")
                card.querySelector(".likesPanel img").src = "/files/static/icons/like.svg"
            }
        })
        .catch(error => {
            console.error('Error liking game:', error);
        });

}
function likeDetailed(gameId) {
    const panel = document.querySelector(`span.likesPanel`);
    let endPoint
    if (panel.hasAttribute("liked"))
        endPoint = `/games/${gameId}/dislike`
    else
        endPoint = `/games/${gameId}/like`
    fetch(endPoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            // Update the number of likes on the page
            panel.querySelector(".likesCount").innerHTML = data.likes;

            if (data.liked) {
                panel.setAttribute("liked", "")
                panel.querySelector(".likesPanel img").src = "/files/static/icons/like_filled.svg"
            }
            else {
                panel.removeAttribute("liked")
                panel.querySelector(".likesPanel img").src = "/files/static/icons/like.svg"
            }
        })
        .catch(error => {
            console.error('Error liking game:', error);
        });
}
function noneClick(event) {
    event.stopPropagation(); // Prevent the event from reaching the parent
}