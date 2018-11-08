"use strict";

document.addEventListener('DOMContentLoaded', function() {
	let sidenav = document.querySelectorAll('.sidenav');
	let sidenavs = M.Sidenav.init(sidenav);

	let logoutButtonSide = document.getElementById('logoutButtonMobile');
	logoutButtonSide.addEventListener('click', logout);

	let logoutButton = document.getElementById('logoutButton');
	logoutButton.addEventListener('click', logout);
});

function logout(event) {
	event.preventDefault();
	document.getElementById('logoutForm').submit();

}
