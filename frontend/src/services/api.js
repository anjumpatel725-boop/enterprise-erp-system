import axios from "axios";

const api = axios.create({
  baseURL:
    import.meta.env.VITE_API_BASE_URL ||
    "http://localhost:8080/api",

  headers: {
    "Content-Type": "application/json",
  },
});

// Add JWT token to every request
api.interceptors.request.use(
  (config) => {
    const token =
      localStorage.getItem("token");

    if (token) {
      config.headers.Authorization =
        `Bearer ${token}`;
    }

    return config;
  },

  (error) => {
    return Promise.reject(error);
  }
);

// Handle responses
api.interceptors.response.use(
  (response) => {
    return response;
  },

  (error) => {

    // Unauthorized - token missing/expired
    if (error.response?.status === 401) {

      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("role");

      window.location.href = "/login";
    }

    // Forbidden - user is logged in,
    // but does not have permission
    if (error.response?.status === 403) {
      console.error(
        "403 FORBIDDEN:",
        error.config?.method?.toUpperCase(),
        error.config?.url
      );
    }

    return Promise.reject(error);
  }
);

export default api;