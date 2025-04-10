<script setup>
import { ref } from 'vue';
// Assuming your components are in the same directory or adjust paths
import ProjectsList from '@/components/project/ProjectsList.vue'; // Your component displaying projects as a list
import ProjectsGrid from '@/components/project/ProjectsGrid.vue'; // The grid component created previously
import Button from '@/components/general/Button.vue';
 

const isGrid = ref(false); // default to list view (isGrid = false)
</script>

<template>
  <v-container fluid>  <v-row class="mb-4">
      <v-col>
        <Button
          v-model="isGrid"
          primary-text="Grid" secondary-text="List" primary-icon="mdi-view-grid"
          secondary-icon="mdi-view-list"
          class="float-end"      />
        </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <ProjectsGrid v-if="isGrid" />
         <ProjectsList v-else />
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
/* Add styles if needed */
</style>