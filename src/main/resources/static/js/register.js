"use strict";

document.addEventListener("DOMContentLoaded", () => {
    var select = document.querySelectorAll('select');
    var selects = M.FormSelect.init(select);

    var date = document.querySelectorAll('.datepicker');
    var dates = M.Datepicker.init(date, {
        format: "mm/dd/yyyy",
        defaultDate: new Date('1991-05-04'),
        yearRange: [1950, 2010]
    });

    const birthday = document.getElementById("birthdayString");
    birthday.addEventListener("focus", (e) => {
        let bInstance = M.Datepicker.getInstance(e.target);
        bInstance.open();
    });

    const texts = document.getElementById("texts");
    const phoneInput = document.getElementById("phone");
    const phoneLabel = document.getElementById("phoneLabel");

    texts.addEventListener("change", function () {
        if (this.checked) {
            phoneLabel.innerText = "Phone*";
            phoneInput.required = true;
        } else {
            phoneLabel.innerText = "Phone";
            phoneInput.required = false;
        }
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
