import { defineStore } from 'pinia';
import apiClient from '@/plugins/axios';

export const useAssignmentStore = defineStore('assignments', {
  state: () => ({
    assignments: [],
    isLoading: false,
    error: null,
  }),

  actions: {
    async fetchAssignments() {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.get('/assignments');

        if (Array.isArray(response.data)) {
          this.assignments = response.data;
        } else {
          console.warn('API response for /assignments was not in the expected format:', response.data);
          this.assignments = [];
          this.error = 'Received unexpected data format from server.';
          throw new Error(this.error);
        }
      } catch (err) {
        console.error('Failed to fetch assignments:', err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || 'Failed to load data.'}`;
        } else if (err.request) {
          this.error = 'Network Error: Could not reach the server. Please check your connection or the backend status.';
        } else {
          this.error = `An unexpected error occurred: ${err.message}`;
        }
        this.assignments = [];
      } finally {
        this.isLoading = false;
      }
    },

    async fetchProjectAssignments(projectId) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.get(`/assignments/project/${projectId}`);

        if (Array.isArray(response.data)) {
          this.assignments = response.data;
        } else {
          console.warn('API response for project assignments was not in the expected format:', response.data);
          this.assignments = [];
          this.error = 'Received unexpected data format from server.';
          throw new Error(this.error);
        }
      } catch (err) {
        console.error('Failed to fetch project assignments:', err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || 'Failed to load data.'}`;
        } else if (err.request) {
          this.error = 'Network Error: Could not reach the server. Please check your connection or the backend status.';
        } else {
          this.error = `An unexpected error occurred: ${err.message}`;
        }
        this.assignments = [];
      } finally {
        this.isLoading = false;
      }
    },

    async refreshData() {
      console.log('Refreshing assignment data...');
      await this.fetchAssignments();
    },

    async terminateAssignment(assignmentId) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.put(`/assignments/${assignmentId}/terminate`);
        this.assignments = this.assignments.filter(assignment => assignment.id !== assignmentId);
      } catch (err) {
        if (err.response) {
          const status = err.response.status;
          const msg = err.response.data || 'Failed to terminate assignment.';
          this.error = `Error ${status}: ${msg}`;
          throw new Error(msg);
        } else if (err.request) {
          this.error = 'Network Error: Could not reach the server.';
          throw new Error(this.error);
        } else {
          this.error = `An unexpected error occurred: ${err.message}`;
          throw new Error(this.error);
        }
      } finally {
        this.isLoading = false;
      }
    },

    async assignEmployeeToProject(projectId, employeeId) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.post(`/assignments/${projectId}/assign/${employeeId}`);
        
        // Refresh the assignments list
        await this.fetchProjectAssignments(projectId);
        return response.data;
      } catch (err) {
        console.error('Failed to assign employee to project:', err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || 'Failed to assign employee.'}`;
        } else if (err.request) {
          this.error = 'Network Error: Could not reach the server.';
        } else {
          this.error = `An unexpected error occurred: ${err.message}`;
        }
        throw new Error(this.error);
      } finally {
        this.isLoading = false;
      }
    },

    async unassignEmployeeFromProject(projectId, employeeId) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.delete(`/assignments/${projectId}/unassign/${employeeId}`);
        
        // Refresh the assignments list
        await this.fetchProjectAssignments(projectId);
        return response.data;
      } catch (err) {
        console.error('Failed to unassign employee from project:', err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || 'Failed to unassign employee.'}`;
        } else if (err.request) {
          this.error = 'Network Error: Could not reach the server.';
        } else {
          this.error = `An unexpected error occurred: ${err.message}`;
        }
        throw new Error(this.error);
      } finally {
        this.isLoading = false;
      }
    }
  },
});
