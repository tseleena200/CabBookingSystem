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

            // Client-Side Validation for Empty Fields
            if (email === "" || password === "") {
                Swal.fire({
                    icon: "error",
                    title: "Validation Error",
                    text: "Please fill in both email and password.",
                    confirmButtonColor: "#d33"
                });
                return; // Stop submission if validation fails
            }

            // Client-Side Validation for Email Format
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                Swal.fire({
                    icon: "error",
                    title: "Invalid Email Format",
                    text: "Please enter a valid email address.",
                    confirmButtonColor: "#d33"
                });
                return; // Stop submission if email format is invalid
            }

            // Send POST request if validation passes
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

// Display logout message if the "logged_out" message is passed in the URL
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const message = urlParams.get('message');
        if (message === 'logged_out') {
            Swal.fire({
                icon: 'info',
                title: 'You have been logged out',
                text: 'Please log in again to continue.',
                confirmButtonColor: '#3085d6'
            });
        }
    };

    // REGISTRATION FORM HANDLING
    let registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", function (event) {
            event.preventDefault();

            // Get form data
            let formData = new FormData(registerForm);

            // Client-side validation
            let valid = true;

            // Get form input values
            let customerRegNum = document.getElementById("regNumber").value.trim();
            let fullName = document.getElementById("name").value.trim();
            let address = document.getElementById("address").value.trim();
            let nic = document.getElementById("nic").value.trim();
            let email = document.getElementById("email").value.trim();
            let password = document.getElementById("password").value.trim();

            // Validate Empty Fields
            if (!customerRegNum || !fullName || !address || !nic || !email || !password) {
                valid = false;
                Swal.fire({
                    icon: "error",
                    title: "Missing Fields",
                    text: "Please fill in all fields.",
                    confirmButtonColor: "#d33"
                });
            }

            // Validate Customer Registration Number (only letters and numbers allowed)
            if (!/^[A-Za-z0-9]+$/.test(customerRegNum)) {
                valid = false;
                Swal.fire({
                    icon: "error",
                    title: "Invalid Registration Number",
                    text: "Please use only letters and numbers for the registration number.",
                    confirmButtonColor: "#d33"
                });
            }

            // Validate Full Name (only alphabets and spaces allowed)
            if (!/^[A-Za-z ]+$/.test(fullName)) {
                valid = false;
                Swal.fire({
                    icon: "error",
                    title: "Invalid Name",
                    text: "Name should contain only letters and spaces.",
                    confirmButtonColor: "#d33"
                });
            }

            // Validate NIC (must be 10 or 12 digits)
            if (!/^\d{10}$|^\d{12}$/.test(nic)) {
                valid = false;
                Swal.fire({
                    icon: "error",
                    title: "Invalid NIC",
                    text: "NIC should be either 10 or 12 digits long.",
                    confirmButtonColor: "#d33"
                });
            }

            // Validate Email format
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                valid = false;
                Swal.fire({
                    icon: "error",
                    title: "Invalid Email",
                    text: "Please enter a valid email address.",
                    confirmButtonColor: "#d33"
                });
            }

            // Validate Password (at least 6 characters, including one letter and one number)
            const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;
            if (!passwordPattern.test(password)) {
                valid = false;
                Swal.fire({
                    icon: "error",
                    title: "Invalid Password",
                    text: "Password must be at least 6 characters with at least one letter and one number.",
                    confirmButtonColor: "#d33"
                });
            }

            // If all client-side validations pass, proceed to check for duplicates
            if (valid) {
                // Check for duplicate Registration Number, NIC, and Email on the server
                Promise.all([
                    checkDuplicate("customer_registration_number", customerRegNum),
                    checkDuplicate("nic_number", nic),
                    checkDuplicate("email", email)
                ])
                    .then(results => {
                        if (results[0]) {
                            valid = false;
                            Swal.fire({
                                icon: "error",
                                title: "Duplicate Registration Number",
                                text: "This Customer Registration Number is already taken.",
                                confirmButtonColor: "#d33"
                            });
                        }
                        if (results[1]) {
                            valid = false;
                            Swal.fire({
                                icon: "error",
                                title: "Duplicate NIC",
                                text: "This NIC is already taken.",
                                confirmButtonColor: "#d33"
                            });
                        }
                        if (results[2]) {
                            valid = false;
                            Swal.fire({
                                icon: "error",
                                title: "Duplicate Email",
                                text: "This Email is already taken.",
                                confirmButtonColor: "#d33"
                            });
                        }

                        // If no duplicates, submit the form
                        if (valid) {
                            fetch("../register", {
                                method: "POST",
                                body: new URLSearchParams(formData)
                            })
                                .then(response => response.text())
                                .then(data => {
                                    console.log(data); // Log data to check server response
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
                                            text: "An error occurred while registering. Server response: " + data,  // Displaying the response from server
                                            confirmButtonColor: "#d33"
                                        });
                                    }
                                })
                                .catch(error => {
                                    console.error("🚨 Registration request failed:", error);
                                });
                        }
                    });
            }
        });
    }

// Helper function to check for duplicates (AJAX request to check server-side)
    function checkDuplicate(field, value) {
        return fetch(`../check_duplicate?field=${field}&value=${encodeURIComponent(value)}`)
            .then(response => response.text())
            .then(data => {
                return data.trim() === "duplicate"; // Server returns "duplicate" if value exists
            });
    }

    // ✅ AUTO-ASSIGN DRIVER BASED ON SELECTED CAR
    let carSelect = document.getElementById("car_id");
    let driverInput = document.getElementById("driver_id");

    if (carSelect && driverInput) {
        carSelect.addEventListener("change", function () {
            let selectedOption = carSelect.options[carSelect.selectedIndex];
            let driverId = selectedOption.getAttribute("data-driver-id");
            driverInput.value = driverId || ""; // Automatically fill driver ID
        });
    }

    // ✅ BOOKING FORM HANDLING (Fully Fixed & Validated)
    let bookingForm = document.getElementById("bookingForm");
    if (bookingForm) {
        bookingForm.addEventListener("submit", function (event) {
            event.preventDefault(); // ✅ Prevent default form submission

            let customerName = document.getElementById("customer_name").value.trim();
            let customerAddress = document.getElementById("customer_address").value.trim();
            let phoneNumber = document.getElementById("phone_number").value.trim();
            let destination = document.getElementById("destination").value.trim();
            let scheduledDate = document.getElementById("scheduled_date").value;
            let scheduledTime = document.getElementById("scheduled_time").value;
            let fareType = document.getElementById("fare_type").value.trim();
            let carId = document.getElementById("car_id").value.trim();
            let driverId = document.getElementById("driver_id").value.trim();

            let today = new Date();
            today.setHours(0, 0, 0, 0);
            let selectedDate = new Date(scheduledDate);
            let now = new Date();
            let selectedTime = new Date(`${scheduledDate}T${scheduledTime}`);

            console.log("📌 Booking Form Data:", { customerName, customerAddress, phoneNumber, destination, scheduledDate, scheduledTime, fareType, carId, driverId });

            // ✅ FULL INPUT VALIDATIONS

            // ✅ Name Validation (Only letters & spaces allowed)
            if (!/^[a-zA-Z\s]+$/.test(customerName)) {
                Swal.fire("Invalid Name!", "Customer name should contain only letters and spaces.", "error");
                return;
            }

            // ✅ Address Validation (Min length 5)
            if (customerAddress.length < 5) {
                Swal.fire("Invalid Address!", "Address must be at least 5 characters long.", "error");
                return;
            }

            // ✅ Phone Number Validation (Must be exactly 10 digits)
            if (!/^\d{10}$/.test(phoneNumber)) {
                Swal.fire("Invalid Phone Number!", "Phone number must be exactly 10 digits.", "error");
                return;
            }

            // ✅ Destination Validation (Cannot be the same as Address)
            if (destination.toLowerCase() === customerAddress.toLowerCase()) {
                Swal.fire("Invalid Destination!", "Destination cannot be the same as the address.", "error");
                return;
            }

            // ✅ Scheduled Date Validation (Cannot be a past date)
            if (selectedDate < today) {
                Swal.fire("Invalid Date!", "Scheduled date must be today or later.", "error");
                return;
            }

            // ✅ Scheduled Time Validation (Cannot be in the past)
            if (selectedTime < now) {
                Swal.fire("Invalid Time!", "Scheduled time cannot be in the past.", "error");
                return;
            }

            // ✅ Car Selection Validation
            if (!carId) {
                Swal.fire("Car Selection Required!", "Please select a car for the booking.", "error");
                return;
            }

            // ✅ Driver Selection Validation (Ensures a driver is linked to the car)
            if (!driverId) {
                Swal.fire("Driver Error!", "No driver assigned to the selected car.", "error");
                return;
            }

            // ✅ Fare Type Validation
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

                    let responseData = data.trim();

                    if (responseData.startsWith("success:")) {
                        let orderNumber = responseData.split(":")[1];

                        Swal.fire({
                            icon: "success",
                            title: "Booking Confirmed!",
                            text: `Your order number is: ${orderNumber}`,
                            timer: 3000,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.href = "view_bookings.jsp"; // ✅ Redirect correctly after SweetAlert
                        });

                    } else if (responseData === "db_insert_fail") {
                        Swal.fire("Database Error!", "Failed to insert booking into the database.", "error");

                    } else if (responseData === "missing_fields") {
                        Swal.fire("Validation Error!", "Please fill in all required fields.", "error");

                    } else if (responseData === "duplicate_booking") {
                        Swal.fire("Duplicate Booking!", "This booking already exists.", "warning");

                    } else {
                        Swal.fire("Error!", "An unexpected error occurred while booking.", "error");
                    }
                })
                .catch(error => {
                    console.error("🚨 Booking request failed:", error);
                    Swal.fire("Server Error!", "Please try again later.", "error");
                });
        });
    }



    // ✅ Handle Booking Confirmation
    document.addEventListener("click", function (event) {
        if (event.target.matches(".confirm-booking-btn")) {
            let orderNumber = event.target.dataset.orderNumber;

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
                            if (data.trim() === "success") {
                                Swal.fire("Confirmed!", "The booking has been confirmed.", "success");

                                // ✅ Update UI
                                let statusCell = document.getElementById(`status_${orderNumber}`);
                                if (statusCell) {
                                    statusCell.innerHTML = "<span class='status-confirmed'>Confirmed</span>";
                                }

                                // ✅ Add "Cancel" button dynamically after confirmation
                                let actionCell = event.target.closest("td");
                                actionCell.innerHTML = `<button class="cancel-booking-btn" data-order-number="${orderNumber}">Cancel</button>`;
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

                    // ✅ Stop execution if the order is already calculated
                    if (data === "already_calculated") {
                        Swal.fire("Warning!", "This order has already been calculated.", "warning");
                        return;
                    }

                    if (data.startsWith("success:")) {
                        let fareData = data.split(":")[1].split(",");
                        let totalFare = parseFloat(fareData[0]).toFixed(2); // ✅ Format to 2 decimal places
                        let taxAmount = parseFloat(fareData[1] || 0).toFixed(2);
                        let discountAmount = parseFloat(fareData[2] || 0).toFixed(2);

                        // ✅ Update UI with formatted values
                        document.getElementById("summaryOrder").innerText = orderNumber;
                        document.getElementById("summaryBaseFare").innerText = `AED ${parseFloat(baseFare).toFixed(2)}`;
                        document.getElementById("summaryDistance").innerText = `${parseFloat(distance).toFixed(1)} KM`;
                        document.getElementById("summaryTax").innerText = `AED ${taxAmount}`;
                        document.getElementById("summaryDiscount").innerText = `AED ${discountAmount}`;
                        document.getElementById("summaryTotal").innerText = `AED ${totalFare}`;

                        document.getElementById("billSummary").style.display = "block";

                        Swal.fire("Success!", `Total Fare (after tax & discount): AED ${totalFare}`, "success");
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
                    console.error("🚨 Error:", error);
                    Swal.fire("Error!", "Network issue. Please try again later.", "error");
                });
        });
    }

    // ✅ EDIT BOOKING FORM HANDLING
    let editBookingForm = document.getElementById("editBookingForm");
    if (editBookingForm) {
        editBookingForm.addEventListener("submit", function(event) {
            event.preventDefault();
            let formData = new FormData(this);
            fetch("../editBooking", {
                method: "POST",
                body: new URLSearchParams(formData)
            })
                .then(response => response.text())
                .then(data => {
                    console.log("📌 Edit Booking Response:", data.trim());
                    if (data.trim() === "success") {
                        Swal.fire({
                            icon: "success",
                            title: "Updated!",
                            text: "Booking details have been updated.",
                            timer: 2000,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.href = "view_bookings.jsp";
                        });
                    } else {
                        Swal.fire("Error!", "Failed to update booking.", "error");
                    }
                })
                .catch(error => {
                    console.error("🚨 Error Updating Booking:", error);
                    Swal.fire("Error!", "Network error. Please try again later.", "error");
                });
        });
    }

    // ✅ DELETE BOOKING FUNCTIONALITY
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("delete-booking-btn")) {
            let orderNumber = event.target.dataset.orderNumber;
            Swal.fire({
                title: "Are you sure?",
                text: "This action is irreversible!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Yes, Delete!",
                confirmButtonColor: "#d33"
            }).then((result) => {
                if (result.isConfirmed) {
                    fetch("../deleteBooking", {
                        method: "POST",
                        headers: { "Content-Type": "application/x-www-form-urlencoded" },
                        body: new URLSearchParams({ order_number: orderNumber })
                    })
                        .then(response => response.text())
                        .then(data => {
                            console.log("📌 Delete Booking Response:", data.trim());
                            if (data.trim() === "success") {
                                Swal.fire("Deleted!", "Booking has been deleted.", "success").then(() => {
                                    event.target.closest("tr").remove();
                                });
                            } else {
                                Swal.fire("Error!", "Failed to delete booking.", "error");
                            }
                        })
                        .catch(error => {
                            console.error("🚨 Delete Booking Failed:", error);
                            Swal.fire("Error!", "Network error. Please try again later.", "error");
                        });
                }
            });
        }
    });

    // ✅ CANCEL BOOKING FUNCTIONALITY
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("cancel-booking-btn")) {
            let orderNumber = event.target.dataset.orderNumber;
            Swal.fire({
                title: "Cancel Booking?",
                text: "Are you sure you want to cancel this booking?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Yes, Cancel!",
                cancelButtonText: "No",
                confirmButtonColor: "#d33"
            }).then((result) => {
                if (result.isConfirmed) {
                    fetch("../cancelBooking", {
                        method: "POST",
                        headers: { "Content-Type": "application/x-www-form-urlencoded" },
                        body: new URLSearchParams({ order_number: orderNumber })
                    })
                        .then(response => response.text())
                        .then(data => {
                            console.log("📌 Cancel Booking Response:", data.trim());
                            if (data.trim() === "success") {
                                Swal.fire("Canceled!", "The booking has been canceled.", "success");
                                let statusCell = document.getElementById(`status_${orderNumber}`);
                                if (statusCell) {
                                    statusCell.innerHTML = "<span class='status-canceled'>Canceled</span>";
                                }
                                event.target.remove();
                            } else {
                                Swal.fire("Error!", "Failed to cancel booking.", "error");
                            }
                        })
                        .catch(error => {
                            console.error("🚨 Cancel Booking Failed:", error);
                            Swal.fire("Error!", "Network error. Please try again later.", "error");
                        });
                }
            });
        }
    });


        // Search functionality for bookings
        let searchInput = document.getElementById("searchBooking");
        let filterStatus = document.getElementById("filterStatus");

        if (searchInput) {
            searchInput.addEventListener("input", function () {
                let query = this.value.toLowerCase();
                document.querySelectorAll("tbody tr").forEach(row => {
                    row.style.display = row.innerText.toLowerCase().includes(query) ? "" : "none";
                });
            });
        }

        // Filter functionality for bookings based on status
        if (filterStatus) {
            filterStatus.addEventListener("change", function () {
                let status = this.value;
                document.querySelectorAll("tbody tr").forEach(row => {
                    let rowStatus = row.children[6]?.innerText || "";  // Status is in the 7th column (index 6)
                    row.style.display = status === "" || rowStatus === status ? "" : "none";
                });
            });
        }

        // Driver reassignment functionality for bookings
        document.querySelectorAll(".driver-select").forEach(select => {
            select.addEventListener("change", function () {
                let orderNumber = this.dataset.orderNumber;  // Get the order number from the select element
                let driverId = this.value;  // Get the driver ID from the selected option

                if (driverId) {
                    fetch("../updateDriver", {
                        method: "POST",
                        headers: { "Content-Type": "application/x-www-form-urlencoded" },
                        body: new URLSearchParams({ order_number: orderNumber, driver_id: driverId })
                    })
                        .then(response => response.text())
                        .then(data => {
                            if (data.trim() === "success") {
                                Swal.fire("Updated!", "Driver reassigned successfully!", "success");
                            } else {
                                Swal.fire("Error!", "Failed to update driver.", "error");
                            }
                        })
                        .catch(error => {
                            console.error("Error updating driver:", error);
                            Swal.fire("Error!", "Network issue. Try again later.", "error");
                        });
                }
            });
        });


        // ✅ Handle Complete Booking functionality
        document.addEventListener("click", function (event) {
            // Check if the clicked element is the "Complete Booking" button
            if (event.target.classList.contains("complete-booking-btn")) {
                let orderNumber = event.target.dataset.orderNumber;  // Get order number from button's data attribute

                Swal.fire({
                    title: "Complete Booking?",
                    text: "Are you sure you want to complete this booking?",
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonText: "Yes, Complete!",
                    cancelButtonText: "Cancel",
                    confirmButtonColor: "#28a745"
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Send request to complete the booking
                        fetch("../completeBooking", {
                            method: "POST",
                            headers: { "Content-Type": "application/x-www-form-urlencoded" },
                            body: new URLSearchParams({ order_number: orderNumber })
                        })
                            .then(response => response.text())
                            .then(data => {
                                if (data.trim() === "success") {
                                    Swal.fire("Completed!", "The booking has been marked as completed.", "success");

                                    // Optionally, you can update the booking status in the UI
                                    let statusCell = document.getElementById(`status_${orderNumber}`);
                                    if (statusCell) {
                                        statusCell.innerHTML = "<span class='status-completed'>Completed</span>";
                                    }

                                    // Disable the "Complete" button once booking is completed
                                    event.target.disabled = true;
                                } else {
                                    Swal.fire("Error!", "Failed to complete the booking.", "error");
                                }
                            })
                            .catch(error => {
                                console.error("🚨 Complete Booking Failed:", error);
                                Swal.fire("Error!", "There was a problem completing the booking. Please try again.", "error");
                            });
                    }
                });
            }
        });




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

    document.addEventListener("DOMContentLoaded", function () {
        console.log("✅ DOM fully loaded!");

        // Get the success and error message parameters from the URL
        const urlParams = new URLSearchParams(window.location.search);
        const successMessage = urlParams.get("success");
        const errorMessage = urlParams.get("error");

        // Debug: Log the parameters to make sure they're being passed
        console.log("Success message:", successMessage);
        console.log("Error message:", errorMessage);

        // Display SweetAlert based on the success or error message
        if (successMessage === "car_deleted") {
            Swal.fire({
                title: "Success!",
                text: "Car deleted successfully.",
                icon: "success",
                confirmButtonColor: "#28a745"
            });
        } else if (errorMessage === "delete_failed") {
            Swal.fire({
                title: "Error!",
                text: "Failed to delete the car. Try again.",
                icon: "error",
                confirmButtonColor: "#d33"
            });
        } else if (errorMessage === "server_error") {
            Swal.fire({
                title: "Server Error!",
                text: "An unexpected error occurred.",
                icon: "error",
                confirmButtonColor: "#d33"
            });
        }
    });


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

                    } else if (data.trim() === "duplicate_license") {
                        Swal.fire({
                            icon: "warning",
                            title: "Duplicate License Plate!",
                            text: "This license plate is already registered.",
                            confirmButtonColor: "#f39c12"
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
        });}
});
