import { Navigate } from "react-router-dom";

function RoleProtectedRoute({
  allowedRoles,
  children,
}) {
  const role = localStorage.getItem("role");

  // User is not logged in
  if (!role) {
    return (
      <Navigate
        to="/login"
        replace
      />
    );
  }

  // User doesn't have permission
  if (!allowedRoles.includes(role)) {
    return (
      <Navigate
        to="/"
        replace
      />
    );
  }

  return children;
}

export default RoleProtectedRoute;