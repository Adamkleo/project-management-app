<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useProjectStore } from '@/stores/projectStore'; // Adjust path if needed
import Swal from 'sweetalert2';

// --- Store and Router ---
const router = useRouter();
const projectStore = useProjectStore();

// --- Computed Properties ---
// Expose projects and loading state for the template
const projects = computed(() => projectStore.projects);
const isLoading = computed(() => projectStore.isLoading);
// Assuming your store might have an error state (good practice)
// const error = computed(() => projectStore.error);

// --- Lifecycle Hook ---
// Fetch projects when the component is mounted
onMounted(() => {
    // Check if data needs fetching (optional optimization)
    // if (!projects.value || projects.value.length === 0) {
         projectStore.fetchProjects();
    // }
});

// --- Utility Function (from original grid component) ---
function truncateText(text, maxLength) {
  if (!text) return '';
  if (text.length <= maxLength) {
    return text;
  }
  return text.substring(0, maxLength) + '...';
}

// --- Event Handlers ---
const handleEditProject = (project) => {
    if (!project || !project.id) {
        console.error("Edit action called without valid project ID.");
        return;
    }
    router.push(`/proyectos/assign/${project.id}`);
}

const handleTerminateProject = async (project) => {
    if (!project || !project.id) {
        console.error("Terminate action called without valid project ID.");
        return;
    }

    // Confirmation Dialog
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: `¿Realmente desea dar de baja el proyecto "${truncateText(project.description, 50)}"? Esta acción podría ser irreversible.`, // Use description or name if available
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33', // Red for terminate
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, dar de baja',
        cancelButtonText: 'Cancelar'
    });

    // Proceed only if confirmed
    if (result.isConfirmed) {
        try {
            // Show loading state within Swal while terminating
            Swal.showLoading();
            await projectStore.terminateProject(project.id);
            Swal.fire(
                '¡Dado de baja!',
                'El proyecto ha sido dado de baja exitosamente.',
                'success'
            );
            // Refetch projects list after successful termination
            await projectStore.fetchProjects();
        } catch (error) {
            console.error("Failed to terminate project:", error);
            Swal.fire({
                icon: 'error',
                title: 'Error al dar de baja',
                text: error.message || 'Ocurrió un error inesperado.'
            });
            // Optionally refetch even on error if state might be inconsistent
            // await projectStore.fetchProjects();
        }
    }
}

const navigateToAddProject = () => {
    router.push('/proyectos/add');
}

// --- Helper function to format dates (optional) ---
const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  try {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  } catch (e) {
    return dateString; // Return original if formatting fails
  }
};

</script>

<template>
    <v-container fluid class="pa-md-8 pa-4">

        <v-row align="center" class="mb-4">
        
            <v-col cols="12" sm="auto" class="flex-grow-1">
                <h1 class="text-h5 text-sm-h4">Gestión de Proyectos</h1>
            </v-col>
            <v-col cols="12" sm="auto">
                 <v-btn
                    color="primary"
                    @click="navigateToAddProject"
                    prepend-icon="mdi-plus-circle"
                    class="w-100 w-sm-auto"
                 >
                    Añadir Proyecto
                 </v-btn>
            </v-col>
        </v-row>

        <v-row v-if="isLoading" justify="center">
            <v-col cols="auto" class="text-center my-16">
                <v-progress-circular
                    indeterminate
                    color="primary"
                    size="64"
                ></v-progress-circular>
                <p class="mt-4 text-medium-emphasis">Cargando Proyectos...</p>
            </v-col>
        </v-row>

        <v-row v-else-if="!projects || projects.length === 0" justify="center">
            <v-col cols="12" md="8" lg="6">
                <v-alert type="info" variant="tonal" class="text-center my-8" icon="mdi-information-outline">
                    No se encontraron proyectos. ¡Intenta añadir uno!
                </v-alert>
            </v-col>
        </v-row>

        <v-row v-else>
            <v-col
                v-for="project in projects"
                :key="project.id"
                cols="12"
                sm="6"
                md="4"
                lg="3"
                class="d-flex"
            >
                <v-card
                    class="mx-auto project-card"
                    elevation="2"
                    hover
                >
                    <v-sheet color="grey-lighten-3" height="150px" class="d-flex align-center justify-center">
                         <v-icon size="64" color="grey-darken-1">mdi-folder-table</v-icon>
                         </v-sheet>

                    <v-card-title class="pb-1 project-title">
                        {{ truncateText(project.description, 60) || 'Proyecto sin descripción' }}
                    </v-card-title>

                    <v-card-subtitle class="pt-0">
                        <v-icon small left>mdi-map-marker-outline</v-icon>
                        {{ project.location || 'Ubicación no especificada' }}
                    </v-card-subtitle>

                    <v-card-text class="pt-2 pb-1">
                        <p class="mb-2 text-body-2">
                           <v-icon small left>mdi-calendar-start</v-icon>
                           <strong>Inicio:</strong> {{ formatDate(project.startDate) }}
                        </p>
                        </v-card-text>

                    <v-spacer></v-spacer> <v-card-actions class="pt-0">
                        <v-btn
                            color="primary"
                            variant="text"
                            @click="handleEditProject(project)"
                            prepend-icon="mdi-pencil"
                            size="small"
                        >
                            Editar
                        </v-btn>
                        <v-spacer></v-spacer>
                        <v-btn
                            color="error"
                            variant="text"
                            @click="handleTerminateProject(project)"
                            prepend-icon="mdi-delete-outline"
                            size="small"
                        >
                            Baja
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>

    </v-container>
</template>

<style scoped>
/* Bring in styles from the original grid component */
.project-card {
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}

.project-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12) !important;
}

.project-title {
    line-height: 1.3;
    font-size: 1rem; /* Adjust title size if needed */
    font-weight: 500;
    min-height: 42px; /* Reserve space for ~2 lines */
    word-break: break-word;
}

.v-card-actions {
    margin-top: auto; /* Ensures actions are at the bottom */
    padding-left: 16px;
    padding-right: 16px;
    padding-bottom: 12px;
}

.v-card-text p {
    display: flex;
    align-items: center; /* Align icon and text */
}
.v-card-text .v-icon {
    margin-right: 6px; /* Space between icon and text */
}

/* Ensure columns stretch */
.d-flex {
  display: flex;
}

/* Adjust padding for different screen sizes */
.pa-md-8 {
    padding: 32px !important;
}
.pa-4 {
     padding: 16px !important;
}

/* Responsive button width */
.w-sm-auto {
    @media (min-width: 600px) {
        width: auto !important;
    }
}
.w-100 {
    width: 100%;
}
</style>