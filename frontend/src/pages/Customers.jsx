import { useEffect, useState } from "react";
import api from "../services/api";

function Customers() {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadCustomers = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get(
        "/sales/customers"
      );

      setCustomers(
        Array.isArray(response.data)
          ? response.data
          : response.data.content || []
      );
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to load customers"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCustomers();
  }, []);

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Customers</h1>
          <p>Customer management</p>
        </div>

        <button
          className="secondary-button"
          onClick={loadCustomers}
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
            <h3>Customer List</h3>
            <p>
              {customers.length} customer(s)
            </p>
          </div>
        </div>

        {loading ? (
          <div className="loading">
            Loading customers...
          </div>
        ) : customers.length === 0 ? (
          <div className="empty-state">
            No customers found.
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Customer Code</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>City</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {customers.map((customer) => (
                  <tr key={customer.id}>
                    <td>{customer.id}</td>
                    <td>
                      {customer.customerCode}
                    </td>
                    <td>
                      <strong>{customer.name}</strong>
                    </td>
                    <td>{customer.email}</td>
                    <td>{customer.phone}</td>
                    <td>{customer.city}</td>
                    <td>
                      <span className="status-badge success">
                        {customer.status || "ACTIVE"}
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

export default Customers;