import { NavLink, useNavigate } from "react-router-dom";

function Sidebar() {
  const navigate = useNavigate();

  const role = localStorage.getItem("role");

  const menuItems = [
    // =========================
    // DASHBOARD
    // =========================
    {
      path: "/",
      label: "Dashboard",
      icon: "▦",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "SALES_MANAGER",
        "ACCOUNTANT",
        "INVENTORY_MANAGER",
        "EMPLOYEE",
      ],
    },

    // =========================
    // HR
    // =========================
    {
      path: "/hr",
      label: "HR",
      icon: "♙",
      roles: [
        "ADMIN",
        "HR_ADMIN",
      ],
    },

    // =========================
    // EMPLOYEES
    // =========================
    {
      path: "/employees",
      label: "Employees",
      icon: "♟",
      roles: [
        "ADMIN",
        "HR_ADMIN",
      ],
    },

    // =========================
    // ATTENDANCE
    // =========================
    {
      path: "/attendance",
      label: "Attendance",
      icon: "◷",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "EMPLOYEE",
      ],
    },

    // =========================
    // LEAVES
    // =========================
    {
      path: "/leaves",
      label: "Leaves",
      icon: "▱",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "EMPLOYEE",
      ],
    },

    // =========================
    // INVENTORY
    // =========================
    {
      path: "/inventory",
      label: "Inventory",
      icon: "▣",
      roles: [
        "ADMIN",
        "INVENTORY_MANAGER",
        "SALES_MANAGER",
      ],
    },

    // =========================
    // CUSTOMERS
    // =========================
    {
      path: "/customers",
      label: "Customers",
      icon: "♧",
      roles: [
        "ADMIN",
        "SALES_MANAGER",
        "HR_ADMIN",
      ],
    },

    // =========================
    // SALES ORDERS
    // =========================
    {
      path: "/sales-orders",
      label: "Sales Orders",
      icon: "▤",
      roles: [
        "ADMIN",
        "SALES_MANAGER",
        "HR_ADMIN",
      ],
    },

    // =========================
    // ACCOUNTING
    // =========================
    {
      path: "/accounting",
      label: "Accounting",
      icon: "₹",
      roles: [
        "ADMIN",
        "ACCOUNTANT",
      ],
    },

    // =========================
    // REPORTS
    // =========================
    {
      path: "/reports",
      label: "Reports",
      icon: "◫",
      roles: [
        "ADMIN",
        "SALES_MANAGER",
        "HR_ADMIN",
        "ACCOUNTANT",
        "INVENTORY_MANAGER",
      ],
    },

    // =========================
    // AUDIT LOGS
    // =========================
    {
      path: "/audit-logs",
      label: "Audit Logs",
      icon: "◉",
      roles: [
        "ADMIN",
        "HR_ADMIN",
        "SALES_MANAGER",
      ],
    },
  ];

  // =========================
  // FILTER MENU BY ROLE
  // =========================

  const visibleMenuItems = menuItems.filter((item) =>
    item.roles.includes(role)
  );

  // =========================
  // LOGOUT
  // =========================
const handleLogout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("username");
  localStorage.removeItem("email");
  localStorage.removeItem("role");

  navigate("/login");
};

  // =========================
  // UI
  // =========================

  return (
    <aside className="sidebar">

      {/* =========================
          LOGO
          ========================= */}

      <div className="logo-section">

        <div className="logo-icon">
          E
        </div>

        <div>
          <h2>Enterprise</h2>
          <span>ERP System</span>
        </div>

      </div>

      {/* =========================
          MENU TITLE
          ========================= */}

      <div className="menu-title">
        MAIN MENU
      </div>

      {/* =========================
          MENU
          ========================= */}

      <nav className="sidebar-menu">

        {visibleMenuItems.map((item) => (

          <NavLink
            key={item.path}
            to={item.path}
            end={item.path === "/"}
            className={({ isActive }) =>
              `menu-item ${isActive ? "active" : ""}`
            }
          >

            <span className="menu-icon">
              {item.icon}
            </span>

            <span>
              {item.label}
            </span>

          </NavLink>

        ))}

      </nav>

      {/* =========================
          BOTTOM
          ========================= */}

      <div className="sidebar-bottom">

        <div className="sidebar-role">

          <span>Role</span>

          <strong>
            {role || "EMPLOYEE"}
          </strong>

        </div>

        <button
          className="logout-button"
          onClick={handleLogout}
        >
          <span>⇥</span>
          Logout
        </button>

      </div>

    </aside>
  );
}

export default Sidebar;