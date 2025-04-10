// src/stores/Projectstore.js
import { defineStore } from 'pinia';
import apiClient from '@/plugins/axios'; // Import our configured Axios instance




export const useProjectStore = defineStore('projects', {
  // --- State ---
  // A function that returns the initial state object
  state: () => ({
    projects: [],      // Holds the list of projects
    isLoading: false,   // Tracks loading state
    error: null,        // Holds potential error messages (String | null)
  }),

  // --- Getters  ---
  // Can be added but not really needed as of the time of writing this
  getters: {

  },

  // --- Actions ---
  // Methods defined here can be asynchronous and access state/other actions via 'this'
  actions: {
    async fetchProjects() {
      this.isLoading = true; // Use 'this' to access state properties
      this.error = null;

      try {
        // Make the GET request using our configured apiClient
        const response = await apiClient.get('/projects'); // Same API call

        // Basic validation remains important
        if (Array.isArray(response.data)) {
          this.projects = response.data;
        } else {
          console.warn('API response for /projects was not an array:', response.data);
          this.projects = [];
          this.error = 'Received unexpected data format from server.';
          throw new Error(this.error);
        }

      } catch (err) {
        console.error('Failed to fetch projects:', err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || 'Failed to load data.'}`;
        } else if (err.request) {
          this.error = 'Network Error: Could not reach the server. Please check your connection or the backend status.';
        } else {
          this.error = `An unexpected error occurred: ${err.message}`;
        }
        this.projects = []; // Clear data on error
      } finally {
        this.isLoading = false; // Set loading false after request finishes
      }
    },

    async refreshData() {
      await this.fetchProjects();
    },

    async addProject(newProject) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.post('/projects', newProject);
        // Optionally push to state:
        this.projects.push(response.data);
      } catch (err) {
        console.error('Failed to add project:', err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || 'No se pudo añadir el empleado.'}`;
        } else if (err.request) {
          this.error = 'Error de red: no se pudo conectar al servidor.';
        } else {
          this.error = `Error inesperado: ${err.message}`;
        }
        throw err; // Re-throw so UI can handle it
      } finally {
        this.isLoading = false;
      }
    },

    async terminateProject(ProjectId) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.put(`/projects/${ProjectId}/terminate`);
        this.projects = this.projects.filter(emp => emp.id !== ProjectId);
        // Optional: Console log sucessful operation using response msg
      } catch (err) {
        if (err.response) {
          const status = err.response.status;
          const msg = err.response.data || 'No se pudo desasignar el empleado.';
          // Store the message if needed
          this.error = `Error ${status}: ${msg}`;
          // Re-throw for the frontend component to handle it
          throw new Error(msg);
        } else if (err.request) {
          this.error = 'Error de red: no se pudo conectar al servidor.';
          throw new Error(this.error);
        } else {
          this.error = `Error inesperado: ${err.message}`;
          throw new Error(this.error);
        }
      } finally {
        this.isLoading = false;
      }
    }
  },


});