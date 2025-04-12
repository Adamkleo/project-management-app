<script setup>
import { ref, onMounted } from "vue";
import DataTable from "@/components/general/DataTable.vue";
import { useProjectStore } from "@/stores/projectStore";

defineProps({
  edit: Function,
  terminate: Function,
  add: Function,
});

const projectStore = useProjectStore();

// Cargar proyectos cuando el componente se monta
onMounted(() => {
  projectStore.fetchProjects();
});

// Cabeceras de la tabla de proyectos
const projectHeaders = ref([
  { title: "Descripcion", key: "description", align: "start" },
  { title: "Fecha Inicio", key: "startDate", align: "start" },
  // { title: 'Fecha Fin', key: 'endDate', align: 'start' },
  { title: "Ubicacion", key: "location", align: "start" },
  { title: "Acciones", key: "actions", align: "end", sortable: false },
]);
</script>

<template>
  <v-container
    fluid
    class="pa-8"
  >
    <v-card-text>
      <DataTable
        :items="projectStore.projects"
        :headers="projectHeaders"
        :title-icon="'mdi-folder-table'"
        :title="'Proyectos'"
        :loading="projectStore.isLoading"
        :server="false"
        :show-actions="true"
        :show-search="true"
        :button-msg="'Añadir Proyecto'"
        search-label="Filtrar Proyectos..."
        @edit-item="edit"
        @delete-item="terminate"
        :button-action="add"
      />
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
