<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

import Swal from "sweetalert2";

import { useEmployeeStore } from "@/stores/employeeStore";
import DataTable from "@/components/general/DataTable.vue";

const router = useRouter();
const employeeStore = useEmployeeStore();

// Fetch employees when component is mounted
onMounted(() => {
  employeeStore.fetchPaginatedEmployees({ page: 0, size: 10 });
});

// Las cabeceras necesarias para la tabla de empleados, es decir, los atributos de los empleados.
const employeeHeaders = ref([
  { title: "NIF", key: "nif", align: "start" },
  { title: "Nombre", key: "firstName", align: "start" },
  { title: "Primer Apellido", key: "lastName1", align: "start" },
  { title: "Segundo Apellido", key: "lastName2", align: "start" },
  { title: "Fecha Nacimiento", key: "birthDate", align: "start" },
  { title: "Teléfono", key: "phone1", align: "start" },
  { title: "Email", key: "email", align: "start" },
  { title: "Estado Civil", key: "civilStatus", align: "start" },
  {
    title: "Formación Universitaria",
    key: "universityEducation",
    align: "start",
  },
  { title: "Acciones", key: "actions", align: "end", sortable: false },
]);

async function loadUsers({ page, size }) {
  console.log("Requested page:", page, "size:", size);
  await employeeStore.fetchPaginatedEmployees({ page, size });
  console.log("Loaded employees:", employeeStore.employees);
}


// --- Dar de baja a un empleado usando su ID y la API a traves del store de empleados con Pinia ---
async function handleTerminateEmployee(employee) {
  try {
    await employeeStore.terminateEmployee(employee.id);
    console.log("Empleado dado de baja exitosamente");
  } catch (error) {
    Swal.fire({
      icon: "error",
      title: "No se pudo desasignar",
      text: error.message,
    });
  } finally {
    // Haz fetch para actualizar (refrescar) la lista de empleados
    await employeeStore.fetchEmployees();
  }
}

// --- Navegar al formulario para añadir un nuevo empleado ---
function navigateToAddEmployee() {
  router.push("/empleados/add");
}

// --- Función auxiliar para slot personalizado ---
function getStatusColor(status) {
  // Este mapeo depende de los valores reales en los datos de universityEducation
  // Ajusta si tus datos usan 'S'/'N' en lugar de 'Sí'/'No'
  const educationValue = typeof status === "string" ? status.toUpperCase() : "";
  if (
    educationValue === "S" ||
    educationValue === "SI" ||
    educationValue === "SÍ"
  )
    return "green";
  if (educationValue === "N" || educationValue === "NO") return "red";
  return "blue-grey"; // Color por defecto
}
</script>

<template>
  <v-container
    fluid
    class="pa-8"
  >
    <v-card-text>
      <DataTable
        :items="employeeStore.employees"
        :headers="employeeHeaders"
        :title-icon="'mdi-account-group'"
        :title="'Empleados'"
        :loading="employeeStore.isLoading"
        :server="false"
        :show-actions="true"
        :show-search="true"
        :use-backend-pagination="true"
        @pagination-change="loadUsers"
        :button-msg="'Añadir Empleado'"
        search-label="Filter Employees..."
        @delete-item="handleTerminateEmployee"
        :button-action="navigateToAddEmployee"
        :total-items="employeeStore.totalEmployees"
      >
        <template #item.universityEducation="{ item }">
          <v-chip
            :color="getStatusColor(item.universityEducation)"
            density="compact"
            label
          >
            {{ item.universityEducation?.toUpperCase() }}
          </v-chip>
        </template>
      </DataTable>
    </v-card-text>
  </v-container>
</template>

<style scoped>
.v-card-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

::v-deep(.v-icon.mdi-account-group) {
  color: rgb(var(--v-theme-primary));
}
</style>
