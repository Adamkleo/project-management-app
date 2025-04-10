<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useProjectStore } from '@/stores/projectStore';
import { useEmployeeStore } from '@/stores/employeeStore';
import { useAssignmentStore } from '@/stores/assignmentStore';
import DataTable from '@/components/general/DataTable.vue';
import Swal from 'sweetalert2';

const route = useRoute();
const router = useRouter();
const projectId = computed(() => parseInt(route.params.id));

// Initialize stores
const projectStore = useProjectStore();
const employeeStore = useEmployeeStore();
const assignmentStore = useAssignmentStore();

// State
const availableEmployees = ref([]);
const assignedEmployees = ref([]);
const project = ref(null);

// Headers for the tables
const availableEmployeesHeaders = ref([
  { title: 'Nombre', key: 'name', align: 'start' },
  { title: 'Acciones', key: 'actions', align: 'end', sortable: false }
]);

const assignedEmployeesHeaders = ref([
  { title: 'Nombre', key: 'name', align: 'start' },
  { title: 'Acciones', key: 'actions', align: 'end', sortable: false }
]);

// Load data when component is mounted
onMounted(async () => {
  try {
    // Fetch project details
    await projectStore.fetchProjects();
    project.value = projectStore.projects.find(p => p.id === projectId.value);
    
    if (!project.value) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Proyecto no encontrado'
      });
      router.push('/proyectos');
      return;
    }
    
    // Fetch all employees and assignments
    await Promise.all([
      employeeStore.fetchEmployees(),
      assignmentStore.fetchProjectAssignments(projectId.value)
    ]);
    
    // Process assignments to separate available and assigned employees
    processAssignments();
  } catch (error) {
    console.error('Error loading data:', error);
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'No se pudo cargar los datos'
    });
  }
});

// Process assignments to separate available and assigned employees
const processAssignments = () => {
  // Get all assigned employee IDs
  const assignedEmployeeIds = assignmentStore.assignments.map(
    assignment => assignment.employee.id
  );
  
  // Filter employees into available and assigned
  availableEmployees.value = employeeStore.employees
    .filter(employee => !assignedEmployeeIds.includes(employee.id))
    .map(employee => ({
      ...employee,
      name: `${employee.firstName} ${employee.lastName1} ${employee.lastName2}`
    }));
  
  // Map assignments to include employee details
  assignedEmployees.value = assignmentStore.assignments.map(assignment => ({
    ...assignment.employee,
    name: `${assignment.employee.firstName} ${assignment.employee.lastName1} ${assignment.employee.lastName2}`
  }));
};

// Handle assigning an employee to the project
const handleAssignEmployee = async (employee) => {
  try {
    // Show loading
    Swal.fire({
      title: 'Asignando empleado...',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
    
    // Assign employee
    await assignmentStore.assignEmployeeToProject(projectId.value, employee.id);
    
    // Refresh data
    await Promise.all([
      employeeStore.fetchEmployees(),
      assignmentStore.fetchProjectAssignments(projectId.value)
    ]);
    
    // Process assignments again
    processAssignments();
    
    // Show success message
    Swal.fire({
      icon: 'success',
      title: 'Éxito',
      text: 'Empleado asignado correctamente'
    });
  } catch (error) {
    console.error('Error assigning employee:', error);
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: error.message || 'No se pudo asignar el empleado'
    });
  }
};

// Handle unassigning an employee from the project
const handleUnassignEmployee = async (employee) => {
  try {
    // Confirm unassignment
    const result = await Swal.fire({
      icon: 'question',
      title: 'Confirmar desasignación',
      text: `¿Está seguro de desasignar a ${employee.name} del proyecto?`,
      showCancelButton: true,
      confirmButtonText: 'Sí, desasignar',
      cancelButtonText: 'Cancelar'
    });
    
    if (result.isConfirmed) {
      // Show loading
      Swal.fire({
        title: 'Desasignando empleado...',
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });
      
      // Unassign employee
      await assignmentStore.unassignEmployeeFromProject(projectId.value, employee.id);
      
      // Refresh data
      await Promise.all([
        employeeStore.fetchEmployees(),
        assignmentStore.fetchProjectAssignments(projectId.value)
      ]);
      
      // Process assignments again
      processAssignments();
      
      // Show success message
      Swal.fire({
        icon: 'success',
        title: 'Éxito',
        text: 'Empleado desasignado correctamente'
      });
    }
  } catch (error) {
    console.error('Error unassigning employee:', error);
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: error.message || 'No se pudo desasignar el empleado'
    });
  }
};

// Navigate back to projects list
const navigateBack = () => {
  router.push('/proyectos');
};
</script>

<template>
  <v-container fluid class="pa-8">
      <v-card-title class="d-flex align-center">
        <v-icon icon="mdi-arrow-left" class="me-2" @click="navigateBack"></v-icon>
        <span>Asignación de Empleados - {{ project?.description || 'Cargando...' }}</span>
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
              <v-card-title class="text-subtitle-1">

              </v-card-title>
              <v-card-text>
                <DataTable 
                  :items="availableEmployees" 
                  :headers="availableEmployeesHeaders" 
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
          
          <v-col cols="12" md="6">
              <v-card-title class="text-subtitle-1">
              </v-card-title>
              <v-card-text>
                <DataTable 
                  :items="assignedEmployees" 
                  :headers="assignedEmployeesHeaders" 
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