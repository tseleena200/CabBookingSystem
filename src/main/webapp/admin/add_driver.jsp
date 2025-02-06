<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Add Driver</title>
  <link rel="stylesheet" href="../css/adminpanel.css">
</head>
<body>

<div class="container">
  <!-- ✅ Left Panel (Intro) -->
  <div class="left-panel">
    <h2>Add New Driver </h2>
    <p>Fill in the details to add a new driver.</p>
  </div>

  <!-- ✅ Right Panel (Form) -->
  <div class="right-panel">
    <h1>Add a New Driver</h1>
    <form id="addDriverForm" action="../addDriver" method="POST">
      <div class="form-group">
        <label>Driver Full Name:</label>
        <input type="text" id="driver_name" name="full_name" required>
      </div>

      <div class="form-group">
        <label>Driver License Number:</label>
        <input type="text" id="driver_license" name="license_number" required>
      </div>

      <div class="form-group">
        <label>Contact Number:</label>
        <input type="number" id="driver_contact" name="contact_number" required>
      </div>

      <button type="submit">Add Driver</button>
      <button type="button" class="cancel-btn" onclick="window.location.href='admin_panel.jsp'">Cancel</button>
    </form>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="../js/script.js"></script>

</body>
</html>
