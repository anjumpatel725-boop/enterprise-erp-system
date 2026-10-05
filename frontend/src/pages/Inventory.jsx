import { useEffect, useState } from "react";
import api from "../services/api";

function Inventory() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadProducts = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get(
        "/inventory/products"
      );

      setProducts(
        Array.isArray(response.data)
          ? response.data
          : response.data.content || []
      );
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to load products"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProducts();
  }, []);

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Inventory</h1>
          <p>Products and stock management</p>
        </div>

        <button
          className="secondary-button"
          onClick={loadProducts}
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
            <h3>Products</h3>
            <p>
              {products.length} product(s)
            </p>
          </div>
        </div>

        {loading ? (
          <div className="loading">
            Loading products...
          </div>
        ) : products.length === 0 ? (
          <div className="empty-state">
            No products found.
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Code</th>
                  <th>Product</th>
                  <th>Category</th>
                  <th>Price</th>
                  <th>Quantity</th>
                  <th>Reorder Level</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {products.map((product) => (
                  <tr key={product.id}>
                    <td>{product.id}</td>
                    <td>
                      {product.productCode}
                    </td>
                    <td>
                      <strong>{product.name}</strong>
                    </td>
                    <td>
                      {product.category?.name ||
                        product.categoryName ||
                        product.category ||
                        "-"}
                    </td>
                    <td>
                      ₹
                      {Number(
                        product.price || 0
                      ).toLocaleString("en-IN")}
                    </td>
                    <td>
                      <span
                        className={
                          product.quantity <=
                          product.reorderLevel
                            ? "stock-low"
                            : "stock-ok"
                        }
                      >
                        {product.quantity}
                      </span>
                    </td>
                    <td>
                      {product.reorderLevel}
                    </td>
                    <td>
                      <span className="status-badge success">
                        {product.status || "ACTIVE"}
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

export default Inventory;