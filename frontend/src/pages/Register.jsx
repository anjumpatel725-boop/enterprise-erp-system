import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";

function Register() {

  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

 
  const [role, setRole] = useState("ADMIN");

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");


  const handleRegister = async (e) => {

    e.preventDefault();

    setError("");
    setMessage("");
    setLoading(true);

    try {

      const response = await api.post(
        "/auth/register",
        {
          username: username.trim(),
          email: email.trim(),
          password,
          role,
        }
      );

      setMessage(
        response.data?.message ||
        "Registration successful! Redirecting to login..."
      );

      // Clear form
      setUsername("");
      setEmail("");
      setPassword("");
      setRole("ADMIN");


      setTimeout(() => {
        navigate("/login");
      }, 1200);


    } catch (err) {

      console.error(
        "REGISTER ERROR:",
        err
      );

      const data =
        err.response?.data;


      if (typeof data === "string") {

        setError(data);

      } else if (data?.message) {

        setError(data.message);

      } else if (data?.errors) {

        setError(
          Object.values(data.errors).join(", ")
        );

      } else {

        setError(
          "Registration failed. Please try again."
        );

      }

    } finally {

      setLoading(false);

    }
  };


  return (

    <div className="login-page">

      {/* LEFT SIDE */}

      <div className="login-left">

        <div className="login-brand">

          <div className="logo-icon large">
            E
          </div>

          <div>

            <h1>
              Enterprise ERP
            </h1>

            <p>
              Business Management Platform
            </p>

          </div>

        </div>


        <div className="login-description">

          <h2>
            Create your ERP account.
          </h2>

          <p>
            Register and manage enterprise
            business operations from a
            centralized platform.
          </p>


          <div className="feature-list">

            <div>
              ✓ Secure Authentication
            </div>

            <div>
              ✓ Enterprise Dashboard
            </div>

            <div>
              ✓ Business Management
            </div>

            <div>
              ✓ Secure JWT Login
            </div>

            <div>
              ✓ Role-Based Access Control
            </div>

          </div>

        </div>

      </div>


      {/* RIGHT SIDE */}

      <div className="login-right">

        <div className="login-card">

          <div className="mobile-logo">

            <div className="logo-icon">
              E
            </div>

          </div>


          <h2>
            Create Account
          </h2>

          <p className="login-subtitle">
            Register for Enterprise ERP
          </p>


          {/* ERROR */}

          {error && (

            <div className="error-message">
              {error}
            </div>

          )}


          {/* SUCCESS */}

          {message && (

            <div className="success-message">
              {message}
            </div>

          )}


          <form
            onSubmit={handleRegister}
          >

            {/* USERNAME */}

            <label htmlFor="username">
              Username
            </label>

            <input
              id="username"
              type="text"
              placeholder="Enter username"
              value={username}
              onChange={(e) =>
                setUsername(e.target.value)
              }
              minLength={3}
              maxLength={50}
              required
            />


            {/* EMAIL */}

            <label htmlFor="email">
              Email
            </label>

            <input
              id="email"
              type="email"
              placeholder="Enter email"
              value={email}
              onChange={(e) =>
                setEmail(e.target.value)
              }
              required
            />


            {/* PASSWORD */}

            <label htmlFor="password">
              Password
            </label>

            <input
              id="password"
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) =>
                setPassword(e.target.value)
              }
              minLength={6}
              maxLength={100}
              required
            />


            {/* ROLE */}

            <label htmlFor="role">
              Role
            </label>

            <select
              id="role"
              value={role}
              onChange={(e) =>
                setRole(e.target.value)
              }
              required
            >

              <option value="ADMIN">
                Admin
              </option>

              <option value="HR_ADMIN">
                HR Admin
              </option>

              <option value="SALES_MANAGER">
                Sales Manager
              </option>

              <option value="ACCOUNTANT">
                Accountant
              </option>

              <option value="INVENTORY_MANAGER">
                Inventory Manager
              </option>

            </select>


            {/* REGISTER BUTTON */}

            <button
              type="submit"
              className="login-button"
              disabled={loading}
            >

              {loading
                ? "Creating Account..."
                : "Create Account"}

            </button>

          </form>


          {/* LOGIN LINK */}

          <div className="register-link">

            Already have an account?{" "}

            <Link to="/login">
              Sign In
            </Link>

          </div>


          {/* EMPLOYEE INFORMATION */}

          <div className="login-footer">

            Employee accounts are created by HR
            through the Employee Management module.

          </div>

        </div>

      </div>

    </div>
  );
}

export default Register;