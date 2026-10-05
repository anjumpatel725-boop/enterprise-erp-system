import { useEffect, useState } from "react";
import api from "../services/api";

function Employees() {
  const [employees, setEmployees] = useState([]);
  const [departments, setDepartments] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [showForm, setShowForm] = useState(false);
  const [showLoginForm, setShowLoginForm] = useState(false);

  const [selectedEmployee, setSelectedEmployee] = useState(null);

  const [formData, setFormData] = useState({
    employeeCode: "",
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    dateOfJoining: "",
    designation: "",
    salary: "",
    status: "ACTIVE",
    departmentId: "",
  });

  const [loginData, setLoginData] = useState({
    username: "",
    email: "",
    password: "",
  });

  // =========================
  // LOAD EMPLOYEES
  // =========================

  const loadEmployees = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get("/hr/employees");

      setEmployees(response.data);
    } catch (err) {
      console.error("Failed to load employees:", err);
      setError("Failed to load employees.");
    } finally {
      setLoading(false);
    }
  };

  // =========================
  // LOAD DEPARTMENTS
  // =========================

  const loadDepartments = async () => {
    try {
      const response = await api.get("/hr/departments");

      setDepartments(response.data);
    } catch (err) {
      console.error("Failed to load departments:", err);
      setError("Failed to load departments.");
    }
  };

  // =========================
  // INITIAL LOAD
  // =========================

  useEffect(() => {
    loadEmployees();
    loadDepartments();
  }, []);

  // =========================
  // EMPLOYEE FORM CHANGE
  // =========================

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // =========================
  // LOGIN FORM CHANGE
  // =========================

  const handleLoginChange = (e) => {
    const { name, value } = e.target;

    setLoginData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // =========================
  // CREATE EMPLOYEE
  // =========================

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setError("");
      setSuccess("");

      const payload = {
        employeeCode: formData.employeeCode,
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        phone: formData.phone,
        dateOfJoining: formData.dateOfJoining,
        designation: formData.designation,
        salary: Number(formData.salary),
        status: formData.status,
        departmentId: Number(formData.departmentId),
      };

      await api.post("/hr/employees", payload);

      setSuccess("Employee created successfully.");

      setFormData({
        employeeCode: "",
        firstName: "",
        lastName: "",
        email: "",
        phone: "",
        dateOfJoining: "",
        designation: "",
        salary: "",
        status: "ACTIVE",
        departmentId: "",
      });

      setShowForm(false);

      await loadEmployees();
    } catch (err) {
      console.error("Failed to create employee:", err);

      const message =
        err.response?.data?.message ||
        err.response?.data ||
        "Failed to create employee.";

      setError(message);
    }
  };

  // =========================
  // OPEN CREATE LOGIN FORM
  // =========================

  const openLoginForm = (employee) => {
    setSelectedEmployee(employee);

    setLoginData({
      username: employee.email
        ? employee.email.split("@")[0]
        : "",
      email: employee.email || "",
      password: "",
    });

    setError("");
    setSuccess("");
    setShowLoginForm(true);
  };

  // =========================
  // CLOSE LOGIN FORM
  // =========================

  const closeLoginForm = () => {
    setShowLoginForm(false);
    setSelectedEmployee(null);

    setLoginData({
      username: "",
      email: "",
      password: "",
    });
  };

  // =========================
  // CREATE EMPLOYEE LOGIN
  // =========================

  const handleCreateLogin = async (e) => {
    e.preventDefault();

    if (!selectedEmployee) {
      setError("Please select an employee.");
      return;
    }

    try {
      setError("");
      setSuccess("");

      const payload = {
        username: loginData.username,
        email: loginData.email,
        password: loginData.password,
      };

      const response = await api.post(
        `/auth/employee/${selectedEmployee.id}/login`,
        payload
      );

      setSuccess(
        response.data?.message ||
          "Employee login created successfully."
      );

      closeLoginForm();
    } catch (err) {
      console.error("Failed to create employee login:", err);

      const message =
        err.response?.data?.message ||
        err.response?.data ||
        "Failed to create employee login.";

      setError(message);
    }
  };

  // =========================
  // LOADING
  // =========================

  if (loading) {
    return (
      <div className="page-container">
        <div className="loading">
          <div className="spinner"></div>
          <p>Loading employees...</p>
        </div>
      </div>
    );
  }

  // =========================
  // UI
  // =========================

  return (
    <div className="page-container">

      {/* =========================
          PAGE HEADER
          ========================= */}

      <div className="page-header">

        <div>
          <h1>Employees</h1>
          <p>Manage employee information</p>
        </div>

        <div className="header-actions">

          <button
            type="button"
            className="primary-button"
            onClick={() => {
              setShowForm(!showForm);
              setShowLoginForm(false);
              setError("");
              setSuccess("");
            }}
          >
            {showForm
              ? "✕ Close"
              : "+ Create Employee"}
          </button>

          <button
            type="button"
            className="secondary-button"
            onClick={loadEmployees}
          >
            ↻ Refresh
          </button>

        </div>

      </div>

      {/* =========================
          SUCCESS
          ========================= */}

      {success && (
        <div className="success-message">
          ✓ {success}
        </div>
      )}

      {/* =========================
          ERROR
          ========================= */}

      {error && (
        <div className="error-message">
          ⚠ {error}
        </div>
      )}

      {/* =========================
          CREATE EMPLOYEE FORM
          ========================= */}

      {showForm && (
        <div className="content-card">

          <div className="card-header">
            <div>
              <h2>Create Employee</h2>
              <p>
                Add a new employee to the organization
              </p>
            </div>
          </div>

          <form onSubmit={handleSubmit}>

            <div className="form-grid">

              <div className="form-group">
                <label>Employee Code</label>

                <input
                  type="text"
                  name="employeeCode"
                  value={formData.employeeCode}
                  onChange={handleChange}
                  placeholder="EMP004"
                  required
                />
              </div>

              <div className="form-group">
                <label>First Name</label>

                <input
                  type="text"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  placeholder="First name"
                  required
                />
              </div>

              <div className="form-group">
                <label>Last Name</label>

                <input
                  type="text"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  placeholder="Last name"
                  required
                />
              </div>

              <div className="form-group">
                <label>Email</label>

                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="employee@gmail.com"
                  required
                />
              </div>

              <div className="form-group">
                <label>Phone</label>

                <input
                  type="text"
                  name="phone"
                  value={formData.phone}
                  onChange={handleChange}
                  placeholder="9876543210"
                  required
                />
              </div>

              <div className="form-group">
                <label>Date of Joining</label>

                <input
                  type="date"
                  name="dateOfJoining"
                  value={formData.dateOfJoining}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label>Designation</label>

                <input
                  type="text"
                  name="designation"
                  value={formData.designation}
                  onChange={handleChange}
                  placeholder="Software Engineer"
                  required
                />
              </div>

              <div className="form-group">
                <label>Salary</label>

                <input
                  type="number"
                  name="salary"
                  value={formData.salary}
                  onChange={handleChange}
                  placeholder="60000"
                  min="0"
                  required
                />
              </div>

              <div className="form-group">
                <label>Department</label>

                <select
                  name="departmentId"
                  value={formData.departmentId}
                  onChange={handleChange}
                  required
                >
                  <option value="">
                    Select Department
                  </option>

                  {departments.map((department) => (
                    <option
                      key={department.id}
                      value={department.id}
                    >
                      {department.name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Status</label>

                <select
                  name="status"
                  value={formData.status}
                  onChange={handleChange}
                >
                  <option value="ACTIVE">
                    ACTIVE
                  </option>

                  <option value="INACTIVE">
                    INACTIVE
                  </option>
                </select>
              </div>

            </div>

            <div className="form-actions">

              <button
                type="submit"
                className="success-button"
              >
                ✓ Create Employee
              </button>

              <button
                type="button"
                className="secondary-button"
                onClick={() => setShowForm(false)}
              >
                Cancel
              </button>

            </div>

          </form>

        </div>
      )}

      {/* =========================
          CREATE LOGIN FORM
          ========================= */}

      {showLoginForm && selectedEmployee && (
        <div className="content-card login-form-card">

          <div className="card-header">
            <div>
              <h2>Create Employee Login</h2>
              <p>
                Create system access for this employee
              </p>
            </div>
          </div>

          <div className="employee-login-info">
            <span className="employee-login-icon">
              👤
            </span>

            <div>
              <strong>
                {selectedEmployee.firstName}{" "}
                {selectedEmployee.lastName}
              </strong>

              <span>
                {selectedEmployee.employeeCode}
                {" • "}
                {selectedEmployee.departmentName}
              </span>
            </div>
          </div>

          <form onSubmit={handleCreateLogin}>

            <div className="form-grid">

              <div className="form-group">
                <label>Employee</label>

                <input
                  type="text"
                  value={`${selectedEmployee.firstName} ${selectedEmployee.lastName} (${selectedEmployee.employeeCode})`}
                  disabled
                />
              </div>

              <div className="form-group">
                <label>Username</label>

                <input
                  type="text"
                  name="username"
                  value={loginData.username}
                  onChange={handleLoginChange}
                  placeholder="amit"
                  required
                />
              </div>

              <div className="form-group">
                <label>Login Email</label>

                <input
                  type="email"
                  name="email"
                  value={loginData.email}
                  onChange={handleLoginChange}
                  placeholder="amit@gmail.com"
                  required
                />
              </div>

              <div className="form-group">
                <label>Password</label>

                <input
                  type="password"
                  name="password"
                  value={loginData.password}
                  onChange={handleLoginChange}
                  placeholder="Enter password"
                  required
                  minLength="6"
                />
              </div>

            </div>

            <div className="form-actions">

              <button
                type="submit"
                className="success-button"
              >
                🔑 Create Login
              </button>

              <button
                type="button"
                className="secondary-button"
                onClick={closeLoginForm}
              >
                Cancel
              </button>

            </div>

          </form>

        </div>
      )}

      {/* =========================
          EMPLOYEE LIST
          ========================= */}

      <div className="content-card">

        <div className="card-header">

          <div>
            <h2>Employee List</h2>
            <p>
              View and manage all employees
            </p>
          </div>

          <span className="record-count">
            {employees.length} employee(s)
          </span>

        </div>

        {employees.length === 0 ? (

          <div className="empty-state">
            <div className="empty-state-icon">
              👥
            </div>

            <h3>No employees found</h3>

            <p>
              Create your first employee to get started.
            </p>
          </div>

        ) : (

          <div className="table-container">

            <table>

              <thead>
                <tr>
                  <th>ID</th>
                  <th>Employee Code</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Designation</th>
                  <th>Department</th>
                  <th>Salary</th>
                  <th>Status</th>
                  <th>Date of Joining</th>
                  <th>Login</th>
                </tr>
              </thead>

              <tbody>

                {employees.map((employee) => (

                  <tr key={employee.id}>

                    <td>
                      <span className="table-id">
                        #{employee.id}
                      </span>
                    </td>

                    <td>
                      <strong className="employee-code">
                        {employee.employeeCode}
                      </strong>
                    </td>

                    <td>
                      <div className="employee-name">
                        <div className="employee-avatar">
                          {employee.firstName
                            ?.charAt(0)
                            ?.toUpperCase()}
                        </div>

                        <span>
                          {employee.firstName}{" "}
                          {employee.lastName}
                        </span>
                      </div>
                    </td>

                    <td>
                      {employee.email}
                    </td>

                    <td>
                      {employee.phone}
                    </td>

                    <td>
                      {employee.designation}
                    </td>

                    <td>
                      <span className="department-badge">
                        {employee.departmentName}
                      </span>
                    </td>

                    <td>
                      <strong>
                        ₹
                        {Number(
                          employee.salary
                        ).toLocaleString("en-IN")}
                      </strong>
                    </td>

                    <td>
                      <span
                        className={`status-badge ${
                          employee.status === "ACTIVE"
                            ? "success"
                            : "status-cancelled"
                        }`}
                      >
                        {employee.status}
                      </span>
                    </td>

                    <td>
                      {employee.dateOfJoining}
                    </td>

                    <td>
                      <button
                        type="button"
                        className="view-button"
                        onClick={() =>
                          openLoginForm(employee)
                        }
                      >
                        🔑 Create Login
                      </button>
                    </td>

                  </tr>

                ))}

              </tbody>

            </table>

          </div>

        )}

      </div>

    </div>
  );
}

export default Employees;
