import { defineStore } from "pinia";
import apiClient from "@/plugins/axios"; // Cliente Axios configurado

export const useProjectStore = defineStore("projects", {
  // --- Estado ---
  state: () => ({
    projects: [], // Lista de proyectos obtenidos del backend
    isLoading: false, // Estado de carga (para spinners o deshabilitar acciones)
    error: null, // Mensaje de error si ocurre un fallo
  }),

  // --- Acciones ---
  actions: {
    // Obtener todos los proyectos
    async fetchProjects() {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.get("/projects");

        if (Array.isArray(response.data)) {
          this.projects = response.data;
        } else {
          console.warn(
            "La respuesta de la API para /projects no es un array:",
            response.data
          );
          this.projects = [];
          this.error = "El servidor devolvió un formato de datos inesperado.";
          throw new Error(this.error);
        }
      } catch (err) {
        console.error("No se pudieron obtener los proyectos:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${
            err.response.data.message || "No se pudieron cargar los datos."
          }`;
        } else if (err.request) {
          this.error =
            "Error de red: no se pudo contactar con el servidor. Verifica tu conexión o el estado del backend.";
        } else {
          this.error = `Error inesperado: ${err.message}`;
        }
        this.projects = [];
      } finally {
        this.isLoading = false;
      }
    },

    // Refrescar los datos de proyectos (vuelve a llamarse fetchProjects)
    async refreshData() {
      await this.fetchProjects();
    },

    // Añadir un nuevo proyecto
    async addProject(newProject) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.post("/projects", newProject);
        this.projects.push(response.data); // Agregar el nuevo proyecto a la lista local
      } catch (err) {
        console.error("No se pudo añadir el proyecto:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${
            err.response.data.message || "No se pudo añadir el proyecto."
          }`;
        } else if (err.request) {
          this.error = "Error de red: no se pudo conectar al servidor.";
        } else {
          this.error = `Error inesperado: ${err.message}`;
        }
        throw err;
      } finally {
        this.isLoading = false;
      }
    },

    // Terminar (finalizar) un proyecto por su ID
    async terminateProject(ProjectId) {
      this.isLoading = true;
      this.error = null;

      try {
        await apiClient.put(`/projects/${ProjectId}/terminate`);
        this.projects = this.projects.filter((emp) => emp.id !== ProjectId);
        // Se elimina el proyecto de la lista local
      } catch (err) {
        if (err.response) {
          const status = err.response.status;
          const msg = err.response.data || "No se pudo finalizar el proyecto.";
          this.error = `Error ${status}: ${msg}`;
          throw new Error(msg);
        } else if (err.request) {
          this.error = "Error de red: no se pudo conectar al servidor.";
          throw new Error(this.error);
        } else {
          this.error = `Error inesperado: ${err.message}`;
          throw new Error(this.error);
        }
      } finally {
        this.isLoading = false;
      }
    },
  },
});
