document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ DOM fully loaded!");

    //  LOADING SCREEN HANDLING
    let loadingScreen = document.getElementById("loading-screen");
    if (loadingScreen) {
        setTimeout(() => {
            loadingScreen.classList.add("fade-out");
            setTimeout(() => {
                window.location.href = "../html/auth.html";
            }, 500);
        }, 2000);
    }

    //  LOGIN FORM HANDLING
    let loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();

            let email = document.getElementById("email").value.trim();
            let password = document.getElementById("password").value.trim();

            fetch("../login", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
            })
                .then(response => response.text())
                .then(data => {
                    if (data.trim() === "admin") {
                        Swal.fire({
                            icon: "success",
                            title: "Welcome, Admin!",
                            text: "Redirecting to Admin Panel...",
                            timer: 2000,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.href = "../admin/admin_panel.jsp";
                        });

                    } else if (data.trim() === "employee") {
                        Swal.fire({
                            icon: "success",
                            title: "Login Successful!",
                            text: "Redirecting to Employee Dashboard...",
                            timer: 2000,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.href = "../jsp/homepage.jsp";
                        });

                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Login Failed!",
                            text: "Invalid email or password!",
                            confirmButtonColor: "#d33"
                        });
                    }
                })
                .catch(error => {
                    console.error("🚨 Login request failed:", error);
                    Swal.fire({
                        icon: "error",
                        title: "Server Error!",
                        text: "Please try again later.",
                        confirmButtonColor: "#d33"
                    });
                });
        });
    }

    //  REGISTRATION FORM HANDLING
    let registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", function (event) {
            event.preventDefault();

            let formData = new FormData(registerForm);

            fetch("../register", {
                method: "POST",
                body: new URLSearchParams(formData)
            })
                .then(response => response.text())
                .then(data => {
                    if (data.trim() === "success") {
                        Swal.fire({
                            icon: "success",
                            title: "Registration Successful!",
                            text: "Employee has been registered.",
                            timer: 2500,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.href = "../admin/admin_panel.jsp";
                        });

                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Registration Failed!",
                            text: "An error occurred while registering.",
                            confirmButtonColor: "#d33"
                        });
                    }
                })
                .catch(error => {
                    console.error("🚨 Registration request failed:", error);
                });
        });
    }

    //  BOOKING FORM HANDLING
    let bookingForm = document.getElementById("bookingForm");
    if (bookingForm) {
        bookingForm.addEventListener("submit", function (event) {
            event.preventDefault();

            let customerName = document.getElementById("customer_name").value.trim();
            let customerAddress = document.getElementById("customer_address").value.trim();
            let phoneNumber = document.getElementById("phone_number").value.trim();
            let destination = document.getElementById("destination").value.trim();
            let scheduledDate = document.getElementById("scheduled_date").value.trim();
            let scheduledTime = document.getElementById("scheduled_time").value.trim();
            let carId = document.getElementById("car_id").value.trim();
            let employeeRegNumber = document.getElementById("employee_reg_number").value.trim();

            if (!customerName || !customerAddress || !phoneNumber || !destination || !scheduledDate || !scheduledTime || !carId) {
                Swal.fire({
                    icon: "error",
                    title: "Missing Fields",
                    text: "Please fill in all required fields!",
                    confirmButtonColor: "#d33"
                });
                return;
            }

            let formData = new URLSearchParams();
            formData.append("customer_name", customerName);
            formData.append("customer_address", customerAddress);
            formData.append("phone_number", phoneNumber);
            formData.append("destination", destination);
            formData.append("scheduled_date", scheduledDate);
            formData.append("scheduled_time", scheduledTime);
            formData.append("car_id", carId);
            formData.append("employee_reg_number", employeeRegNumber);

            fetch("../addBooking", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: formData
            })
                .then(response => response.text())
                .then(data => {
                    console.log("🔍 Server Response:", data.trim());

                    if (data.startsWith("success:")) {
                        let orderNumber = data.split(":")[1];
                        Swal.fire({
                            icon: "success",
                            title: "Booking Confirmed!",
                            text: `Your order number is: ${orderNumber}`,
                            timer: 2500,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.reload();
                        });

                    } else if (data === "car_already_booked") {
                        Swal.fire({
                            icon: "warning",
                            title: "Car Unavailable!",
                            text: "This car is already booked for the selected time.",
                            confirmButtonColor: "#f39c12"
                        });

                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Booking Failed",
                            text: "An error occurred while booking.",
                            confirmButtonColor: "#d33"
                        });
                    }
                })
                .catch(error => {
                    console.error("🚨 Booking request failed:", error);
                });
        });
    }

    let addCarForm = document.getElementById("addCarForm");
    if (addCarForm) {
        addCarForm.addEventListener("submit", function (event) {
            event.preventDefault();

            let formData = new FormData(addCarForm);

            fetch("../addCar", {
                method: "POST",
                body: new URLSearchParams(formData)
            })
                .then(response => response.text())
                .then(data => {
                    console.log("🔍 Server Response:", data.trim());

                    if (data.trim() === "success") {
                        Swal.fire({
                            icon: "success",
                            title: "Car Added Successfully!",
                            timer: 2000,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.reload();
                        });

                    } else if (data.trim() === "duplicate_license") {
                        Swal.fire({
                            icon: "warning",
                            title: "Duplicate License Plate!",
                            text: "This license plate is already registered.",
                            confirmButtonColor: "#f39c12"
                        });

                    } else if (data.trim() === "driver_already_assigned") {
                        Swal.fire({
                            icon: "error",
                            title: "Driver Already Assigned!",
                            text: "This driver is already assigned to another car.",
                            confirmButtonColor: "#d33"
                        });

                    } else if (data.trim() === "missing_fields") {
                        Swal.fire({
                            icon: "error",
                            title: "Missing Fields",
                            text: "Please fill in all required fields!",
                            confirmButtonColor: "#d33"
                        });

                    } else if (data.trim() === "invalid_capacity") {
                        Swal.fire({
                            icon: "error",
                            title: "Invalid Capacity",
                            text: "Please enter a valid capacity (numbers only).",
                            confirmButtonColor: "#d33"
                        });

                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Failed to Add Car!",
                            text: "An error occurred.",
                            confirmButtonColor: "#d33"
                        });
                    }
                })
                .catch(error => {
                    console.error("🚨 Add Car Request Failed:", error);
                });
        });
    }

    //  ADD DRIVER FORM HANDLING
    let addDriverForm = document.getElementById("addDriverForm");
    if (addDriverForm) {
        addDriverForm.addEventListener("submit", function (event) {
            event.preventDefault();

            let formData = new FormData(addDriverForm);

            fetch("../addDriver", {
                method: "POST",
                body: new URLSearchParams(formData)
            })
                .then(response => response.text())
                .then(data => {
                    if (data.trim() === "success") {
                        Swal.fire({
                            icon: "success",
                            title: "Driver Added Successfully!",
                            timer: 2000,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.reload();
                        });

                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Failed to Add Driver!",
                            text: "An error occurred.",
                            confirmButtonColor: "#d33"
                        });
                    }
                })
                .catch(error => {
                    console.error("🚨 Add Driver Request Failed:", error);
                });
        });
    }
});
