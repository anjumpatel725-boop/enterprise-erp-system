import { useEffect, useState } from "react";
import api from "../services/api";

function Reports() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadReport = async () => {
    try {
      setLoading(true);

      const response =
        await api.get("/reports/dashboard");

      setData(response.data);
    } catch (error) {
      console.error(error);
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
        Loading reports...
      </div>
    );
  }

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Reports & Analytics</h1>
          <p>Enterprise business insights</p>
        </div>

        <button
          className="secondary-button"
          onClick={loadReport}
        >
          ↻ Refresh
        </button>
      </div>

      {data && (
        <>
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon">₹</div>

              <div className="stat-value">
                ₹
                {Number(
                  data.totalSales || 0
                ).toLocaleString("en-IN")}
              </div>

              <div className="stat-title">
                Total Sales
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon">▤</div>

              <div className="stat-value">
                {data.totalOrders}
              </div>

              <div className="stat-title">
                Total Orders
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon">▣</div>

              <div className="stat-value">
                {data.totalProducts}
              </div>

              <div className="stat-title">
                Products
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon">⚠</div>

              <div className="stat-value">
                {data.lowStockProducts}
              </div>

              <div className="stat-title">
                Low Stock
              </div>
            </div>
          </div>

          <div className="dashboard-panel">
            <div className="panel-header">
              <div>
                <h3>Enterprise Analytics</h3>
                <p>
                  Current business performance
                </p>
              </div>
            </div>

            <div className="report-bars">
              <div className="report-row">
                <div>
                  <span>Employees</span>
                  <strong>
                    {data.totalEmployees}
                  </strong>
                </div>

                <div className="progress">
                  <div
                    style={{
                      width: `${Math.min(
                        data.totalEmployees * 10,
                        100
                      )}%`,
                    }}
                  />
                </div>
              </div>

              <div className="report-row">
                <div>
                  <span>Products</span>
                  <strong>
                    {data.totalProducts}
                  </strong>
                </div>

                <div className="progress">
                  <div
                    style={{
                      width: `${Math.min(
                        data.totalProducts * 10,
                        100
                      )}%`,
                    }}
                  />
                </div>
              </div>

              <div className="report-row">
                <div>
                  <span>Customers</span>
                  <strong>
                    {data.totalCustomers}
                  </strong>
                </div>

                <div className="progress">
                  <div
                    style={{
                      width: `${Math.min(
                        data.totalCustomers * 10,
                        100
                      )}%`,
                    }}
                  />
                </div>
              </div>

              <div className="report-row">
                <div>
                  <span>Orders</span>
                  <strong>
                    {data.totalOrders}
                  </strong>
                </div>

                <div className="progress">
                  <div
                    style={{
                      width: `${Math.min(
                        data.totalOrders * 10,
                        100
                      )}%`,
                    }}
                  />
                </div>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default Reports;