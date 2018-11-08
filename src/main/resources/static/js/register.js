"use strict";

document.addEventListener("DOMContentLoaded", () => {
    var select = document.querySelectorAll('select');
    var selects = M.FormSelect.init(select);

    var date = document.querySelectorAll('.datepicker');
    var dates = M.Datepicker.init(date, {
        format: "mm/dd/yyyy"
    });

    const birthday = document.getElementById("birthdayString");
    birthday.addEventListener("focus", (e) => {
        let bInstance = M.Datepicker.getInstance(e.target);
        bInstance.open();
    });

    // const submitBtn = document.getElementById("submitBtn");
    // submitBtn.addEventListener("click", (e) => {
    //     e.preventDefault();
    //
    //     const pass = document.getElementById("password");
    //     const passV = document.getElementById("passwordVerify");
    //
    //     if (pass.value !== passV.value) {
    //         const vTooltip = new Tooltip(passV, {
    //             placement: "right",
    //             title: "Passwords don't match!",
    //             trigger: "manual"
    //         });
    //         vTooltip.show();
    //         setTimeout(vTooltip.hide, 5000);
    //     } else {
    //         const form = document.getElementById("registerForm");
    //         form.submit();
    //     }
    // });
});
