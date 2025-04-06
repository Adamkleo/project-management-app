<script setup>
import { ref, computed, watch } from 'vue';

// --- Props ---
const props = defineProps({
  // Data and Configuration
  items: { // The actual data rows to display
    type: Array,
    required: true,
  },
  headers: { // Column definitions for v-data-table
    type: Array,
    required: true,
  },
  loading: { // Controls the loading state indicator
    type: Boolean,
    default: false,
  },
  title: { // Text displayed in the card title
    type: String,
    default: 'Data Table',
  },
  buttonMsg: { // Text for the button in the title
    type: String,
    default: 'Add Item',
  },
  buttonAction: { // Action to perform when the button is clicked
    type: Function,
    default: () => {},
  },
  titleIcon: { // Optional icon before the title
    type: String,
    default: 'mdi-table',
  },
  // Search Feature
  showSearch: { // Whether to display the search input field
    type: Boolean,
    default: true,
  },
  searchLabel: { // Label for the search input field
    type: String,
    default: 'Search',
  },
  searchInitialValue: { // Initial value for the search field
    type: String,
    default: ''
  },
  // Pagination
  itemsPerPage: { // Default items per page
    type: Number,
    default: 10,
  },
  itemsPerPageOptions: { // Options for the items per page dropdown
    type: Array,
    default: () => [
      { value: 10, title: '10' },
      { value: 25, title: '25' },
      { value: 50, title: '50' },
      { value: 100, title: '100' },
    ]
  },
  paginationTotalVisible: { // Max pagination links to show
    type: Number,
    default: 7
  },
  // Server-Side Operations
  server: { // Set to true if pagination/sort/filter is handled server-side
    type: Boolean,
    default: false,
  },
  serverTotalItems: { // Required if server=true: Total items available on server for pagination
    type: Number,
    default: 0,
  },
  // Action Buttons Configuration (only relevant if 'actions' key exists in headers)
  showActions: { // Whether to reserve space and potentially show default actions
    type: Boolean,
    default: false, // Default to false unless explicitly needed
  },
  showEditAction: {
    type: Boolean,
    default: true,
  },
  editActionIcon: {
    type: String,
    default: 'mdi-pencil'
  },
  editActionTooltip: {
    type: String,
    default: 'Edit Item'
  },
  showDeleteAction: {
    type: Boolean,
    default: true,
  },
  deleteActionIcon: {
    type: String,
    default: 'mdi-delete'
  },
  deleteActionColor: {
    type: String,
    default: 'error'
  },
  deleteActionTooltip: {
    type: String,
    default: 'Delete Item'
  },
  // Styling and Misc
  density: { // Table density ('default', 'compact', 'comfortable')
    type: String,
    default: 'compact',
  },
  hover: { // Enable row hover effect
    type: Boolean,
    default: true,
  },
  tableClass: { // Additional CSS classes for the v-data-table
    type: [String, Array, Object],
    default: 'elevation-1',
  },
  loadingText: { // Text displayed during loading state
    type: String,
    default: 'Loading... Please wait',
  },
});

// --- Emits ---
const emit = defineEmits([
  'edit-item',        // Emitted when the default edit icon is clicked -> (item)
  'delete-item',      // Emitted when the default delete icon is clicked -> (item)
  'update:options',   // Emitted when page, sort, or itemsPerPage changes (for server-side) -> (options)
  'update:search',    // Emitted when search term changes (useful for server-side search) -> (searchTerm)
]);

// --- Internal State ---
const page = ref(1); // Current page number
const internalSearch = ref(props.searchInitialValue); // Internal model for search field
const itemsPerPageRef = ref(props.itemsPerPage); // Internal ref for itemsPerPage

// Sync internal search with prop changes (if parent controls it)
watch(() => props.searchInitialValue, (newVal) => {
  if (newVal !== internalSearch.value) {
    internalSearch.value = newVal;
  }
});

// Sync internal itemsPerPageRef with prop changes
watch(() => props.itemsPerPage, (newVal) => {
  itemsPerPageRef.value = newVal;
}, { immediate: true });


// --- Computed Properties ---

// Calculate total items count based on client/server mode (for internal display in footer)
const filteredItemsCount = computed(() => {
  if (props.server) {
    return props.serverTotalItems; // Use server total if provided
  }
  // Basic client-side filtering simulation for length calculation
  if (!internalSearch.value) {
    return props.items.length;
  }
  // Note: This client-side filtering for count isn't perfect as v-data-table
  // does its own internal filtering. It's an estimate.
  // For accurate client-side count, v-data-table doesn't directly expose filtered count easily.
  // Usually for client-side, you don't need items-length prop on v-data-table.
  // We only need it for the pagination length calculation below.
  return props.items.filter(item =>
    Object.values(item).some(val =>
      String(val).toLowerCase().includes(internalSearch.value.toLowerCase())
    )
  ).length;
});

// Calculate total number of pages for v-pagination
const pageCount = computed(() => {
  const itemsCount = props.server ? props.serverTotalItems : filteredItemsCount.value;
  return Math.ceil(itemsCount / itemsPerPageRef.value);
});

// --- Methods ---

// Handler for v-data-table's @update:options event
function onOptionsUpdate(options) {
  // If server-side operations are enabled, emit the event for the parent component
  if (props.server) {
    emit('update:options', options);
  }
  // Update internal refs based on options changes (relevant even for client-side)
  page.value = options.page;
  itemsPerPageRef.value = options.itemsPerPage;

  // Note: Sorting updates (`options.sortBy`) are handled internally by v-data-table
  // for client-side. For server-side, the emitted event includes sortBy.
}

// Handler for search input changes
function onSearchUpdate(value) {
  // Always update internal model
  internalSearch.value = value;
  // Emit for parent if server-side filtering might be needed
  if (props.server) {
    emit('update:search', value);
  }
  // Reset to page 1 when search changes (good practice for client-side)
  page.value = 1;
}

</script>


<template>
  <v-container width="90%" fluid color>
    <v-card :loading="loading" class="mx-auto">
      <v-card-title class="d-flex align-center pe-2">
        <slot name="title-prepend">
          <v-icon :icon="titleIcon" v-if="titleIcon"></v-icon> &nbsp;
        </slot>

        {{ title }}

        <v-spacer></v-spacer>

        <v-text-field v-if="showSearch" v-model="internalSearch" density="compact" :label="searchLabel"
          prepend-inner-icon="mdi-magnify" variant="solo-filled" flat hide-details single-line class="me-2"
          style="max-width: 300px;" @update:model-value="onSearchUpdate"></v-text-field>

        <slot name="title-append"></slot>
        <v-btn color="primary" prepend-icon="mdi-plus" @click=buttonAction> {{ buttonMsg }} </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <v-data-table v-model:page="page" v-model:items-per-page="itemsPerPageRef" :headers="headers" :items="items"
        :items-length="serverTotalItems > 0 ? serverTotalItems : filteredItemsCount" :search="internalSearch"
        :loading="loading" :loading-text="loadingText" :density="density" :hover="hover" :class="tableClass"
        :items-per-page-options="itemsPerPageOptions" :server="server" @update:options="onOptionsUpdate">
        <template v-for="(_, slotName) in $slots" v-slot:[slotName]="slotProps">
          <slot :name="slotName" v-bind="slotProps"></slot>
        </template>

        <template v-if="showActions" v-slot:item.actions="{ item }">
          <slot name="item.actions" :item="item">
            <v-icon v-if="showEditAction" size="small" class="me-2" @click.stop="$emit('edit-item', item)"
              :title="editActionTooltip">
              {{ editActionIcon }}
            </v-icon>
            <v-icon v-if="showDeleteAction" size="small" :color="deleteActionColor"
              @click.stop="$emit('delete-item', item)" :title="deleteActionTooltip">
              {{ deleteActionIcon }}
            </v-icon>
          </slot>
        </template>

        <template v-slot:bottom>
          <slot name="bottom">
            <div class="text-center pt-2 pb-2">
              <v-pagination v-model="page" :length="pageCount" :total-visible="paginationTotalVisible"></v-pagination>
            </div>
          </slot>
        </template>

      </v-data-table>
    </v-card>
  </v-container>
</template>



<style scoped>
/* Add component-specific styles if needed */
.v-card-title {
  padding: 12px 16px;
  /* Example adjustment */
}
</style>