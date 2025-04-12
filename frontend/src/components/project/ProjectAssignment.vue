<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useProjectStore } from "@/stores/projectStore";
import { useEmployeeStore } from "@/stores/employeeStore";
import { useAssignmentStore } from "@/stores/assignmentStore";
import DataTable from "@/components/general/DataTable.vue";
import Swal from "sweetalert2";

const route = useRoute();
const router = useRouter();
// Id del proyecto seleccionado
const projectId = computed(() => parseInt(route.params.id));

const projectStore = useProjectStore();
const employeeStore = useEmployeeStore();
const assignmentStore = useAssignmentStore();

// Estados: empleados desponibles y asignados a un proyecto
const availableEmployees = ref([]);
const assignedEmployees = ref([]);
// Estado: El objeto del proyecto
const project = ref(null);

// Attributos de los empleados necesarios
const employeeHeaders = [
  { title: "Nombre", key: "name", align: "start" },
  { title: "Acciones", key: "actions", align: "end", sortable: false },
];

// Combinar el primer nombre y los dos apellidos del empleado
const formatEmployeeName = (employee) =>
  `${employee.firstName} ${employee.lastName1} ${employee.lastName2}`;

/* SweetAlert Functions que estan utilizadas en varios sitios */

// Funcion para mostrar error alert de sweetalert2 con on mensaje
const showErrorAlert = (message) => {
  Swal.fire({
    icon: "error",
    title: "Error",
    text: message,
  });
};

// Funcion para mostrar que un processo esta 'cargando' con un titulo
const showLoadingSwal = (title) => {
  Swal.fire({
    title,
    allowOutsideClick: false,
    didOpen: () => {
      Swal.showLoading();
    },
  });
};

// Cargar empleados y asignaciones
const loadEmployeesAndAssignments = async () => {
  /* Promise para cargar los dos antes de empezar
   * En el caso que hay muchos empleados y poco asignaciones
   * Uno puede terminar antes que el otro
   * Es mejor si esperamos a los dos
   */
  await Promise.all([
    employeeStore.fetchBasicEmployees(),
    assignmentStore.fetchProjectAssignments(projectId.value),
  ]);

  // Processar asignaciones cuando se completa la carga
  processAssignments();
};

// On mount
onMounted(async () => {
  try {
    // Fetch proyectos
    await projectStore.fetchProjects();
    // Poner el objeto del proyecto
    project.value = projectStore.projects.find((p) => p.id === projectId.value);

    // Si no existe el proyecto, error, y volvemos a proyectos
    if (!project.value) {
      showErrorAlert("Proyecto no encontrado");
      router.push("/proyectos");
      return;
    }

    // Cargar empleados y asignados
    await loadEmployeesAndAssignments();
  } catch (error) {
    console.error("Error loading data:", error);
    showErrorAlert("No se pudo cargar los datos");
  }
});

// Formatea un empleado añadiendo su nombre completo como propiedad 'name'
function formatWithName(employee) {
  return {
    ...employee,
    name: formatEmployeeName(employee),
  };
}

// Filtra empleados que no están asignados
function getAvailableEmployees(allEmployees, assignedIds) {
  const available = [];

  for (const employee of allEmployees) {
    if (!assignedIds.includes(employee.id)) {
      available.push(formatWithName(employee));
    }
  }

  return available;
}

// Devuelve los empleados asignados con nombre formateado
function getAssignedEmployees(assignments) {
  const assigned = [];

  for (const assignment of assignments) {
    const employee = assignment.employee;
    assigned.push(formatWithName(employee));
  }

  return assigned;
}

// Procesa la separación entre empleados disponibles y asignados
const processAssignments = () => {
  // Obtener los IDs de empleados ya asignados al proyecto
  const assignedIds = assignmentStore.assignments.map((a) => a.employee.id);

  // Obtener y guardar empleados disponibles
  availableEmployees.value = getAvailableEmployees(
    employeeStore.basicEmployees,
    assignedIds
  );

  // Obtener y guardar empleados asignados
  assignedEmployees.value = getAssignedEmployees(assignmentStore.assignments);
};

// Asignar empleado con el assignment store
const handleAssignEmployee = async (employee) => {
  try {
    showLoadingSwal("Asignando empleado...");
    await assignmentStore.assignEmployeeToProject(projectId.value, employee.id);
    await loadEmployeesAndAssignments();
    Swal.fire({
      icon: "success",
      title: "Éxito",
      text: "Empleado asignado correctamente",
    });
  } catch (error) {
    console.error("Error assigning employee:", error);
    showErrorAlert(error.message || "No se pudo asignar el empleado");
  }
};

// Quitar empleado de un proyecto con el assignment store
const handleUnassignEmployee = async (employee) => {
  try {
    const result = await Swal.fire({
      icon: "question",
      title: "Confirmar desasignación",
      text: `¿Está seguro de desasignar a ${employee.name} del proyecto?`,
      showCancelButton: true,
      confirmButtonText: "Sí, desasignar",
      cancelButtonText: "Cancelar",
    });

    if (!result.isConfirmed) return;

    showLoadingSwal("Desasignando empleado...");
    await assignmentStore.unassignEmployeeFromProject(
      projectId.value,
      employee.id
    );
    await loadEmployeesAndAssignments();
    Swal.fire({
      icon: "success",
      title: "Éxito",
      text: "Empleado desasignado correctamente",
    });
  } catch (error) {
    console.error("Error unassigning employee:", error);
    showErrorAlert(error.message || "No se pudo desasignar el empleado");
  }
};

const navigateBack = () => {
  router.push("/proyectos");
};
</script>

<template>
  <v-container
    fluid
    class="pa-8"
  >
    <v-card-title class="d-flex align-center">
      <v-icon
        icon="mdi-arrow-left"
        class="me-2"
        @click="navigateBack"
      ></v-icon>
      <span
        >Asignación de Empleados -
        {{ project?.description || "Cargando..." }}</span
      >
    </v-card-title>
    <v-card-text>
      <v-row>
        <v-col
          cols="12"
          md="6"
        >
          <v-card-title class="text-subtitle-1"> </v-card-title>
          <v-card-text>
            <DataTable
              :items="availableEmployees"
              :headers="employeeHeaders"
              :loading="employeeStore.isLoading || assignmentStore.isLoading"
              :show-actions="true"
              :title="'Empleados Disponibles'"
              :show-search="true"
              search-label="Buscar empleados disponibles..."
              @edit-item="handleAssignEmployee"
              :show-select="false"
              :show-button="false"
              edit-action-icon="mdi-arrow-right"
              edit-action-tooltip="Asignar empleado"
              :show-delete-action="false"
            />
          </v-card-text>
        </v-col>

        <v-col
          cols="12"
          md="6"
        >
          <v-card-title class="text-subtitle-1"> </v-card-title>
          <v-card-text>
            <DataTable
              :items="assignedEmployees"
              :headers="employeeHeaders"
              :loading="employeeStore.isLoading || assignmentStore.isLoading"
              :show-actions="true"
              :show-search="true"
              :title="'Empleados Asignados'"
              search-label="Buscar empleados asignados..."
              @delete-item="handleUnassignEmployee"
              :show-select="false"
              :show-button="false"
              :show-edit-action="false"
              delete-action-icon="mdi-arrow-left"
              delete-action-tooltip="Desasignar empleado"
            />
          </v-card-text>
        </v-col>
      </v-row>
    </v-card-text>
  </v-container>
</template>

<style scoped>
.v-card-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}
</style>
