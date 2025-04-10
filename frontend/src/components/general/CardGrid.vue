<script setup>
import { computed } from 'vue';

// --- Props ---
// Define the props the component accepts
const props = defineProps({
  /**
   * An array of project objects to display.
   * Each object should have at least: id, name.
   * Optional: category, description, imageUrl, tags, link
   */
  projects: {
    type: Array,
    required: true,
    default: () => [] // Default to an empty array if not provided
  }
});

// --- Computed Properties ---
// Determine if there are any projects to display
const hasProjects = computed(() => props.projects && props.projects.length > 0);

// --- Utility Function ---
// Kept as it's used for display logic within the component
function truncateText(text, maxLength) {
  if (!text) return '';
  if (text.length <= maxLength) {
    return text;
  }
  return text.substring(0, maxLength) + '...';
}

</script>

<template>
  <v-container fluid>
    <v-row v-if="!hasProjects" justify="center">
       <v-col cols="12" md="8" lg="6">
         <v-alert type="info" variant="tonal" class="text-center my-8">
            No projects to display.
          </v-alert>
       </v-col>
    </v-row>

    <v-row v-else>
      <v-col
        v-for="project in projects"
        :key="project.id || project.name" cols="12"
        sm="6"
        md="4"
        lg="3"
        class="d-flex" >
        <v-card
          class="mx-auto project-card"
          elevation="2"
          hover
          :href="project.link || '#'"
          target="_blank"
          rel="noopener noreferrer"
        >
          <v-img
            :src="project.imageUrl" :lazy-src="`https://via.placeholder.com/150x100.png/EEEEEE/BDBDBD?text=Loading`"
            height="200px"
            aspect-ratio="16/9"
            cover
            gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
          >
            <template v-slot:placeholder>
              <v-row class="fill-height ma-0" align="center" justify="center">
                <v-progress-circular
                  indeterminate
                  color="grey-lighten-5"
                ></v-progress-circular>
              </v-row>
            </template>
            <v-card-title class="text-white project-title">{{ project.name || 'Untitled Project' }}</v-card-title>
          </v-img>

          <v-card-subtitle class="pt-4">
            {{ project.category || 'Uncategorized' }}
          </v-card-subtitle>

          <v-card-text>
            <p class="mb-2 text-body-2 project-description">
              {{ truncateText(project.description, 100) }}
            </p>
            <div v-if="project.tags && project.tags.length > 0" class="mt-2">
              <v-chip
                v-for="tag in project.tags.slice(0, 3)"
                :key="tag"
                size="small"
                class="mr-1 mb-1"
                label
                variant="tonal"
                color="primary"
              >
                {{ tag }}
              </v-chip>
              <span v-if="project.tags.length > 3" class="text-caption text-medium-emphasis ml-1">
                 +{{ project.tags.length - 3 }} more
              </span>
            </div>
             <p v-if="!project.description && (!project.tags || project.tags.length === 0)" class="text-caption text-medium-emphasis project-description">
              No details provided.
            </p>
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              v-if="project.link"
              color="primary"
              variant="text"
              :href="project.link"
              target="_blank"
              rel="noopener noreferrer"
              append-icon="mdi-open-in-new"
            >
              Learn More
            </v-btn>
             <v-btn
              v-else
              color="grey"
              variant="text"
              disabled
            >
              No Link
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.project-card {
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
  display: flex;
  flex-direction: column;
  height: 100%; /* Ensure cards in the same row take up full height of the column */
  width: 100%; /* Ensure card takes full width of the column */
}

.project-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15) !important; /* Use !important cautiously if needed */
}

.project-title {
    line-height: 1.3;
    text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.7);
    word-break: break-word; /* Prevent long titles from overflowing */
}

.project-description {
  min-height: 60px; /* Adjust as needed based on desired card consistency */
  /* Consider using line-clamp for multi-line truncation if browser support is sufficient */
  /* display: -webkit-box; */
  /* -webkit-line-clamp: 3; */
  /* -webkit-box-orient: vertical; */
  /* overflow: hidden; */
}

.v-card-actions {
    margin-top: auto; /* Pushes actions to the bottom */
}

/* Ensure columns stretch to allow cards to fill height */
.d-flex {
  display: flex;
}
</style>