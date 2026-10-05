import { useEffect, useState } from "react";
import api from "../services/api";

function Attendance() {
  const role = localStorage.getItem("role");

  const [attendance, setAttendance] = useState([]);
  const [employees, setEmployees] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [showForm, setShowForm] = useState(false);

  const [formData, setFormData] = useState({
    employeeId: "",
    attendanceDate: "",
    status: "PRESENT",
    remarks: "",
  });

  // ================= LOAD ATTENDANCE =================

  const loadAttendance = async () => {
    try {
      setLoading(true);
      setError("");

      let response;

      if (role === "EMPLOYEE") {
        response = await api.get("/employee/attendance");
      } else {
        response = await api.get("/hr/attendance");
      }

      setAttendance(response.data);
    } catch (err) {
      console.error("Attendance loading error:", err);

      setError(
        err.response?.data?.message ||
          "Failed to load attendance"
      );
    } finally {
      setLoading(false);
    }
  };

  // ================= LOAD EMPLOYEES =================

  const loadEmployees = async () => {
    if (role === "EMPLOYEE") {
      return;
    }

    try {
      const response = await api.get("/hr/employees");

      setEmployees(response.data);
    } catch (err) {
      console.error("Employee loading error:", err);

      setError(
        err.response?.data?.message ||
          "Failed to load employees"
      );
    }
  };

  // ================= INITIAL LOAD =================

  useEffect(() => {
    loadAttendance();
    loadEmployees();
  }, []);

  // ================= FORM CHANGE =================

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  // ================= CREATE ATTENDANCE =================

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setError("");
      setSuccess("");

      const payload = {
        employeeId: Number(formData.employeeId),
        attendanceDate: formData.attendanceDate,
        status: formData.status,
        remarks: formData.remarks,
      };

      await api.post("/hr/attendance", payload);

      setSuccess("Attendance marked successfully");

      setFormData({
        employeeId: "",
        attendanceDate: "",
        status: "PRESENT",
        remarks: "",
      });

      setShowForm(false);

      await loadAttendance();
    } catch (err) {
      console.error("Create attendance error:", err);

      setError(
        err.response?.data?.message ||
          "Failed to mark attendance"
      );
    }
  };

  // ================= DELETE ATTENDANCE =================

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this attendance record?"
    );

    if (!confirmDelete) {
      return;
    }

    try {
      setError("");
      setSuccess("");

      await api.delete(`/hr/attendance/${id}`);

      setSuccess("Attendance deleted successfully");

      await loadAttendance();
    } catch (err) {
      console.error("Delete attendance error:", err);

      setError(
        err.response?.data?.message ||
          "Failed to delete attendance"
      );
    }
  };

  // ================= LOADING =================

  if (loading) {
    return (
      <div className="page-container">
        <h1>Attendance</h1>
        <p>Loading attendance...</p>
      </div>
    );
  }

  // ================= UI =================

  return (
    <div className="page-container">

      {/* ================= HEADER ================= */}

      <div className="page-header">
        <div>
          <h1>Attendance</h1>

          <p>
            {role === "EMPLOYEE"
              ? "View your attendance records"
              : "Manage employee attendance"}
          </p>
        </div>

        {role !== "EMPLOYEE" && (
          <button
            className="primary-button"
            onClick={() => setShowForm(!showForm)}
          >
            {showForm
              ? "Cancel"
              : "+ Mark Attendance"}
          </button>
        )}
      </div>

      {/* ================= MESSAGES ================= */}

      {error && (
        <div className="message error-message">
          {error}
        </div>
      )}

      {success && (
        <div className="message success-message">
          {success}
        </div>
      )}

      {/* ================= CREATE ATTENDANCE FORM ================= */}

      {showForm && role !== "EMPLOYEE" && (
        <div className="content-card attendance-form-card">

          <div className="card-header">
            <div>
              <h2>Mark Attendance</h2>
              <p>
                Record attendance for an employee
              </p>
            </div>
          </div>

          <form onSubmit={handleSubmit}>

            <div className="form-grid">

              {/* Employee */}

              <div className="form-group">
                <label>Employee</label>

                <select
                  name="employeeId"
                  value={formData.employeeId}
                  onChange={handleChange}
                  required
                >
                  <option value="">
                    Select Employee
                  </option>

                  {employees.map((employee) => (
                    <option
                      key={employee.id}
                      value={employee.id}
                    >
                      {employee.employeeCode} -{" "}
                      {employee.firstName}{" "}
                      {employee.lastName}
                    </option>
                  ))}
                </select>
              </div>

              {/* Date */}

              <div className="form-group">
                <label>Attendance Date</label>

                <input
                  type="date"
                  name="attendanceDate"
                  value={formData.attendanceDate}
                  onChange={handleChange}
                  required
                />
              </div>

              {/* Status */}

              <div className="form-group">
                <label>Status</label>

                <select
                  name="status"
                  value={formData.status}
                  onChange={handleChange}
                  required
                >
                  <option value="PRESENT">
                    PRESENT
                  </option>

                  <option value="ABSENT">
                    ABSENT
                  </option>

                  <option value="HALF_DAY">
                    HALF DAY
                  </option>

                  <option value="LEAVE">
                    LEAVE
                  </option>
                </select>
              </div>

              {/* Remarks */}

              <div className="form-group form-group-full">
                <label>Remarks</label>

                <textarea
                  name="remarks"
                  value={formData.remarks}
                  onChange={handleChange}
                  maxLength={500}
                  placeholder="Enter remarks"
                  rows="3"
                />
              </div>

            </div>

            <div className="form-actions">

              <button
                type="submit"
                className="primary-button"
              >
                Save Attendance
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

      {/* ================= ATTENDANCE TABLE ================= */}

      <div className="table-panel">

        <div className="table-wrapper">

          <table>

            <thead>
              <tr>

                {role !== "EMPLOYEE" && (
                  <th>Employee</th>
                )}

                <th>Date</th>
                <th>Status</th>
                <th>Remarks</th>

                {role !== "EMPLOYEE" && (
                  <th>Action</th>
                )}

              </tr>
            </thead>

            <tbody>

              {attendance.length === 0 ? (
                <tr>
                  <td
                    colSpan={
                      role === "EMPLOYEE"
                        ? 3
                        : 5
                    }
                    className="empty-table"
                  >
                    No attendance records found
                  </td>
                </tr>
              ) : (
                attendance.map((record) => (
                  <tr key={record.id}>

                    {/* Employee */}

                    {role !== "EMPLOYEE" && (
                      <td>
                        <div className="employee-table-info">
                          <strong>
                            {record.employeeCode}
                          </strong>

                          <span>
                            {record.employeeName}
                          </span>
                        </div>
                      </td>
                    )}

                    {/* Date */}

                    <td>
                      {record.attendanceDate}
                    </td>

                    {/* Status */}

                    <td>
                      <span
                        className={`status-badge ${
                          record.status === "PRESENT"
                            ? "status-success"
                            : record.status === "ABSENT"
                            ? "status-danger"
                            : record.status === "HALF_DAY"
                            ? "status-warning"
                            : "status-info"
                        }`}
                      >
                        {record.status}
                      </span>
                    </td>

                    {/* Remarks */}

                    <td>
                      {record.remarks || "-"}
                    </td>

                    {/* Action */}

                    {role !== "EMPLOYEE" && (
                      <td>
                        <button
                          className="danger-button small-button"
                          onClick={() =>
                            handleDelete(record.id)
                          }
                        >
                          Delete
                        </button>
                      </td>
                    )}

                  </tr>
                ))
              )}

            </tbody>

          </table>

        </div>
      </div>

    </div>
  );
}

export default Attendance;