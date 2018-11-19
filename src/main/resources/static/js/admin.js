"use strict";

document.addEventListener("DOMContentLoaded", () => {
    adminTabs();
    const cardContents = document.getElementsByClassName("card-content");
    for (let elem of cardContents) {
        if (!elem.classList.contains("setting-page")) {
            elem.classList.add("hoverPointer");
            elem.addEventListener("click", (elem) => {
                const link = elem.target.closest(".card-content").dataset.target;
                const cId = elem.target.closest(".card-content").dataset.comment;
                if (link !== "undefined" && link !== undefined) {
                    console.log(cId);
                    if (cId !== "undefined" && link !== undefined) {
                        window.location = `/posts/${link}#${cId}`
                    } else {
                        window.location = `/posts/${link}`;
                    }
                }
            });
        }
    }
});

function adminTabs() {
    const adminTab = document.getElementById("adminTab");
    const adminTabsInstance = M.Tabs.init(adminTab);
}