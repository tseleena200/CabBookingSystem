// ✅ Loading Screen Functionality
document.addEventListener("DOMContentLoaded", function () {
    setTimeout(() => {
        document.getElementById("loading-screen").classList.add("fade-out");

        // Redirect after fade out
        setTimeout(() => {
            window.location.href = "auth.html";
        }, 500);
    }, 3000);

    document.getElementById("loginForm").addEventListener("submit", function(event) {
        event.preventDefault();

        let email = document.getElementById("email").value;
        let password = document.getElementById("password").value;

        if (email === "admin@bookcab.com" && password === "123456") {
            alert("Login successful! Redirecting...");
            window.location.href = "dashboard.html";
        } else {
            alert("Invalid credentials. Please try again.");
        }
    });

});



