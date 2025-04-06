// src/plugins/axios.js

// Adam: I FOUND THIS CODE ONLINE

import axios from 'axios';

// --- Configuration ---
// Use environment variables for base URL if available, otherwise fallback
// Vite uses `import.meta.env.VITE_API_BASE_URL`
// Vue CLI uses `process.env.VUE_APP_API_BASE_URL`
const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'; // Adjust fallback if needed

// Create the Axios instance
const apiClient = axios.create({
  baseURL: baseURL,
  headers: {
    'Content-Type': 'application/json',
    // You can add other default headers here if necessary
    // e.g., 'Accept': 'application/json'
  }
});

export default apiClient;