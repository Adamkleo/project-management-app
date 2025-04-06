<script setup>
import { ref, onMounted } from 'vue'; // Added onMounted
import { useRouter } from 'vue-router'; // Import useRouter
import DataTable from '@/components/general/DataTable.vue'; // Adjust path if needed
import { useProjectStore } from '@/stores/projectStore';
import Swal from 'sweetalert2';

const router = useRouter(); // Get router instance
const projectStore = useProjectStore();

// Fetch employees when component is mounted
onMounted(() => {
    projectStore.fetchProjects();
});

const projectHeaders = ref([
    { title: 'Descripcion', key: 'description', align: 'start' },
    { title: 'Fecha Inicio', key: 'startDate', align: 'start' },
    // { title: 'Fecha Fin', key: 'endDate', align: 'start' },
    { title: 'Ubicacion', key: 'location', align: 'start' },
    { title: 'Acciones', key: 'actions', align: 'end', sortable: false }
]);


// --- Event Handlers ---
const handleEditProject = async (project) => {
    console.log("Editing project")
}

const handleTerminateProject = async (project) => {
    try {
        await projectStore.terminateProject(project.id);
        console.log('Proyecto dado de baja exitosamente')
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'No se pudo desasignar',
            text: error.message
        })
    } finally {
        await projectStore.fetchProjects()
    }
}


const navigateToAddProject = () => {
    router.push('/proyectos/add')
}


</script>


<template>
    <v-container fluid class="pa-8">



        <v-card-text>
            <DataTable :items="projectStore.projects" :headers="projectHeaders" :title-icon="'mdi-folder-table'"
                :title="'Proyectos'" :loading="projectStore.isLoading" :server="false" :show-actions="true"
                :show-search="true" :button-msg="'Añadir Proyecto'" search-label="Filtrar Proyectos..."
                @edit-item="handleEditProject" @delete-item="handleTerminateProject"
                :button-action="navigateToAddProject">
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

::v-deep(.v-icon.mdi-folder-table) {
  color: rgb(var(--v-theme-primary));
}


</style>