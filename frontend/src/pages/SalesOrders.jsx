import { useEffect, useState } from "react";
import api from "../services/api";

function SalesOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadOrders = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get(
        "/sales/orders"
      );

      setOrders(
        Array.isArray(response.data)
          ? response.data
          : response.data.content || []
      );
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to load sales orders"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOrders();
  }, []);

  const getStatusClass = (status) => {
    if (status === "DELIVERED") return "status-delivered";
    if (status === "CANCELLED") return "status-cancelled";
    if (status === "SHIPPED") return "status-shipped";
    if (status === "PROCESSING") return "status-processing";
    if (status === "CONFIRMED") return "status-confirmed";

    return "status-pending";
  };

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Sales Orders</h1>
          <p>Orders, payments and order status</p>
        </div>

        <button
          className="secondary-button"
          onClick={loadOrders}
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
            <h3>Sales Orders</h3>
            <p>
              {orders.length} order(s)
            </p>
          </div>
        </div>

        {loading ? (
          <div className="loading">
            Loading orders...
          </div>
        ) : orders.length === 0 ? (
          <div className="empty-state">
            No sales orders found.
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Order</th>
                  <th>Customer</th>
                  <th>Date</th>
                  <th>Total</th>
                  <th>Status</th>
                  <th>Payment</th>
                  <th>Items</th>
                </tr>
              </thead>

              <tbody>
                {orders.map((order) => (
                  <tr key={order.id}>
                    <td>
                      <strong>
                        {order.orderNumber}
                      </strong>
                    </td>

                    <td>
                      {order.customerName ||
                        order.customerId}
                    </td>

                    <td>
                      {order.orderDate
                        ? new Date(
                            order.orderDate
                          ).toLocaleDateString(
                            "en-IN"
                          )
                        : "-"}
                    </td>

                    <td>
                      ₹
                      {Number(
                        order.totalAmount || 0
                      ).toLocaleString("en-IN")}
                    </td>

                    <td>
                      <span
                        className={`status-badge ${getStatusClass(
                          order.status
                        )}`}
                      >
                        {order.status}
                      </span>
                    </td>

                    <td>
                      {order.paymentStatus}
                    </td>

                    <td>
                      {order.items?.length || 0}
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

export default SalesOrders;