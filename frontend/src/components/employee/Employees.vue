<script setup>
import { ref, onMounted } from 'vue'; // Added onMounted
import { useRouter } from 'vue-router'; // Import useRouter
import DataTable from '@/components/general/DataTable.vue'; // Adjust path if needed
import { useEmployeeStore } from '@/stores/employeeStore'; // Your Pinia store
import Swal from 'sweetalert2';

const router = useRouter(); // Get router instance
const employeeStore = useEmployeeStore();

// Fetch employees when component is mounted
onMounted(() => {
    employeeStore.fetchEmployees();
});

// Headers specifically for the employee table (Keep as is)
const employeeHeaders = ref([
    { title: 'NIF', key: 'nif', align: 'start' },
    { title: 'Nombre', key: 'firstName', align: 'start' },
    { title: 'Primer Apellido', key: 'lastName1', align: 'start' },
    { title: 'Segundo Apellido', key: 'lastName2', align: 'start' },
    { title: 'Fecha Nacimiento', key: 'birthDate', align: 'start' }, // Consider formatting date if needed
    { title: 'Teléfono', key: 'phone1', align: 'start' },
    { title: 'Email', key: 'email', align: 'start' },
    { title: 'Estado Civil', key: 'civilStatus', align: 'start' },
    { title: 'Formación Universitaria', key: 'universityEducation', align: 'start' },
    { title: 'Acciones', key: 'actions', align: 'end', sortable: false }
]);

// --- Event Handlers ---
function handleEditEmployee(employee) {
}


async function handleTerminateEmployee(employee) {
    try {
        await employeeStore.terminateEmployee(employee.id);
        console.log('Empleado dado de baja exitosamente');
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'No se pudo desasignar',
            text: error.message
        });
    } finally {
        await employeeStore.fetchEmployees();
    }
}


// --- UPDATED: Navigate to the Add Employee form route ---
function navigateToAddEmployee() {
    console.log('Parent: Navigating to Add New Employee form');
    // Make sure '/employees/add' matches the route you define in your router config
    router.push('/empleados/add');
}

// --- Helper for custom slot (Keep as is) ---
function getStatusColor(status) {
    // This mapping depends on the actual data values for universityEducation
    // Adjust if your data uses 'S'/'N' instead of 'Sí'/'No'
    // Assuming your DataTable passes the raw item data to the slot
    const educationValue = typeof status === 'string' ? status.toUpperCase() : '';
    if (educationValue === 'S' || educationValue === 'SI' || educationValue === 'SÍ') return 'green';
    if (educationValue === 'N' || educationValue === 'NO') return 'red';
    return 'blue-grey'; // Default color
}

</script>

<template>
    <v-container fluid class="pa-8">



        <v-card-text>
            <DataTable :items="employeeStore.employees" :headers="employeeHeaders" :title-icon="'mdi-account-group'"
                :title="'Empleados'" :loading="employeeStore.isLoading" :server="false" :show-actions="true"
                :show-search="true" :button-msg="'Añadir Empleado'" search-label="Filter Employees..."
                @edit-item="handleEditEmployee" @delete-item="handleTerminateEmployee"
                :button-action="navigateToAddEmployee">
                <template #item.universityEducation="{ item }">
                    <v-chip :color="getStatusColor(item.universityEducation)" density="compact" label>
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