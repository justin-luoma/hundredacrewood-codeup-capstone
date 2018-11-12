"use strict";

document.addEventListener("DOMContentLoaded", () => {
    let messagingInterval = setInterval(checkMessages, 5000);
    fetch("/messaging/count",{
        credentials: "include"
    })
        .then(resp => resp.ok ? resp.json() : new Error("failed to fetch unread count"))
        .then(count => {
            if (count > 0 ) {
                const counter = document.getElementsByClassName("message-badge");
                for (let elem of counter) {
                    elem.innerText = count;
                    elem.classList.remove("hide")
                }
            }
        })
        .catch(error => console.error(error));
});

function processMessage(json) {
    let message = json.message;
    if (message.length > 50) {
        message = message.slice(0, 49);
    }
    let toastHtml = `
        <span>${json.from}: ${message}</span>
        <button class="btn-flat toast-action">Reply</button>
        <button class="btn-flat toast-action">View</button>
    `;
    M.toast({
        html: toastHtml,
        classes: "message-toast",
        outDuration: 3000
    });
}

function checkMessages() {
    fetch("/messaging", {
        credentials: "include"
    }).then(resp => resp.ok ? resp.json() : new Error("failed to fetch messages"))
        .then(json => {
            if (!json.none) {
                processMessage(json);
            }
        })
    .catch(error => console.error(error));
}