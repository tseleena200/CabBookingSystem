document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ DOM fully loaded!");

    //  LOADING SCREEN HANDLING
    let loadingScreen = document.getElementById("loading-screen");

    if (loadingScreen) {
        setTimeout(() => {
            loadingScreen.classList.add("fade-out");
            setTimeout(() => {
                loadingScreen.style.display = "none";
            }, 500);
        }, 2000);
    }

    //  LOGIN FORM HANDLING
    let loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault(); // Prevent default form submission

            let email = document.getElementById("email").value.trim();
            let password = document.getElementById("password").value.trim();

            console.log("🔍 Attempting login for:", email);

            fetch("http://localhost:8080/CabBookingSystem_war_exploded/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                credentials: "include",  //  Ensure session cookies work
                body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
            })
                .then(response => response.text())
                .then(data => {
                    console.log("🔍 Server Response:", `"${data.trim()}"`);

                    if (data.trim() === "admin") {
                        alert("✅ Welcome, Admin!");
                        sessionStorage.setItem("userRole", "admin");
                        window.location.href = "http://localhost:8080/CabBookingSystem_war_exploded/admin/admin_panel.jsp";
                    } else if (data.trim() === "employee") {
                        alert("✅ Login successful!");
                        sessionStorage.setItem("userRole", "employee");
                        window.location.href = "http://localhost:8080/CabBookingSystem_war_exploded/jsp/homepage.jsp";
                    } else {
                        document.getElementById("errorMessage").textContent = "❌ Invalid email or password!";
                        document.getElementById("errorMessage").style.display = "block";
                    }
                })
                .catch(error => {
                    console.error("🚨 Login request failed:", error);
                    document.getElementById("errorMessage").textContent = "⚠️ Server error. Please try again.";
                    document.getElementById("errorMessage").style.display = "block";
                });
        });
    } else {
        console.warn("⚠️ No login form found on this page.");
    }

    //  LOGOUT FUNCTIONALITY
    let logoutButton = document.getElementById("logoutBtn");
    if (logoutButton) {
        logoutButton.addEventListener("click", function () {
            sessionStorage.removeItem("userRole"); // Remove session
            alert("👋 Logged out successfully!");
            window.location.href = "http://localhost:8080/CabBookingSystem_war_exploded/html/login.html";
        });
    }
});
