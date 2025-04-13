import { defineStore } from "pinia";
import apiClient from "@/plugins/axios"; // Cliente Axios configurado

// Utilidad para traducir campos booleanos a texto legible
function mapEmployee(employee) {
  return {
    ...employee,
    civilStatus: employee.civilStatus === "S" ? "Soltero" : "Casado",
    universityEducation: employee.universityEducation === "S" ? "Sí" : "No",
  };
}

export const useEmployeeStore = defineStore("employees", {
  // --- Estado ---
  state: () => ({
    employees: [],            // Lista completa de empleados
    basicEmployees: [],       // Lista básica (solo ID y nombre)
    isLoading: false,         // Indicador de carga
    error: null,              // Mensaje de error
    totalEmployees: 0,        // Total de empleados (para paginación)
    totalPages: 0,            // Total de páginas (para paginación)
  }),

  // --- Acciones ---
  actions: {
    // Obtener todos los empleados
    async fetchEmployees() {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.get("/employees");

        if (Array.isArray(response.data)) {
          this.employees = response.data.map(mapEmployee);
        } else {
          console.warn("La respuesta de /employees no es un array:", response.data);
          this.employees = [];
        }
      } catch (err) {
        console.error("No se pudieron obtener los empleados:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || "No se pudieron cargar los datos."}`;
        } else if (err.request) {
          this.error = "Error de red: no se pudo contactar con el servidor. Verifica tu conexión o el estado del backend.";
        } else {
          this.error = `Error inesperado: ${err.message}`;
        }
        this.employees = [];
      } finally {
        this.isLoading = false;
      }
    },

    // Refrescar la lista de empleados
    async refreshData() {
      console.log("Actualizando los datos de empleados...");
      await this.fetchEmployees();
    },

    // Añadir un nuevo empleado
    async addEmployee(newEmployee) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.post("/employees", newEmployee);
        this.employees.push(mapEmployee(response.data));
      } catch (err) {
        console.error("No se pudo añadir el empleado:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || "No se pudo añadir el empleado."}`;
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

    // Terminar (desactivar) un empleado
    async terminateEmployee(employeeId) {
      this.isLoading = true;
      this.error = null;

      try {
        await apiClient.put(`/employees/${employeeId}/terminate`);
        this.employees = this.employees.filter((emp) => emp.id !== employeeId);
      } catch (err) {
        if (err.response) {
          const status = err.response.status;
          const msg = err.response.data || "No se pudo desactivar el empleado.";
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

    // Obtener lista básica de empleados (usado por selects)
    async fetchBasicEmployees() {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.get("/employees/basic");

        if (Array.isArray(response.data)) {
          this.basicEmployees = response.data;
        } else {
          console.warn("La respuesta de /employees/basic no es un array:", response.data);
          this.basicEmployees = [];
        }
      } catch (err) {
        console.error("No se pudieron obtener los empleados básicos:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || "No se pudo cargar la lista básica de empleados."}`;
        } else if (err.request) {
          this.error = "Error de red: no se pudo contactar con el servidor.";
        } else {
          this.error = `Error inesperado: ${err.message}`;
        }
        this.basicEmployees = [];
      } finally {
        this.isLoading = false;
      }
    },

    // Obtener empleados paginados
    async fetchPaginatedEmployees({ page = 0, size = 10 } = {}) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await apiClient.get("/employees/paginated", {
          params: { page, size },
        });

        const { content, totalElements, totalPages } = response.data;

        if (Array.isArray(content)) {
          this.employees = content.map(mapEmployee);
        } else {
          console.warn("El formato de respuesta de paginación es inesperado:", response.data);
          this.employees = [];
        }

        this.totalEmployees = totalElements;
        this.totalPages = totalPages;
      } catch (err) {
        console.error("No se pudieron obtener los empleados paginados:", err);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data.message || "No se pudieron cargar los datos paginados."}`;
        } else if (err.request) {
          this.error = "Error de red: no se pudo contactar con el servidor.";
        } else {
          this.error = `Error inesperado: ${err.message}`;
        }
        this.employees = [];
      } finally {
        this.isLoading = false;
      }
    },
  },
});
