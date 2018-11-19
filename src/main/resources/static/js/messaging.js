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
            const readDiv = document.getElementById("read");
            if (readDiv !== null && readDiv.childElementCount === 0) {
                readDiv.innerHTML = "<h3>No read messages</h3>";
            }
            const unreadDiv = document.getElementById("unread");
            if (unreadDiv !== null && unreadDiv.childElementCount === 0) {
                unreadDiv.innerHTML = "<h3>No unread messages</h3>";
            }
            const sentDiv = document.getElementById("sent");
            if (sentDiv !== null && sentDiv.childElementCount === 0) {
                sentDiv.innerHTML = "<h3>No sent messages</h3>";
            }
        })
        .catch(error => console.error(error));
    const modals = document.getElementById('messageModel');
    M.Modal.init(modals, {
        endingTop: "15%",
    });
    let messageTab = document.getElementById("messageTab");
    if (messageTab != undefined)
        M.Tabs.init(messageTab);
    const tooltippedElems = document.querySelectorAll('.tooltipped');
    const tooltippedInstances = M.Tooltip.init(tooltippedElems);
    const floatingElems = document.querySelectorAll('.fixed-action-btn');
    const floatingInstance = M.FloatingActionButton.init(floatingElems);
    const sendMessageBtn = document.getElementById("sendMessageBtn");
    sendMessageBtn.addEventListener("click", sendMessageBtn);
});

function replyClick(elem) {
    const modalTitle = document.getElementById("modalTitle");
    const modalBody = document.getElementById("modalBody");
    const modalId = document.getElementById("to");
    modalTitle.innerText = `Replying to: ${elem.dataset.title}`;
    modalBody.innerText = elem.dataset.body;
    modalId.value = elem.dataset.id;
    const modal = M.Modal.getInstance(document.getElementById("messageModel"));
    modal.open();
    const toastElem = elem.parentElement;
    dismissToast(toastElem);
    // let toastInstance = M.Toast.getInstance(toastElem);
    // toastInstance.dismiss();
}

function createNewMessage(elem) {
    const modalTitle = document.getElementById("modalTitle");
    const modalBody = document.getElementById("modalBody");
    const modalId = document.getElementById("to");
    modalTitle.innerText = `New message to: ${elem.dataset.title}`;
    modalBody.classList.add("hide");
    modalId.value = elem.dataset.id;
    const modal = M.Modal.getInstance(document.getElementById("messageModel"));
    modal.open();
}

function dismissToast(elem) {
    let toastInstance = M.Toast.getInstance(elem);
    toastInstance.dismiss();
}

function sendMessage(elem) {
    console.log(elem);
    const message = document.getElementById("message");
    if (message.value.length > 0) {
        const form = document.getElementById("messageForm");
        form.submit();
    }
}

function processMessage(json) {
    let message = json.message;
    if (message.length > 50) {
        message = message.slice(0, 49);
    }
    let toastHtml = `
        <span>${json.from}: ${message}</span>
        <button class="btn-flat toast-action" data-title="${json.from}" data-body="${json.message}" data-id="${json.fromId}" onclick="replyClick(this)">Reply</button>
        <a class="btn-flat toast-action" href="/messages/${json.id}" >View</a>
    `;
    if (json.from != undefined) {
        M.toast({
            html: toastHtml,
            classes: "message-toast",
            outDuration: 3000
        });
    }

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

function markAllRead() {
    const form = document.getElementById("markAllReadForm");
    form.submit();
}