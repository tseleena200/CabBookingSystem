document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ DOM fully loaded!");

    // ✅ Prevent unnecessary redirect to auth.html
    let loadingScreen = document.getElementById("loading-screen");

    if (loadingScreen) {
        // ✅ Only redirect if the user is NOT logged in
        let userSession = sessionStorage.getItem("userLoggedIn");
        if (!userSession) {
            setTimeout(() => {
                loadingScreen.classList.add("fade-out");

                // ✅ Redirect ONLY IF NOT LOGGED IN
                setTimeout(() => {
                    window.location.href = "auth.html";
                }, 500);
            }, 3000);
        }
    }

    // ✅ LOGIN FORM HANDLING
    let loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault(); // ✅ Prevent default form submission

            let email = document.getElementById("email").value.trim();
            let password = document.getElementById("password").value.trim();

            console.log("🔍 Attempting login for:", email);

            // ✅ Send login request to servlet
            fetch("http://localhost:8080/CabBookingSystem_war_exploded/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
            })
                .then(response => response.text())
                .then(data => {
                    console.log("🔍 Server Response:", data);

                    if (data.trim() === "success") {
                        alert("✅ Login successful! Redirecting...");

                        // ✅ Store session info in sessionStorage
                        sessionStorage.setItem("userLoggedIn", "true");

                        // ✅ Redirect to homepage.jsp
                        window.location.href = "http://localhost:8080/CabBookingSystem_war_exploded/jsp/homepage.jsp";

                    } else {
                        alert("❌ Invalid credentials. Please try again.");
                    }
                })
                .catch(error => {
                    console.error("🚨 Login request failed:", error);
                    alert("⚠️ Server error. Please try again later.");
                });
        });
    } else {
        console.warn("⚠️ No login form found on this page.");
    }
});
