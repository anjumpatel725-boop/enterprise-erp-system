import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Dashboard() {
  const navigate = useNavigate();

  const [data, setData] = useState(null);
  const [employee, setEmployee] = useState(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const role = localStorage.getItem("role");

  // =====================================================
  // EMPLOYEE DASHBOARD
  // =====================================================

  const loadEmployeeProfile = async () => {
    try {
      setLoading(true);
      setError("");

      const response =
        await api.get("/employee/profile");

      console.log(
        "EMPLOYEE PROFILE:",
        response.data
      );

      setEmployee(response.data);

    } catch (err) {
      console.error(
        "EMPLOYEE PROFILE ERROR:",
        err
      );

      if (err.response?.status === 401) {
        setError(
          "Session expired. Please login again."
        );
      } else if (err.response?.status === 403) {
        setError(
          "Access denied. Employee dashboard permission is missing."
        );
      } else {
        setError(
          err.response?.data?.message ||
            "Unable to load employee profile"
        );
      }
    } finally {
      setLoading(false);
    }
  };

  // =====================================================
  // NORMAL ERP DASHBOARD
  // =====================================================

  const loadDashboard = async () => {
    try {
      setLoading(true);
      setError("");

      const response =
        await api.get("/reports/dashboard");

      console.log(
        "DASHBOARD RESPONSE:",
        response.data
      );

      setData(response.data);

    } catch (err) {
      console.error(
        "DASHBOARD ERROR:",
        err
      );

      if (err.response?.status === 401) {
        setError(
          "Session expired. Please login again."
        );
      } else if (err.response?.status === 403) {
        setError(
          "Access denied. Your account does not have permission to view the dashboard."
        );
      } else {
        setError(
          err.response?.data?.message ||
            "Unable to load dashboard"
        );
      }
    } finally {
      setLoading(false);
    }
  };

  // =====================================================
  // LOAD DASHBOARD ACCORDING TO ROLE
  // =====================================================

  useEffect(() => {
    if (role === "EMPLOYEE") {
      loadEmployeeProfile();
    } else {
      loadDashboard();
    }
  }, [role]);

  // =====================================================
  // LOADING
  // =====================================================

  if (loading) {
    return (
      <div className="loading">
        Loading dashboard...
      </div>
    );
  }

  // =====================================================
  // EMPLOYEE DASHBOARD
  // =====================================================

  if (role === "EMPLOYEE") {

    if (!employee) {
      return (
        <div>

          <div className="page-heading">
            <div>
              <h1>
                Employee Dashboard
              </h1>

              <p>
                My employee information
              </p>
            </div>

            <button
              className="secondary-button"
              onClick={loadEmployeeProfile}
            >
              ↻ Refresh
            </button>
          </div>

          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

        </div>
      );
    }

    return (
      <div>

        {/* ================= HEADER ================= */}

        <div className="page-heading">

          <div>
            <h1>
              Welcome, {employee.firstName} 👋
            </h1>

            <p>
              Employee Dashboard
            </p>
          </div>

          <button
            className="secondary-button"
            onClick={loadEmployeeProfile}
          >
            ↻ Refresh
          </button>

        </div>

        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        {/* ================= PROFILE CARDS ================= */}

        <div className="stats-grid">

          <div className="stat-card">

            <div className="stat-icon">
              👤
            </div>

            <div className="stat-value">
              {employee.employeeCode}
            </div>

            <div className="stat-title">
              Employee Code
            </div>

          </div>

          <div className="stat-card">

            <div className="stat-icon">
              💼
            </div>

            <div className="stat-value">
              {employee.designation || "N/A"}
            </div>

            <div className="stat-title">
              Designation
            </div>

          </div>

          <div className="stat-card">

            <div className="stat-icon">
              🏢
            </div>

            <div className="stat-value">
              {employee.departmentName || "N/A"}
            </div>

            <div className="stat-title">
              Department
            </div>

          </div>

          <div className="stat-card">

            <div className="stat-icon">
              ●
            </div>

            <div className="stat-value">
              {employee.status || "N/A"}
            </div>

            <div className="stat-title">
              Employment Status
            </div>

          </div>

        </div>

        {/* ================= EMPLOYEE PROFILE ================= */}

        <div className="dashboard-grid">

          <div className="dashboard-panel">

            <div className="panel-header">

              <div>
                <h3>
                  My Profile
                </h3>

                <p>
                  Your employee information
                </p>
              </div>

            </div>

            <div className="overview-list">

              <div>
                <span>
                  Full Name
                </span>

                <strong>
                  {employee.firstName}{" "}
                  {employee.lastName}
                </strong>
              </div>

              <div>
                <span>
                  Employee Code
                </span>

                <strong>
                  {employee.employeeCode}
                </strong>
              </div>

              <div>
                <span>
                  Email
                </span>

                <strong>
                  {employee.email}
                </strong>
              </div>

              <div>
                <span>
                  Phone
                </span>

                <strong>
                  {employee.phone || "N/A"}
                </strong>
              </div>

              <div>
                <span>
                  Designation
                </span>

                <strong>
                  {employee.designation || "N/A"}
                </strong>
              </div>

              <div>
                <span>
                  Department
                </span>

                <strong>
                  {employee.departmentName || "N/A"}
                </strong>
              </div>

            </div>

          </div>

          {/* ================= EMPLOYMENT DETAILS ================= */}

          <div className="dashboard-panel">

            <div className="panel-header">

              <div>
                <h3>
                  Employment Details
                </h3>

                <p>
                  Your current employment information
                </p>
              </div>

            </div>

            <div className="overview-list">

              <div>
                <span>
                  Date of Joining
                </span>

                <strong>
                  {employee.dateOfJoining || "N/A"}
                </strong>
              </div>

              <div>
                <span>
                  Status
                </span>

                <strong>
                  {employee.status || "N/A"}
                </strong>
              </div>

              <div>
                <span>
                  Department ID
                </span>

                <strong>
                  {employee.departmentId || "N/A"}
                </strong>
              </div>

              <div>
                <span>
                  Employee ID
                </span>

                <strong>
                  {employee.id}
                </strong>
              </div>

            </div>

          </div>

        </div>

        {/* ================= QUICK ACTIONS ================= */}

        <div className="dashboard-grid">

          <div
            className="dashboard-panel dashboard-stat-card"
            onClick={() =>
              navigate("/attendance")
            }
            role="button"
            tabIndex={0}
          >

            <div className="panel-header">

              <div>
                <h3>
                  🕐 My Attendance
                </h3>

                <p>
                  View your attendance records
                </p>
              </div>

              <strong>
                →
              </strong>

            </div>

          </div>

          <div
            className="dashboard-panel dashboard-stat-card"
            onClick={() =>
              navigate("/leaves")
            }
            role="button"
            tabIndex={0}
          >

            <div className="panel-header">

              <div>
                <h3>
                  📝 My Leaves
                </h3>

                <p>
                  Apply and view your leave requests
                </p>
              </div>

              <strong>
                →
              </strong>

            </div>

          </div>

        </div>

      </div>
    );
  }

  // =====================================================
  // NORMAL ERP DASHBOARD DATA
  // =====================================================

  const dashboard = data || {
    totalEmployees: 0,
    totalProducts: 0,
    totalCustomers: 0,
    totalOrders: 0,
    totalSales: 0,
    pendingOrders: 0,
    lowStockProducts: 0,
  };

  // =====================================================
  // ROLE-WISE CARDS
  // =====================================================

  const allCards = [

    {
      title: "Employees",
      value: dashboard.totalEmployees,
      icon: "♙",
      path: "/employees",
      description: "Manage employees",
      roles: [
        "ADMIN",
        "HR_ADMIN",
      ],
    },

    {
      title: "Products",
      value: dashboard.totalProducts,
      icon: "▣",
      path: "/inventory",
      description: "Manage inventory",
      roles: [
        "ADMIN",
        "SALES_MANAGER",
        "INVENTORY_MANAGER",
      ],
    },

    {
      title: "Customers",
      value: dashboard.totalCustomers,
      icon: "♧",
      path: "/customers",
      description: "Manage customers",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "SALES_MANAGER",
      ],
    },

    {
      title: "Orders",
      value: dashboard.totalOrders,
      icon: "▤",
      path: "/sales-orders",
      description: "View sales orders",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "SALES_MANAGER",
      ],
    },

    {
      title: "Total Sales",
      value: `₹${Number(
        dashboard.totalSales || 0
      ).toLocaleString("en-IN")}`,
      icon: "₹",
      path: "/accounting",
      description: "View accounting",
      roles: [
        "ADMIN",
        "ACCOUNTANT",
      ],
    },

    {
      title: "Pending Orders",
      value: dashboard.pendingOrders,
      icon: "◷",
      path: "/sales-orders",
      description: "Review pending orders",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "SALES_MANAGER",
      ],
    },

    {
      title: "Low Stock",
      value: dashboard.lowStockProducts,
      icon: "⚠",
      path: "/inventory",
      description: "Check low stock",
      roles: [
        "ADMIN",
        "SALES_MANAGER",
        "INVENTORY_MANAGER",
      ],
    },

  ];

  // =====================================================
  // FILTER CARDS ACCORDING TO ROLE
  // =====================================================

  const visibleCards =
    allCards.filter((card) =>
      card.roles.includes(role)
    );

  // =====================================================
  // ROLE-WISE OVERVIEW
  // =====================================================

  const overviewItems = [

    {
      title: "Employees",
      value: dashboard.totalEmployees,
      path: "/employees",
      roles: [
        "ADMIN",
        "HR_ADMIN",
      ],
    },

    {
      title: "Products",
      value: dashboard.totalProducts,
      path: "/inventory",
      roles: [
        "ADMIN",
        "SALES_MANAGER",
        "INVENTORY_MANAGER",
      ],
    },

    {
      title: "Customers",
      value: dashboard.totalCustomers,
      path: "/customers",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "SALES_MANAGER",
      ],
    },

    {
      title: "Orders",
      value: dashboard.totalOrders,
      path: "/sales-orders",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "SALES_MANAGER",
      ],
    },

    {
      title: "Pending Orders",
      value: dashboard.pendingOrders,
      path: "/sales-orders",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "SALES_MANAGER",
      ],
    },

    {
      title: "Low Stock",
      value: dashboard.lowStockProducts,
      path: "/inventory",
      roles: [
        "ADMIN",
        "SALES_MANAGER",
        "INVENTORY_MANAGER",
      ],
    },

  ];

  const visibleOverview =
    overviewItems.filter((item) =>
      item.roles.includes(role)
    );

  // =====================================================
  // ROLE TITLE
  // =====================================================

  const roleTitles = {
    ADMIN: "Administrator Dashboard",
    HR_ADMIN: "HR Management Dashboard",
    SALES_MANAGER: "Sales Management Dashboard",
    ACCOUNTANT: "Accounting Dashboard",
    INVENTORY_MANAGER: "Inventory Dashboard",
  };

  const dashboardTitle =
    roleTitles[role] ||
    "Enterprise Dashboard";

  // =====================================================
  // NORMAL ERP DASHBOARD
  // =====================================================

  return (
    <div>

      {/* =================================================
          HEADER
         ================================================= */}

      <div className="page-heading">

        <div>

          <h1>
            {dashboardTitle}
          </h1>

          <p>
            Overview of your enterprise
          </p>

        </div>

        <button
          className="secondary-button"
          onClick={loadDashboard}
        >
          ↻ Refresh
        </button>

      </div>

      {/* =================================================
          ERROR
         ================================================= */}

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      {/* =================================================
          ROLE INFO
         ================================================= */}

      <div
        className="dashboard-panel"
        style={{
          marginBottom: "20px",
        }}
      >

        <div className="panel-header">

          <div>

            <h3>
              Welcome back 👋
            </h3>

            <p>
              You are logged in as{" "}
              <strong>
                {role}
              </strong>
            </p>

          </div>

        </div>

      </div>

      {/* =================================================
          ROLE-WISE STAT CARDS
         ================================================= */}

      <div className="stats-grid">

        {visibleCards.map((card) => (

          <div
            className="stat-card dashboard-stat-card"
            key={card.title}
            onClick={() =>
              navigate(card.path)
            }
            role="button"
            tabIndex={0}
            onKeyDown={(e) => {

              if (
                e.key === "Enter" ||
                e.key === " "
              ) {
                navigate(card.path);
              }

            }}
          >

            {/* ICON */}

            <div className="stat-icon">
              {card.icon}
            </div>

            {/* VALUE */}

            <div className="stat-value">
              {card.value}
            </div>

            {/* TITLE */}

            <div className="stat-title">
              {card.title}
            </div>

            {/* LINK */}

            <div className="stat-card-link">
              {card.description}
              <span>
                {" →"}
              </span>
            </div>

          </div>

        ))}

      </div>

      {/* =================================================
          BUSINESS OVERVIEW
         ================================================= */}

      {visibleOverview.length > 0 && (

        <div className="dashboard-grid">

          <div className="dashboard-panel">

            <div className="panel-header">

              <div>

                <h3>
                  Business Overview
                </h3>

                <p>
                  Current ERP statistics
                </p>

              </div>

            </div>

            <div className="overview-list">

              {visibleOverview.map((item) => (

                <div
                  key={item.title}
                  className="overview-clickable"
                  onClick={() =>
                    navigate(item.path)
                  }
                  role="button"
                  tabIndex={0}
                  onKeyDown={(e) => {

                    if (
                      e.key === "Enter" ||
                      e.key === " "
                    ) {
                      navigate(item.path);
                    }

                  }}
                >

                  <span>
                    {item.title}
                  </span>

                  <strong>
                    {item.value}
                    {" →"}
                  </strong>

                </div>

              ))}

            </div>

          </div>

          {/* =================================================
              SYSTEM STATUS
             ================================================= */}

          <div className="dashboard-panel">

            <div className="panel-header">

              <div>

                <h3>
                  System Status
                </h3>

                <p>
                  Enterprise ERP services
                </p>

              </div>

            </div>

            <div className="system-status">

              {[
                "Backend API",
                "Authentication",
                "PostgreSQL Database",
                "Monitoring",
              ].map((item) => (

                <div
                  className="status-row"
                  key={item}
                >

                  <span>
                    {item}
                  </span>

                  <span className="status-badge success">
                    ● Online
                  </span>

                </div>

              ))}

            </div>

          </div>

        </div>

      )}

      {/* =================================================
          ROLE-SPECIFIC QUICK ACTIONS
         ================================================= */}

      {(role === "HR_ADMIN" ||
        role === "ADMIN") && (

        <div className="dashboard-grid">

          <div
            className="dashboard-panel dashboard-stat-card"
            onClick={() =>
              navigate("/attendance")
            }
            role="button"
            tabIndex={0}
          >

            <div className="panel-header">

              <div>

                <h3>
                  🕐 Attendance
                </h3>

                <p>
                  Manage employee attendance
                </p>

              </div>

              <strong>
                →
              </strong>

            </div>

          </div>

          <div
            className="dashboard-panel dashboard-stat-card"
            onClick={() =>
              navigate("/leaves")
            }
            role="button"
            tabIndex={0}
          >

            <div className="panel-header">

              <div>

                <h3>
                  📝 Leaves
                </h3>

                <p>
                  Manage employee leave requests
                </p>

              </div>

              <strong>
                →
              </strong>

            </div>

          </div>

        </div>

      )}

    </div>
  );
}

export default Dashboard;
