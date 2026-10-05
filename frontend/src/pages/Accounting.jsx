import { useEffect, useState } from "react";
import api from "../services/api";

function Accounting() {
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadReport = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get(
        "/accounting/reports/summary"
      );

      setReport(response.data);
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to load accounting report"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadReport();
  }, []);

  if (loading) {
    return (
      <div className="loading">
        Loading accounting...
      </div>
    );
  }

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Accounting</h1>
          <p>Financial summary</p>
        </div>

        <button
          className="secondary-button"
          onClick={loadReport}
        >
          ↻ Refresh
        </button>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      {report && (
        <div className="stats-grid accounting-grid">
          <div className="stat-card">
            <div className="stat-icon">↑</div>

            <div className="stat-value">
              ₹
              {Number(
                report.totalCredit || 0
              ).toLocaleString("en-IN")}
            </div>

            <div className="stat-title">
              Total Credit
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">↓</div>

            <div className="stat-value">
              ₹
              {Number(
                report.totalDebit || 0
              ).toLocaleString("en-IN")}
            </div>

            <div className="stat-title">
              Total Debit
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">₹</div>

            <div className="stat-value">
              ₹
              {Number(
                report.netBalance || 0
              ).toLocaleString("en-IN")}
            </div>

            <div className="stat-title">
              Net Balance
            </div>
          </div>
        </div>
      )}

      <div className="dashboard-panel">
        <div className="panel-header">
          <div>
            <h3>Accounting Summary</h3>
            <p>
              Current financial position
            </p>
          </div>
        </div>

        {report && (
          <div className="overview-list">
            <div>
              <span>Total Credit</span>
              <strong>
                ₹
                {Number(
                  report.totalCredit || 0
                ).toLocaleString("en-IN")}
              </strong>
            </div>

            <div>
              <span>Total Debit</span>
              <strong>
                ₹
                {Number(
                  report.totalDebit || 0
                ).toLocaleString("en-IN")}
              </strong>
            </div>

            <div>
              <span>Net Balance</span>
              <strong>
                ₹
                {Number(
                  report.netBalance || 0
                ).toLocaleString("en-IN")}
              </strong>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Accounting;