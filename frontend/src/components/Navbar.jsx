import { useEffect, useState } from "react";

function Navbar() {
  const [showProfile, setShowProfile] = useState(false);

  const username =
    localStorage.getItem("username") || "User";

  const role =
    localStorage.getItem("role") || "EMPLOYEE";

  const email =
    localStorage.getItem("email") || "Not available";

  const [employee, setEmployee] = useState(null);

  useEffect(() => {
    const loadEmployeeProfile = async () => {
      const token = localStorage.getItem("token");

      if (!token) return;

      try {
        const response = await fetch(
          "http://localhost:8080/api/employee/profile",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          return;
        }

        const data = await response.json();

        setEmployee(data);

      } catch (error) {
        console.log(
          "Employee profile not available"
        );
      }
    };

    if (role === "EMPLOYEE") {
      loadEmployeeProfile();
    }
  }, [role]);

  const formatRole = (value) => {
    if (!value) return "User";

    return value
      .replaceAll("_", " ")
      .toLowerCase()
      .replace(/\b\w/g, (char) =>
        char.toUpperCase()
      );
  };

  const position =
    employee?.designation ||
    formatRole(role);

  const displayEmail =
    employee?.email ||
    email;

  const displayName =
    employee
      ? `${employee.firstName || ""} ${
          employee.lastName || ""
        }`.trim()
      : username;

  const avatarLetter =
    displayName.charAt(0).toUpperCase();

  return (
    <header className="navbar">

      {/* LEFT */}
      <div>
        <h3>
          Enterprise Resource Planning
        </h3>

        <p>
          Manage your business from one place
        </p>
      </div>

      {/* RIGHT */}
      <div className="navbar-right">

        {/* NOTIFICATION */}
        <button
          className="notification-button"
          title="Notifications"
        >
          🔔
        </button>

        {/* PROFILE */}
        <div className="user-profile-wrapper">

          <div
            className="user-profile"
            onClick={() =>
              setShowProfile(!showProfile)
            }
          >

            <div className="profile-large-avatar">
              {avatarLetter}
            </div>

            <div className="user-profile-text">

              <strong>
                {displayName}
              </strong>

              <span>
                {formatRole(role)}
              </span>

            </div>

            <span className="profile-arrow">
              {showProfile ? "▲" : "▼"}
            </span>

          </div>

          {/* PROFILE DROPDOWN */}
          {showProfile && (
            <div className="profile-dropdown">

              <div className="profile-dropdown-header">

                <div className="profile-large-avatar">
                  {avatarLetter}
                </div>

                <div>
                  <h4>
                    {displayName}
                  </h4>

                  <p>
                    {formatRole(role)}
                  </p>
                </div>

              </div>

              <div className="profile-information">

                {/* USERNAME */}
                <div className="profile-info-row">

                  <span>
                    👤 Username
                  </span>

                  <strong>
                    {username}
                  </strong>

                </div>

                {/* ROLE */}
                <div className="profile-info-row">

                  <span>
                    🛡️ Role
                  </span>

                  <strong>
                    {formatRole(role)}
                  </strong>

                </div>

                {/* POSITION */}
                <div className="profile-info-row">

                  <span>
                    💼 Position
                  </span>

                  <strong>
                    {position}
                  </strong>

                </div>

                {/* EMAIL */}
                <div className="profile-info-row">

                  <span>
                    ✉️ Email
                  </span>

                  <strong>
                    {displayEmail}
                  </strong>

                </div>

                {/* DEPARTMENT */}
                {employee?.departmentName && (
                  <div className="profile-info-row">

                    <span>
                      🏢 Department
                    </span>

                    <strong>
                      {employee.departmentName}
                    </strong>

                  </div>
                )}

                {/* EMPLOYEE CODE */}
                {employee?.employeeCode && (
                  <div className="profile-info-row">

                    <span>
                      🆔 Employee ID
                    </span>

                    <strong>
                      {employee.employeeCode}
                    </strong>

                  </div>
                )}

              </div>

              <div className="profile-dropdown-footer">

                <button
                  onClick={() =>
                    setShowProfile(false)
                  }
                >
                  Close
                </button>

              </div>

            </div>
          )}

        </div>

      </div>

    </header>
  );
}

export default Navbar;