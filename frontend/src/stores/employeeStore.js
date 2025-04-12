// src/stores/employeeStore.js
import { defineStore } from 'pinia';
import apiClient from '@/plugins/axios'; // Import our configured Axios instance


function mapEmployee(employee) {
  return {
    ...employee,
    civilStatus: employee.civilStatus === 'S' ? 'Soltero' : 'Casado',
    universityEducation: employee.universityEducation === 'S' ? 'Sí' : 'No',
  };
}


export const useEmployeeStore = defineStore('employees', {
  // --- State ---
  // A function that returns the initial state object
  state: () => ({
    employees: [],      // Holds the list of employees
    basicEmployees: [],   // DTO data
    isLoading: false,   // Tracks loading state
    error: null,        // Holds potential error messages (String | null)
  }),

  // --- Getters (Optional) ---
  // Access state via the 'state' argument or 'this' (if not using arrow functions)
  getters: {
    // Example: Get the count of employees
    employeeCount(state) {
      return state.employees.length;
    },
    // Example: A simple boolean check if data is ready to be displayed
    hasData(state) {
      return !state.isLoading && !state.error && state.employees.length > 0;
    },
  },

  // --- Actions ---
  // Methods defined here can be asynchronous and access state/other actions via 'this'
  actions: {
    async fetchEmployees() {
      this.isLoading = true; // Use 'this' to access state properties
      this.error = null;

      try {
        // Make the GET request using our configured apiClient
        const response = await apiClient.get('/employees'); // Same API call

        // Basic validation remains important
        if (Array.isArray(response.data)) {
          this.employees = response.data.map(mapEmployee);
        } else {
          console.warn('API response for /employees was not an array:', response.data);
          this.employees = [];
          // Optionally set an error specific to data format
          // this.error = 'Received unexpected data format from server.';
        }

      } catch (err) {
        console.error('Failed to fetch employees:', err);
        // Improve error message based on actual error if possible
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || 'Failed to load data.'}`;
        } else if (err.request) {
          this.error = 'Network Error: Could not reach the server. Please check your connection or the backend status.';
        } else {
          this.error = `An unexpected error occurred: ${err.message}`;
        }
        this.employees = []; // Clear data on error
      } finally {
        this.isLoading = false; // Set loading false after request finishes
      }
    },

    async refreshData() {
      console.log('Refreshing employee data...');
      await this.fetchEmployees();
    },

    async addEmployee(newEmployee) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.post('/employees', newEmployee);
        // Optionally push to state:
        this.employees.push(mapEmployee(response.data));
        // print employee
      } catch (err) {
        console.error('Failed to add employee:', err);
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
    
    async terminateEmployee(employeeId) {
      this.isLoading = true;
      this.error = null;
    
      try {
        const response = await apiClient.put(`/employees/${employeeId}/terminate`);
        this.employees = this.employees.filter(emp => emp.id !== employeeId);
      } catch (err) {
        if (err.response) {
          const status = err.response.status;
          const msg = err.response.data || 'No se pudo desasignar el empleado.';
    
          // Store the message if needed
          this.error = `Error ${status}: ${msg}`;
    
          // Optional: Re-throw for the frontend component to handle it
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
    },
    
    async fetchBasicEmployees() {
      this.isLoading = true;
      this.error = null;
    
      try {
        const response = await apiClient.get('/employees/basic');
        
        if (Array.isArray(response.data)) {
          this.basicEmployees = response.data;
        } else {
          console.warn('API response for /employees/basic was not an array:', response.data);
          this.basicEmployees = [];
        }
    
      } catch (err) {
        console.error('Failed to fetch basic employees:', err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || 'Failed to load basic employee data.'}`;
        } else if (err.request) {
          this.error = 'Network Error: Could not reach the server.';
        } else {
          this.error = `Unexpected error: ${err.message}`;
        }
        this.basicEmployees = [];
      } finally {
        this.isLoading = false;
      }
    },
    
  },
      

});