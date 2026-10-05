import { useEffect, useState } from "react";
import api from "../services/api";

function HR() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadEmployees = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get(
        "/hr/employees"
      );

      setEmployees(
        Array.isArray(response.data)
          ? response.data
          : response.data.content || []
      );
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to load employees"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadEmployees();
  }, []);

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Human Resources</h1>
          <p>Employee management</p>
        </div>

        <button
          className="secondary-button"
          onClick={loadEmployees}
        >
          ↻ Refresh
        </button>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="dashboard-panel table-panel">
        <div className="panel-header">
          <div>
            <h3>Employees</h3>
            <p>
              {employees.length} employee(s)
            </p>
          </div>
        </div>

        {loading ? (
          <div className="loading">
            Loading employees...
          </div>
        ) : employees.length === 0 ? (
          <div className="empty-state">
            No employees found.
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Department</th>
                  <th>Position</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {employees.map((employee) => (
                  <tr key={employee.id}>
                    <td>{employee.id}</td>

                    <td>
                      <strong>
                        {employee.firstName ||
                          employee.name ||
                          "-"}{" "}
                        {employee.lastName || ""}
                      </strong>
                    </td>

                    <td>
                      {employee.email || "-"}
                    </td>

                    <td>
                      {employee.department?.name ||
                        employee.departmentName ||
                        "-"}
                    </td>

                    <td>
                      {employee.position ||
                        employee.jobTitle ||
                        "-"}
                    </td>

                    <td>
                      <span className="status-badge success">
                        {employee.status ||
                          "ACTIVE"}
                      </span>
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

export default HR;