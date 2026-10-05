import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";

function Login() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await api.post("/auth/login", {
        username: username.trim(),
        password,
      });

      console.log("LOGIN RESPONSE:", response.data);

      const token =
        response.data?.token ||
        response.data?.accessToken ||
        response.data?.jwt;

      if (!token) {
        throw new Error("Token not received");
      }

      const loggedInUsername =
        response.data?.username || username.trim();

      const role =
        response.data?.role || "EMPLOYEE";

      const email =
        response.data?.email || "";

      // =========================
      // SAVE LOGIN INFORMATION
      // =========================

      localStorage.setItem("token", token);

      localStorage.setItem(
        "username",
        loggedInUsername
      );

      localStorage.setItem(
        "role",
        role
      );

      // IMPORTANT:
      // Save email so Navbar can display it
      localStorage.setItem(
        "email",
        email
      );

      console.log(
        "LOGIN USER:",
        loggedInUsername
      );

      console.log(
        "LOGIN ROLE:",
        role
      );

      console.log(
        "LOGIN EMAIL:",
        email
      );

      navigate("/");

    } catch (err) {
      console.error("LOGIN ERROR:", err);

      const data = err.response?.data;

      if (typeof data === "string") {
        setError(data);
      } else if (data?.message) {
        setError(data.message);
      } else {
        setError(
          "Invalid username or password"
        );
      }

    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">

      {/* LEFT */}
      <div className="login-left">

        <div className="login-brand">

          <div className="logo-icon large">
            E
          </div>

          <div>
            <h1>Enterprise ERP</h1>
            <p>
              Business Management Platform
            </p>
          </div>

        </div>

        <div className="login-description">

          <h2>
            Manage your enterprise
            efficiently.
          </h2>

          <p>
            A centralized platform for
            managing employees, inventory,
            customers, sales and reports.
          </p>

          <div className="feature-list">
            <div>✓ Secure Authentication</div>
            <div>✓ Enterprise Dashboard</div>
            <div>✓ Inventory Management</div>
            <div>✓ Sales Management</div>
          </div>

        </div>

      </div>

      {/* RIGHT */}
      <div className="login-right">

        <div className="login-card">

          <div className="mobile-logo">
            <div className="logo-icon">
              E
            </div>
          </div>

          <h2>Welcome Back</h2>

          <p className="login-subtitle">
            Sign in to Enterprise ERP
          </p>

          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          <form onSubmit={handleLogin}>

            <label>
              Username
            </label>

            <input
              type="text"
              placeholder="Enter username"
              value={username}
              onChange={(e) =>
                setUsername(e.target.value)
              }
              required
            />

            <label>
              Password
            </label>

            <input
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) =>
                setPassword(e.target.value)
              }
              required
            />

            <button
              type="submit"
              className="login-button"
              disabled={loading}
            >
              {loading
                ? "Signing In..."
                : "Sign In"}
            </button>

          </form>

          <div className="register-link">
            Don't have an account?{" "}
            <Link to="/register">
              Create Account
            </Link>
          </div>

          <div className="login-footer">
            Secured with JWT authentication
          </div>

        </div>

      </div>

    </div>
  );
}

export default Login;