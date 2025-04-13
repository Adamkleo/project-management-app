import { defineStore } from "pinia";
import apiClient from "@/plugins/axios";

// Definimos el store de asignaciones
export const useAssignmentStore = defineStore("assignments", {
  state: () => ({
    // Lista de asignaciones cargadas desde el backend
    assignments: [],
    // Indicador de carga para mostrar spinners o deshabilitar acciones
    isLoading: false,
    // Mensaje de error si ocurre un fallo en alguna petición
    error: null,
  }),

  actions: {
    // Obtener todas las asignaciones desde el backend
    async fetchAssignments() {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.get("/assignments");

        // Verificamos que la respuesta sea un array
        if (Array.isArray(response.data)) {
          this.assignments = response.data;
        } else {
          console.warn(
            "La respuesta de la API para /assignments no tiene el formato esperado:",
            response.data
          );
          this.assignments = [];
          this.error = "El servidor devolvió un formato de datos inesperado.";
          throw new Error(this.error);
        }
      } catch (err) {
        console.error("No se pudieron obtener las asignaciones:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${
            err.response.data.message || "Error al cargar los datos."
          }`;
        } else if (err.request) {
          this.error =
            "Error de red: No se pudo contactar con el servidor. Revisa tu conexión o el estado del backend.";
        } else {
          this.error = `Ocurrió un error inesperado: ${err.message}`;
        }
        this.assignments = [];
      } finally {
        this.isLoading = false;
      }
    },

    // Obtener asignaciones relacionadas con un proyecto específico
    async fetchProjectAssignments(projectId) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.get(
          `/assignments/project/${projectId}`
        );

        if (Array.isArray(response.data)) {
          this.assignments = response.data;
        } else {
          console.warn(
            "La respuesta de la API para asignaciones del proyecto no tiene el formato esperado:",
            response.data
          );
          this.assignments = [];
          this.error = "El servidor devolvió un formato de datos inesperado.";
          throw new Error(this.error);
        }
      } catch (err) {
        console.error(
          "No se pudieron obtener las asignaciones del proyecto:",
          err
        );
        if (err.response) {
          this.error = `Error ${err.response.status}: ${
            err.response.data.message || "Error al cargar los datos."
          }`;
        } else if (err.request) {
          this.error =
            "Error de red: No se pudo contactar con el servidor. Revisa tu conexión o el estado del backend.";
        } else {
          this.error = `Ocurrió un error inesperado: ${err.message}`;
        }
        this.assignments = [];
      } finally {
        this.isLoading = false;
      }
    },

    // Refrescar las asignaciones generales (equivale a fetchAssignments)
    async refreshData() {
      console.log("Actualizando los datos de asignaciones...");
      await this.fetchAssignments();
    },

    // Terminar (finalizar) una asignación específica
    async terminateAssignment(assignmentId) {
      this.isLoading = true;
      this.error = null;

      try {
        await apiClient.put(`/assignments/${assignmentId}/terminate`);
        // Eliminamos la asignación del estado local
        this.assignments = this.assignments.filter(
          (assignment) => assignment.id !== assignmentId
        );
      } catch (err) {
        if (err.response) {
          const status = err.response.status;
          const msg = err.response.data || "No se pudo terminar la asignación.";
          this.error = `Error ${status}: ${msg}`;
          throw new Error(msg);
        } else if (err.request) {
          this.error = "Error de red: No se pudo contactar con el servidor.";
          throw new Error(this.error);
        } else {
          this.error = `Ocurrió un error inesperado: ${err.message}`;
          throw new Error(this.error);
        }
      } finally {
        this.isLoading = false;
      }
    },

    // Asignar un empleado a un proyecto
    async assignEmployeeToProject(projectId, employeeId) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.post(
          `/assignments/${projectId}/assign/${employeeId}`
        );
        // Actualizamos las asignaciones del proyecto luego de la operación
        await this.fetchProjectAssignments(projectId);
        return response.data;
      } catch (err) {
        console.error("No se pudo asignar el empleado al proyecto:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${
            err.response.data.message || "No se pudo asignar al empleado."
          }`;
        } else if (err.request) {
          this.error = "Error de red: No se pudo contactar con el servidor.";
        } else {
          this.error = `Ocurrió un error inesperado: ${err.message}`;
        }
        throw new Error(this.error);
      } finally {
        this.isLoading = false;
      }
    },

    // Desasignar un empleado de un proyecto
    async unassignEmployeeFromProject(projectId, employeeId) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.delete(
          `/assignments/${projectId}/unassign/${employeeId}`
        );
        // Actualizamos las asignaciones del proyecto luego de la operación
        await this.fetchProjectAssignments(projectId);
        return response.data;
      } catch (err) {
        console.error("No se pudo desasignar el empleado del proyecto:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${
            err.response.data.message || "No se pudo desasignar al empleado."
          }`;
        } else if (err.request) {
          this.error = "Error de red: No se pudo contactar con el servidor.";
        } else {
          this.error = `Ocurrió un error inesperado: ${err.message}`;
        }
        throw new Error(this.error);
      } finally {
        this.isLoading = false;
      }
    },
  },
});
