<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import Swal from "sweetalert2";

import { useProjectStore } from "@/stores/projectStore";
import ProjectsList from "@/components/project/ProjectsList.vue";
import ProjectsGrid from "@/components/project/ProjectsGrid.vue";
import Button from "@/components/general/Button.vue";

// Router y Store
const router = useRouter();
const projectStore = useProjectStore();

// Estado para alternar entre vista de lista y grid
const isGrid = ref(false);

// Props para pasar funciones al componente hijo (solo necesario si este componente es hijo, pero aquí no se usan)
defineProps({
  edit: Function,
  terminate: Function,
  add: Function
});

// Truncar texto largo para mostrar en SweetAlert
function truncateText(text, maxLength) {
  if (!text) return "";
  return text.length <= maxLength ? text : text.substring(0, maxLength) + "...";
}

// Editar proyecto --> redirige a la vista de asignación
const handleEditProject = (project) => {
  if (!project || !project.id) {
    console.error("Edit action called without valid project ID.");
    return;
  }
  router.push(`/proyectos/assign/${project.id}`);
};

// Dar de baja proyecto --> confirmación --> llamada a backend
const handleTerminateProject = async (project) => {
  if (!project || !project.id) {
    console.error("Terminate action called without valid project ID.");
    return;
  }

  const result = await Swal.fire({
    title: "¿Está seguro?",
    text: `¿Realmente desea dar de baja el proyecto "${truncateText(project.description, 50)}"? Esta acción podría ser irreversible.`,
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "Sí, dar de baja",
    cancelButtonText: "Cancelar",
  });

  if (result.isConfirmed) {
    try {
      Swal.fire({ 
        title: "Procesando...",
        text: "Dando de baja el proyecto.",
        allowOutsideClick: false,
        didOpen: () => Swal.showLoading()
      });

      await projectStore.terminateProject(project.id);
      await projectStore.fetchProjects(); // Refrescar lista

      Swal.fire("¡Dado de baja!", "El proyecto ha sido dado de baja exitosamente.", "success");
    } catch (error) {
      console.error("Failed to terminate project:", error);
      Swal.fire({
        icon: "error",
        title: "Error al dar de baja",
        text: error.message || "Ocurrió un error inesperado."
      });
    }
  }
};

// ➕ Navegar al formulario para añadir proyecto
const navigateToAddProject = () => {
  router.push("/proyectos/add");
};
</script>

<template>
  <v-container fluid>
    <!-- Botón para alternar entre grid y lista -->
    <v-row class="mb-4">
      <v-col>
        <Button
          v-model="isGrid"
          primary-text="Grid"
          secondary-text="List"
          primary-icon="mdi-view-grid"
          secondary-icon="mdi-view-list"
          class="float-end"
        />
      </v-col>
    </v-row>

    <!-- Renderizar vista correspondiente -->
    <v-row>
      <v-col cols="12">
        <ProjectsGrid
          v-if="isGrid"
          :edit="handleEditProject"
          :terminate="handleTerminateProject"
          :add="navigateToAddProject"
        />

        <ProjectsList
          v-else
          :edit="handleEditProject"
          :terminate="handleTerminateProject"
          :add="navigateToAddProject"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped></style>
