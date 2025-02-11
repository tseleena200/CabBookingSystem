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
                let scheduledDate = document.getElementById("scheduled_date").value;
                let scheduledTime = document.getElementById("scheduled_time").value;
                let fareType = document.getElementById("fare_type").value.trim();
                let carId = document.getElementById("car_id").value.trim();

                let today = new Date();
                today.setHours(0, 0, 0, 0);
                let selectedDate = new Date(scheduledDate);
                let now = new Date();
                let selectedTime = new Date(`${scheduledDate}T${scheduledTime}`);

                //  Name Validation
                if (!/^[a-zA-Z\s]+$/.test(customerName)) {
                    Swal.fire("Invalid Name!", "Customer name should contain only letters and spaces.", "error");
                    return;
                }

                // Address Validation
                if (customerAddress.length < 5) {
                    Swal.fire("Invalid Address!", "Address must be at least 5 characters long.", "error");
                    return;
                }

                // Phone Number Validation (Must be 10 digits)
                if (!/^\d{10}$/.test(phoneNumber)) {
                    Swal.fire("Invalid Phone Number!", "Phone number must be exactly 10 digits.", "error");
                    return;
                }

                //  Destination Validation (Cannot be the same as Address)
                if (destination.toLowerCase() === customerAddress.toLowerCase()) {
                    Swal.fire("Invalid Destination!", "Destination cannot be the same as the address.", "error");
                    return;
                }

                //  Scheduled Date Validation (Cannot be past)
                if (selectedDate < today) {
                    Swal.fire("Invalid Date!", "Scheduled date must be today or later.", "error");
                    return;
                }

                //  Scheduled Time Validation (Cannot be past today)
                if (selectedTime < now) {
                    Swal.fire("Invalid Time!", "Scheduled time cannot be in the past.", "error");
                    return;
                }

                //  Car Selection Validation
                if (!carId) {
                    Swal.fire("Car Selection Required!", "Please select a car for the booking.", "error");
                    return;
                }

                //  Fare Type Validation
                if (!fareType) {
                    Swal.fire("Fare Type Required!", "Please select a fare type before booking.", "error");
                    return;
                }

                let formData = new URLSearchParams(new FormData(bookingForm));

                fetch("../addBooking", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: formData
                })
                    .then(response => response.text())
                    .then(data => {
                        console.log("🔍 Server Response:", data.trim());

                        if (data.startsWith("success:")) {
                            Swal.fire("Booking Confirmed!", `Your order number is: ${data.split(":")[1]}`, "success").then(() => {
                                window.location.reload();
                            });

                        } else if (data.trim() === "db_insert_fail") {
                            Swal.fire("Database Error!", "Failed to insert booking into the database.", "error");

                        } else {
                            Swal.fire("Error!", "An error occurred while booking.", "error");
                        }
                    })
                    .catch(error => {
                        console.error("🚨 Booking request failed:", error);
                        Swal.fire("Server Error!", "Please try again later.", "error");
                    });
            });
        }

        //Confirm Form handling
    document.addEventListener("click", function (event) {
        if (event.target.matches(".confirm-booking-btn")) {
            let orderNumber = event.target.dataset.orderNumber;

            if (!orderNumber) {
                console.error("🚨 Error: Missing order number in dataset.");
                Swal.fire("Error!", "Missing order number. Please try again.", "error");
                return;
            }

            Swal.fire({
                title: "Confirm Booking?",
                text: "Are you sure you want to confirm this booking?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Yes, Confirm!",
                cancelButtonText: "Cancel",
                confirmButtonColor: "#28a745"
            }).then((result) => {
                if (result.isConfirmed) {
                    fetch("../bookingConfirmation", {
                        method: "POST",
                        headers: { "Content-Type": "application/x-www-form-urlencoded" },
                        body: "order_number=" + encodeURIComponent(orderNumber)
                    })
                        .then(response => response.text())
                        .then(data => {
                            console.log("📌 Server Response:", data);
                            if (data.trim() === "success") {
                                Swal.fire("Confirmed!", "The booking has been confirmed.", "success");
                                document.getElementById(`status_${orderNumber}`).innerHTML = "<span class='status-confirmed'>Confirmed</span>";
                                event.target.remove();
                            } else {
                                Swal.fire("Error!", "Failed to confirm booking.", "error");
                            }
                        })
                        .catch(error => console.error("🚨 Booking Confirmation Failed:", error));
                }
            });
        }
    });



// ✅ Handle Bill Calculation
    let billForm = document.getElementById("billForm");
    if (billForm) {
        billForm.addEventListener("submit", function (event) {
            event.preventDefault();

            let orderNumber = document.getElementById("order_number").value.trim();
            let distance = document.getElementById("distance").value.trim();
            let baseFare = document.getElementById("base_fare").value.trim();
            let taxRate = document.getElementById("tax_rate").value.trim();
            let discountRate = document.getElementById("discount_rate").value.trim();

            console.log("📌 Submitting Order Number:", orderNumber);
            console.log("📌 Distance Entered:", distance);
            console.log("📌 Base Fare Entered:", baseFare);
            console.log("📌 Tax Rate Entered:", taxRate);
            console.log("📌 Discount Rate Entered:", discountRate);

            // ✅ Input Validations
            if (!orderNumber) {
                Swal.fire("Error!", "Please enter a valid order number.", "error");
                return;
            }
            if (!distance || isNaN(distance) || distance <= 0) {
                Swal.fire("Error!", "Please enter a valid distance greater than 0.", "error");
                return;
            }
            if (!baseFare || isNaN(baseFare) || baseFare <= 0) {
                Swal.fire("Error!", "Please enter a valid base fare greater than 0.", "error");
                return;
            }
            if (!taxRate || isNaN(taxRate) || taxRate < 0 || taxRate > 100) {
                Swal.fire("Error!", "Please enter a valid tax rate (0-100%).", "error");
                return;
            }
            if (!discountRate || isNaN(discountRate) || discountRate < 0 || discountRate > 100) {
                Swal.fire("Error!", "Please enter a valid discount rate (0-100%).", "error");
                return;
            }

            let formData = new URLSearchParams();
            formData.append("order_number", orderNumber);
            formData.append("distance", distance);
            formData.append("base_fare", baseFare);
            formData.append("tax_rate", taxRate);
            formData.append("discount_rate", discountRate);

            fetch("../calculateBill", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: formData
            })
                .then(response => response.text())
                .then(data => {
                    console.log("🔍 Server Response:", data.trim());

                    if (data.startsWith("success:")) {
                        let fareData = data.split(":")[1].split(",");
                        let totalFare = parseFloat(fareData[0]);
                        let taxAmount = parseFloat(fareData[1]) || 0;
                        let discountAmount = parseFloat(fareData[2]) || 0;

                        // ✅ Prevent `NaN` issues by ensuring default values
                        taxAmount = isNaN(taxAmount) ? 0 : taxAmount;
                        discountAmount = isNaN(discountAmount) ? 0 : discountAmount;

                        document.getElementById("summaryOrder").innerText = orderNumber;
                        document.getElementById("summaryBaseFare").innerText = `AED ${parseFloat(baseFare).toFixed(2)}`;
                        document.getElementById("summaryDistance").innerText = `${parseFloat(distance).toFixed(1)} KM`;
                        document.getElementById("summaryTax").innerText = `AED ${taxAmount.toFixed(2)}`;
                        document.getElementById("summaryDiscount").innerText = `AED ${discountAmount.toFixed(2)}`;
                        document.getElementById("summaryTotal").innerText = `AED ${totalFare.toFixed(2)}`;

                        document.getElementById("billSummary").style.display = "block";

                        Swal.fire("Success!", `Total Fare (after tax & discount): AED ${totalFare.toFixed(2)}`, "success");
                    } else if (data === "already_calculated") {
                        Swal.fire("Warning!", "This order has already been calculated.", "warning");
                    } else if (data === "missing_order_number") {
                        Swal.fire("Error!", "Please enter a valid order number.", "error");
                    } else if (data === "booking_not_found") {
                        Swal.fire("Error!", "No booking found with this order number.", "error");
                    } else if (data === "not_confirmed") {
                        Swal.fire("Warning!", "Booking must be confirmed before calculating the fare.", "warning");
                    } else if (data === "update_failed") {
                        Swal.fire("Error!", "Failed to update the database. Please try again.", "error");
                    } else {
                        Swal.fire("Error!", "An unexpected error occurred. Try again later.", "error");
                    }
                })
                .catch(error => {
                    console.error(" Error:", error);
                    Swal.fire("Error!", "Network issue. Please try again later.", "error");
                });
        });
    }
//car form handling
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
