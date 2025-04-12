<script setup>
/* NEEDS COMMENTING DOCS AND STUFF AFTER FIXING / ADDING BACKEND PAGINATION */
/* NEEDS COMMENTING DOCS AND STUFF AFTER FIXING / ADDING BACKEND PAGINATION */
/* NEEDS COMMENTING DOCS AND STUFF AFTER FIXING / ADDING BACKEND PAGINATION */
/* NEEDS COMMENTING DOCS AND STUFF AFTER FIXING / ADDING BACKEND PAGINATION */
/* NEEDS COMMENTING DOCS AND STUFF AFTER FIXING / ADDING BACKEND PAGINATION */
/* NEEDS COMMENTING DOCS AND STUFF AFTER FIXING / ADDING BACKEND PAGINATION */

import { ref, computed, watch } from "vue";

// --- Props ---
const props = defineProps({
  // Data and Configuration
  items: {
    // The actual data rows to display
    type: Array,
    required: true,
  },
  headers: {
    // Column definitions for v-data-table
    type: Array,
    required: true,
  },
  loading: {
    // Controls the loading state indicator
    type: Boolean,
    default: false,
  },
  useBackendPagination: {
    type: Boolean,
    default: false,
  },
  title: {
    // Text displayed in the card title
    type: String,
    default: "Data Table",
  },
  buttonMsg: {
    // Text for the button in the title
    type: String,
    default: "Add Item",
  },
  buttonAction: {
    // Action to perform when the button is clicked
    type: Function,
    default: () => {},
  },
  titleIcon: {
    // Optional icon before the title
    type: String,
    default: "mdi-table",
  },
  // Search Feature
  showSearch: {
    // Whether to display the search input field
    type: Boolean,
    default: true,
  },
  searchLabel: {
    // Label for the search input field
    type: String,
    default: "Search",
  },
  searchInitialValue: {
    // Initial value for the search field
    type: String,
    default: "",
  },
  // Pagination
  itemsPerPage: {
    // Default items per page
    type: Number,
    default: 10,
  },
  itemsPerPageOptions: {
    // Options for the items per page dropdown
    type: Array,
    default: () => [
      { value: 10, title: "10" },
      { value: 25, title: "25" },
      { value: 50, title: "50" },
      { value: 100, title: "100" },
    ],
  },
  paginationTotalVisible: {
    // Max pagination links to show
    type: Number,
    default: 7,
  },
  totalItems: {
    type: Number,
    default: 0,
  },

  // Action Buttons Configuration (only relevant if 'actions' key exists in headers)
  showActions: {
    // Whether to reserve space and potentially show default actions
    type: Boolean,
    default: false, // Default to false unless explicitly needed
  },
  showEditAction: {
    type: Boolean,
    default: true,
  },
  editActionIcon: {
    type: String,
    default: "mdi-pencil",
  },
  editActionTooltip: {
    type: String,
    default: "Edit Item",
  },
  showDeleteAction: {
    type: Boolean,
    default: true,
  },
  deleteActionIcon: {
    type: String,
    default: "mdi-delete",
  },
  deleteActionColor: {
    type: String,
    default: "error",
  },
  deleteActionTooltip: {
    type: String,
    default: "Delete Item",
  },
  // Styling and Misc
  density: {
    // Table density ('default', 'compact', 'comfortable')
    type: String,
    default: "compact",
  },
  hover: {
    // Enable row hover effect
    type: Boolean,
    default: true,
  },
  tableClass: {
    // Additional CSS classes for the v-data-table
    type: [String, Array, Object],
    default: "elevation-1",
  },
  loadingText: {
    // Text displayed during loading state
    type: String,
    default: "Loading... Please wait",
  },
  showSelect: {
    type: Boolean,
    default: false,
  },
  showButton: {
    type: Boolean,
    default: true,
  },
  selected: {
    type: Array,
    default: () => [],
  },
});

// --- Emits ---
const emit = defineEmits([
  "edit-item", // Emitted when the default edit icon is clicked -> (item)
  "delete-item", // Emitted when the default delete icon is clicked -> (item)
  "update:search", // Emitted when search term changes (useful for server-side search) -> (searchTerm)
  "update:selected", // Emitted when selected items change -> (selectedItems)
  "pagination-change", //
]);

// --- Internal State ---
const paginationPage = ref(1); // for backend pagination only
const page = ref(1); // for frontend pagination

const internalSearch = ref(props.searchInitialValue); // Internal model for search field
const itemsPerPageRef = ref(props.itemsPerPage); // Internal ref for itemsPerPage
const internalSelected = ref(props.selected); // Internal ref for selected items

// Sync internal search with prop changes (if parent controls it)
watch(
  () => props.searchInitialValue,
  (newVal) => {
    if (newVal !== internalSearch.value) {
      internalSearch.value = newVal;
    }
  }
);

watch([page, itemsPerPageRef], ([newPage, newSize]) => {
  if (props.useBackendPagination) {
    emit("pagination-change", {
      page: newPage - 1, // backend is usually 0-indexed
      size: newSize,
    });
  }
});

// Sync internal itemsPerPageRef with prop changes
watch(
  () => props.itemsPerPage,
  (newVal) => {
    itemsPerPageRef.value = newVal;
  },
  { immediate: true }
);

// Sync internal selected with prop changes
watch(
  () => props.selected,
  (newVal) => {
    internalSelected.value = newVal;
  },
  { immediate: true }
);

// Watch for changes to internal selected and emit to parent
watch(
  () => internalSelected.value,
  (newVal) => {
    emit("update:selected", newVal);
  }
);

// --- Computed Properties ---

// Calculate filtered items count for search functionality
const filteredItemsCount = computed(() => {
  if (props.useBackendPagination) {
    return props.items.length; // backend already filtered
  }

  if (!internalSearch.value) {
    return props.items.length;
  }

  return props.items.filter((item) =>
    Object.values(item).some((val) =>
      String(val).toLowerCase().includes(internalSearch.value.toLowerCase())
    )
  ).length;
});

// Calculate total number of pages for v-pagination
const pageCount = computed(() => {
  return props.useBackendPagination
    ? Math.ceil(props.totalItems / itemsPerPageRef.value)
    : Math.ceil(filteredItemsCount.value / itemsPerPageRef.value);
});

// --- Methods ---

// Handler for search input changes
function onSearchUpdate(value) {
  internalSearch.value = value;
  emit("update:search", value);

  if (!props.useBackendPagination) {
    page.value = 1; // reset only for frontend
  }
}
</script>

<template>
  <v-container
    width="90%"
    fluid
    color
  >
    <v-card
      :loading="loading"
      class="mx-auto"
    >
      <v-card-title class="d-flex align-center pe-2">
        <slot name="title-prepend">
          <v-icon
            :icon="titleIcon"
            v-if="titleIcon"
          ></v-icon>
          &nbsp;
        </slot>

        {{ title }}

        <v-spacer></v-spacer>

        <v-text-field
          v-if="showSearch"
          v-model="internalSearch"
          density="compact"
          :label="searchLabel"
          prepend-inner-icon="mdi-magnify"
          variant="solo-filled"
          flat
          hide-details
          single-line
          class="me-2"
          style="max-width: 300px"
          @update:model-value="onSearchUpdate"
        ></v-text-field>

        <slot name="title-append"></slot>

        <v-btn
          v-if="showButton"
          color="primary"
          prepend-icon="mdi-plus"
          @click="buttonAction"
        >
          {{ buttonMsg }}
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <v-data-table
        :page="useBackendPagination ? undefined : page"
        @update:page="
          (val) => {
            if (!useBackendPagination) page = val;
          }
        "
        v-model:items-per-page="itemsPerPageRef"
        :headers="headers"
        :items="items"
        :search="internalSearch"
        :loading="loading"
        :loading-text="loadingText"
        :density="density"
        :show-select="showSelect"
        :hover="hover"
        :class="tableClass"
        v-model:selected="internalSelected"
        :items-per-page-options="itemsPerPageOptions"
      >
        <template
          v-for="(_, slotName) in $slots"
          v-slot:[slotName]="slotProps"
        >
          <slot
            :name="slotName"
            v-bind="slotProps"
          ></slot>
        </template>

        <template
          v-if="showActions"
          v-slot:item.actions="{ item }"
        >
          <slot
            name="item.actions"
            :item="item"
          >
            <v-icon
              v-if="showEditAction"
              size="small"
              class="me-2"
              @click.stop="$emit('edit-item', item)"
              :title="editActionTooltip"
            >
              {{ editActionIcon }}
            </v-icon>
            <v-icon
              v-if="showDeleteAction"
              size="small"
              :color="deleteActionColor"
              @click.stop="$emit('delete-item', item)"
              :title="deleteActionTooltip"
            >
              {{ deleteActionIcon }}
            </v-icon>
          </slot>
        </template>

        <template v-slot:bottom>
          <slot name="bottom">
            <div class="text-center pt-2 pb-2">
              <v-pagination
                v-if="useBackendPagination"
                :model-value="paginationPage"
                @update:model-value="
                  (val) => {
                    paginationPage = val;
                    emit('pagination-change', {
                      page: val - 1,
                      size: itemsPerPageRef,
                    });
                  }
                "
                :length="pageCount"
                :total-visible="paginationTotalVisible"
              />

              <v-pagination
                v-else
                v-model="page"
                :length="pageCount"
                :total-visible="paginationTotalVisible"
              />
            </div>
          </slot>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<style scoped>
.v-card-title {
  padding: 12px 16px;
}
</style>
