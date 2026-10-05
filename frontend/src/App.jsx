import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";
import "./index.css";
import "./app.css";
import Sidebar from "./components/Sidebar";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import RoleProtectedRoute from "./components/RoleProtectedRoute";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import HR from "./pages/HR";
import Employees from "./pages/Employees";
import Attendance from "./pages/Attendance";
import Leaves from "./pages/Leaves";
import Inventory from "./pages/Inventory";
import Customers from "./pages/Customers";
import SalesOrders from "./pages/SalesOrders";
import Accounting from "./pages/Accounting";
import Reports from "./pages/Reports";
import AuditLogs from "./pages/AuditLogs";


function Layout() {
  return (
    <div className="app-layout">

      <Sidebar />

      <div className="main-section">

        <Navbar />

        <main className="content">

          <Routes>

            {/* ================= DASHBOARD ================= */}

            <Route
              path="/"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "HR_ADMIN",
                    "SALES_MANAGER",
                    "ACCOUNTANT",
                    "INVENTORY_MANAGER",
                    "EMPLOYEE",
                  ]}
                >
                  <Dashboard />
                </RoleProtectedRoute>
              }
            />


            {/* ================= HR ================= */}

            <Route
              path="/hr"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "HR_ADMIN",
                  ]}
                >
                  <HR />
                </RoleProtectedRoute>
              }
            />


            {/* ================= EMPLOYEES ================= */}

            <Route
              path="/employees"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "HR_ADMIN",
                  ]}
                >
                  <Employees />
                </RoleProtectedRoute>
              }
            />


            {/* ================= ATTENDANCE ================= */}

            <Route
              path="/attendance"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "HR_ADMIN",
                    "EMPLOYEE",
                  ]}
                >
                  <Attendance />
                </RoleProtectedRoute>
              }
            />


            {/* ================= LEAVES ================= */}

            <Route
              path="/leaves"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "HR_ADMIN",
                    "EMPLOYEE",
                  ]}
                >
                  <Leaves />
                </RoleProtectedRoute>
              }
            />


            {/* ================= INVENTORY ================= */}

            <Route
              path="/inventory"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "INVENTORY_MANAGER",
                    "SALES_MANAGER",
                  ]}
                >
                  <Inventory />
                </RoleProtectedRoute>
              }
            />


            {/* ================= CUSTOMERS ================= */}

            <Route
              path="/customers"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "SALES_MANAGER",
                    "HR_ADMIN",
                  ]}
                >
                  <Customers />
                </RoleProtectedRoute>
              }
            />


            {/* ================= SALES ORDERS ================= */}

            <Route
              path="/sales-orders"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "SALES_MANAGER",
                    "HR_ADMIN",
                  ]}
                >
                  <SalesOrders />
                </RoleProtectedRoute>
              }
            />


            {/* ================= ACCOUNTING ================= */}

            <Route
              path="/accounting"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "ACCOUNTANT",
                  ]}
                >
                  <Accounting />
                </RoleProtectedRoute>
              }
            />


            {/* ================= REPORTS ================= */}

            <Route
              path="/reports"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "SALES_MANAGER",
                    "HR_ADMIN",
                    "ACCOUNTANT",
                    "INVENTORY_MANAGER",
                  ]}
                >
                  <Reports />
                </RoleProtectedRoute>
              }
            />


            {/* ================= AUDIT LOGS ================= */}

            <Route
              path="/audit-logs"
              element={
                <RoleProtectedRoute
                  allowedRoles={[
                    "ADMIN",
                    "HR_ADMIN",
                    "SALES_MANAGER",
                  ]}
                >
                  <AuditLogs />
                </RoleProtectedRoute>
              }
            />

          </Routes>

        </main>

      </div>

    </div>
  );
}


function App() {
  return (
    <BrowserRouter>

      <Routes>

        {/* ================= PUBLIC LOGIN ================= */}

        <Route
          path="/login"
          element={<Login />}
        />


        {/* ================= PUBLIC REGISTER ================= */}

        <Route
          path="/register"
          element={<Register />}
        />


        {/* ================= PROTECTED APPLICATION ================= */}

        <Route
          path="/*"
          element={
            <ProtectedRoute>
              <Layout />
            </ProtectedRoute>
          }
        />

      </Routes>

    </BrowserRouter>
  );
}


export default App;
