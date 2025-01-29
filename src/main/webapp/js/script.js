// ✅ Loading Screen Functionality
document.addEventListener("DOMContentLoaded", function () {
    setTimeout(() => {
        document.getElementById("loading-screen").classList.add("fade-out");

        // Redirect after fade out
        setTimeout(() => {
            window.location.href = "auth.html";
        }, 500);
    }, 3000);
});



