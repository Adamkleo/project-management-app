<script setup>
import { onMounted, computed } from "vue";
import { useProjectStore } from "@/stores/projectStore";
import CardGrid from "@/components/general/CardGrid.vue";
import innovacionImage from "@/assets/innovacion.jpg";

// Props recibidas del componente padre
defineProps({
  edit: Function,
  terminate: Function,
  add: Function,
});

// Acceso al store de proyectos
const projectStore = useProjectStore();

// Computed: lista de proyectos y estado de carga
const projects = computed(() => projectStore.projects);
const isLoading = computed(() => projectStore.isLoading);

// Cargar los proyectos cuando el componente se monta
onMounted(() => {
  projectStore.fetchProjects();
});

// Función para formatear fechas al formato local
const formatDate = (dateString) => {
  if (!dateString) return "N/A";
  try {
    const options = { year: "numeric", month: "short", day: "numeric" };
    return new Date(dateString).toLocaleDateString("es-ES", options);
  } catch (e) {
    console.warn("Formato de fecha inválido:", dateString);
    return dateString;
  }
};
</script>

<template>
  <v-container
    fluid
    class="pa-md-8 pa-4"
  >
    <!-- Encabezado y botón para añadir proyecto -->
    <v-row
      align="center"
      class="mb-4"
    >
      <v-col
        cols="12"
        sm="auto"
        class="flex-grow-1"
      >
        <h1 class="text-h5 text-sm-h4">Gestión de Proyectos</h1>
      </v-col>
      <v-col
        cols="12"
        sm="auto"
      >
        <v-btn
          color="primary"
          @click="add"
          prepend-icon="mdi-plus-circle"
          class="w-100 w-sm-auto"
        >
          Añadir Proyecto
        </v-btn>
      </v-col>
    </v-row>

    <!-- Mostrar spinner de carga si se están cargando los proyectos -->
    <v-row
      v-if="isLoading"
      justify="center"
    >
      <v-col
        cols="auto"
        class="text-center my-16"
      >
        <v-progress-circular
          indeterminate
          color="primary"
          size="64"
        />
        <p class="mt-4 text-medium-emphasis">Cargando Proyectos...</p>
      </v-col>
    </v-row>

    <!-- Grid de tarjetas de proyectos -->
    <CardGrid
      v-else
      :items="projects"
      item-key="id"
      title-prop="description"
      subtitle-prop="location"
      :default-image="innovacionImage"
    >
      <!-- Contenido principal de cada tarjeta -->
      <template #content="{ item: project }">
        <v-card-text class="pt-2 pb-1 project-details-text">
          <p class="mb-1 text-body-2">
            <v-icon
              small
              left
              >mdi-calendar-start</v-icon
            >
            <strong>Inicio:</strong> {{ formatDate(project.startDate) }}
          </p>
          <p
            v-if="project.endDate"
            class="text-body-2"
          >
            <v-icon
              small
              left
              >mdi-calendar-end</v-icon
            >
            <strong>Fin:</strong> {{ formatDate(project.endDate) }}
          </p>
          <p
            v-else
            class="text-body-2 text-medium-emphasis"
          >
            <v-icon
              small
              left
              >mdi-calendar-blank-outline</v-icon
            >
            Fin no definido
          </p>
        </v-card-text>
      </template>

      <!-- Acciones disponibles para cada tarjeta -->
      <template #actions="{ item: project }">
        <v-btn
          color="primary"
          variant="text"
          @click="edit(project)"
          prepend-icon="mdi-pencil"
          size="small"
        >
          Editar
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
          color="error"
          variant="text"
          @click="terminate(project)"
          prepend-icon="mdi-delete-outline"
          size="small"
        >
          Baja
        </v-btn>
      </template>
    </CardGrid>
  </v-container>
</template>

<style scoped>
.project-details-text p {
  display: flex;
  align-items: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.project-details-text .v-icon {
  margin-right: 6px;
  flex-shrink: 0;
}
</style>
