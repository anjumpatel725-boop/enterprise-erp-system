import { useEffect, useState } from "react";
import api from "../services/api";

function Leaves() {
  const role = localStorage.getItem("role");

  const [leaves, setLeaves] = useState([]);
  const [employees, setEmployees] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [showForm, setShowForm] = useState(false);

  const [formData, setFormData] = useState({
    employeeId: "",
    leaveType: "CASUAL",
    startDate: "",
    endDate: "",
    reason: "",
    status: "PENDING",
  });

  // ================= LOAD LEAVES =================

  const loadLeaves = async () => {
    try {
      setLoading(true);
      setError("");

      let response;

      if (role === "EMPLOYEE") {
        response = await api.get("/employee/leaves");
      } else {
        response = await api.get("/hr/leaves");
      }

      setLeaves(response.data);
    } catch (err) {
      console.error("Leave loading error:", err);

      setError(
        err.response?.data?.message ||
          "Failed to load leaves"
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
      const response =
        await api.get("/hr/employees");

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
    loadLeaves();
    loadEmployees();
  }, []);

  // ================= FORM CHANGE =================

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  // ================= CREATE / APPLY LEAVE =================

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setError("");
      setSuccess("");

      if (role === "EMPLOYEE") {

        // Employee does NOT send employeeId or status.
        // Backend gets employee from JWT.

        const payload = {
          leaveType: formData.leaveType,
          startDate: formData.startDate,
          endDate: formData.endDate,
          reason: formData.reason,
        };

        await api.post(
          "/employee/leaves",
          payload
        );

        setSuccess(
          "Leave request submitted successfully"
        );

      } else {

        // HR/Admin can create leave for any employee

        const payload = {
          employeeId: Number(
            formData.employeeId
          ),
          leaveType: formData.leaveType,
          startDate: formData.startDate,
          endDate: formData.endDate,
          reason: formData.reason,
          status: formData.status,
        };

        await api.post(
          "/hr/leaves",
          payload
        );

        setSuccess(
          "Leave created successfully"
        );
      }

      setFormData({
        employeeId: "",
        leaveType: "CASUAL",
        startDate: "",
        endDate: "",
        reason: "",
        status: "PENDING",
      });

      setShowForm(false);

      await loadLeaves();

    } catch (err) {
      console.error("Create leave error:", err);

      setError(
        err.response?.data?.message ||
          "Failed to create leave"
      );
    }
  };

  // ================= UPDATE STATUS =================

  const updateLeaveStatus = async (
    leave,
    status
  ) => {
    try {
      setError("");
      setSuccess("");

      const payload = {
        employeeId: leave.employeeId,
        leaveType: leave.leaveType,
        startDate: leave.startDate,
        endDate: leave.endDate,
        reason: leave.reason,
        status: status,
      };

      await api.put(
        `/hr/leaves/${leave.id}`,
        payload
      );

      setSuccess(
        `Leave ${status.toLowerCase()} successfully`
      );

      await loadLeaves();

    } catch (err) {
      console.error("Update leave error:", err);

      setError(
        err.response?.data?.message ||
          "Failed to update leave"
      );
    }
  };

  // ================= DELETE LEAVE =================

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this leave?"
    );

    if (!confirmDelete) {
      return;
    }

    try {
      setError("");
      setSuccess("");

      await api.delete(
        `/hr/leaves/${id}`
      );

      setSuccess(
        "Leave deleted successfully"
      );

      await loadLeaves();

    } catch (err) {
      console.error("Delete leave error:", err);

      setError(
        err.response?.data?.message ||
          "Failed to delete leave"
      );
    }
  };

  // ================= LOADING =================

  if (loading) {
    return (
      <div className="page-container">
        <h1>Leaves</h1>
        <p>Loading leaves...</p>
      </div>
    );
  }

  // ================= UI =================

  return (
    <div className="page-container">

      {/* ================= HEADER ================= */}

      <div className="page-header">

        <div>
          <h1>Leaves</h1>

          <p>
            {role === "EMPLOYEE"
              ? "View and manage your leave requests"
              : "Manage employee leave requests"}
          </p>
        </div>

        <button
          className="primary-button"
          onClick={() =>
            setShowForm(!showForm)
          }
        >
          {showForm
            ? "Cancel"
            : "+ Apply Leave"}
        </button>

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

      {/* ================= CREATE LEAVE FORM ================= */}

      {showForm && (
        <div className="content-card leave-form-card">

          <div className="card-header">
            <div>
              <h2>
                {role === "EMPLOYEE"
                  ? "Apply for Leave"
                  : "Create Leave"}
              </h2>

              <p>
                {role === "EMPLOYEE"
                  ? "Submit a new leave request"
                  : "Create a leave request for an employee"}
              </p>
            </div>
          </div>

          <form onSubmit={handleSubmit}>

            <div className="form-grid">

              {/* ================= EMPLOYEE ================= */}

              {role !== "EMPLOYEE" && (
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
              )}

              {/* ================= LEAVE TYPE ================= */}

              <div className="form-group">

                <label>Leave Type</label>

                <select
                  name="leaveType"
                  value={formData.leaveType}
                  onChange={handleChange}
                  required
                >

                  <option value="CASUAL">
                    CASUAL
                  </option>

                  <option value="SICK">
                    SICK
                  </option>

                  <option value="EARNED">
                    EARNED
                  </option>

                  <option value="UNPAID">
                    UNPAID
                  </option>

                </select>

              </div>

              {/* ================= START DATE ================= */}

              <div className="form-group">

                <label>Start Date</label>

                <input
                  type="date"
                  name="startDate"
                  value={formData.startDate}
                  onChange={handleChange}
                  required
                />

              </div>

              {/* ================= END DATE ================= */}

              <div className="form-group">

                <label>End Date</label>

                <input
                  type="date"
                  name="endDate"
                  value={formData.endDate}
                  onChange={handleChange}
                  required
                />

              </div>

              {/* ================= REASON ================= */}

              <div className="form-group form-group-full">

                <label>Reason</label>

                <textarea
                  name="reason"
                  value={formData.reason}
                  onChange={handleChange}
                  maxLength={500}
                  rows="3"
                  placeholder="Enter reason"
                />

              </div>

              {/* ================= STATUS ================= */}

              {role !== "EMPLOYEE" && (
                <div className="form-group">

                  <label>Status</label>

                  <select
                    name="status"
                    value={formData.status}
                    onChange={handleChange}
                    required
                  >

                    <option value="PENDING">
                      PENDING
                    </option>

                    <option value="APPROVED">
                      APPROVED
                    </option>

                    <option value="REJECTED">
                      REJECTED
                    </option>

                  </select>

                </div>
              )}

            </div>

            {/* ================= SUBMIT ================= */}

            <div className="form-actions">

              <button
                type="submit"
                className="primary-button"
              >
                {role === "EMPLOYEE"
                  ? "Submit Leave Request"
                  : "Save Leave"}
              </button>

              <button
                type="button"
                className="secondary-button"
                onClick={() =>
                  setShowForm(false)
                }
              >
                Cancel
              </button>

            </div>

          </form>

        </div>
      )}

      {/* ================= LEAVE TABLE ================= */}

      <div className="table-panel">

        <div className="table-wrapper">

          <table>

            <thead>

              <tr>

                {role !== "EMPLOYEE" && (
                  <th>Employee</th>
                )}

                <th>Leave Type</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Reason</th>
                <th>Status</th>

                {role !== "EMPLOYEE" && (
                  <th>Action</th>
                )}

              </tr>

            </thead>

            <tbody>

              {leaves.length === 0 ? (

                <tr>

                  <td
                    colSpan={
                      role === "EMPLOYEE"
                        ? 6
                        : 7
                    }
                    className="empty-table"
                  >
                    No leave records found
                  </td>

                </tr>

              ) : (

                leaves.map((leave) => (

                  <tr key={leave.id}>

                    {/* Employee */}

                    {role !== "EMPLOYEE" && (
                      <td>

                        <div className="employee-table-info">

                          <strong>
                            {leave.employeeCode}
                          </strong>

                          <span>
                            {leave.employeeName}
                          </span>

                        </div>

                      </td>
                    )}

                    {/* Leave Type */}

                    <td>
                      {leave.leaveType}
                    </td>

                    {/* Start Date */}

                    <td>
                      {leave.startDate}
                    </td>

                    {/* End Date */}

                    <td>
                      {leave.endDate}
                    </td>

                    {/* Reason */}

                    <td>
                      {leave.reason || "-"}
                    </td>

                    {/* Status */}

                    <td>

                      <span
                        className={`status-badge ${
                          leave.status === "APPROVED"
                            ? "status-success"
                            : leave.status === "REJECTED"
                            ? "status-danger"
                            : "status-warning"
                        }`}
                      >
                        {leave.status}
                      </span>

                    </td>

                    {/* Actions */}

                    {role !== "EMPLOYEE" && (
                      <td>

                        <div className="table-actions">

                          {/* Approve */}

                          {leave.status === "PENDING" && (
                            <>
                              <button
                                className="success-button small-button"
                                onClick={() =>
                                  updateLeaveStatus(
                                    leave,
                                    "APPROVED"
                                  )
                                }
                              >
                                Approve
                              </button>

                              <button
                                className="warning-button small-button"
                                onClick={() =>
                                  updateLeaveStatus(
                                    leave,
                                    "REJECTED"
                                  )
                                }
                              >
                                Reject
                              </button>
                            </>
                          )}

                          {/* Delete */}

                          <button
                            className="danger-button small-button"
                            onClick={() =>
                              handleDelete(leave.id)
                            }
                          >
                            Delete
                          </button>

                        </div>

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

export default Leaves;