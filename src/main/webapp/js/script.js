<!-- Loading Screen -->
document.addEventListener("DOMContentLoaded", function () {
    setTimeout(() => {
        document.getElementById("loading-screen").classList.add("fade-out");
        document.getElementById("main-content").style.display = "block";
    }, 3000); // 3 seconds delay before showing content
});
<!-- Loading Screen -->