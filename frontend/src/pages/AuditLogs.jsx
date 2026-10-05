import { useEffect, useState } from "react";
import api from "../services/api";

function AuditLogs() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadLogs = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get(
        "/audit/paged?page=0&size=50"
      );

      setLogs(response.data.content || []);
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to load audit logs"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadLogs();
  }, []);

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Audit Logs</h1>
          <p>System activity tracking</p>
        </div>

        <button
          className="secondary-button"
          onClick={loadLogs}
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
            <h3>System Activities</h3>
            <p>
              {logs.length} activity record(s)
            </p>
          </div>
        </div>

        {loading ? (
          <div className="loading">
            Loading audit logs...
          </div>
        ) : logs.length === 0 ? (
          <div className="empty-state">
            No audit logs found.
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>User</th>
                  <th>Action</th>
                  <th>Module</th>
                  <th>Entity</th>
                  <th>Description</th>
                  <th>Date</th>
                </tr>
              </thead>

              <tbody>
                {logs.map((log) => (
                  <tr key={log.id}>
                    <td>{log.id}</td>

                    <td>
                      <strong>
                        {log.username}
                      </strong>
                    </td>

                    <td>
                      <span className="action-badge">
                        {log.action}
                      </span>
                    </td>

                    <td>{log.module}</td>

                    <td>
                      {log.entityType}
                      {log.entityId
                        ? ` #${log.entityId}`
                        : ""}
                    </td>

                    <td>{log.description}</td>

                    <td>
                      {log.createdAt
                        ? new Date(
                            log.createdAt
                          ).toLocaleString(
                            "en-IN"
                          )
                        : "-"}
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

export default AuditLogs;