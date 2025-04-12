<script setup>
import { ref, computed, watch } from "vue";

// Props del componente
const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
  headers: {
    type: Array,
    required: true,
  },
  loading: {
    type: Boolean,
    default: false,
  },
  useBackendPagination: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: "Data Table",
  },
  buttonMsg: {
    type: String,
    default: "Add Item",
  },
  buttonAction: {
    type: Function,
    default: () => {},
  },
  titleIcon: {
    type: String,
    default: "mdi-table",
  },
  showSearch: {
    type: Boolean,
    default: true,
  },
  searchLabel: {
    type: String,
    default: "Search",
  },
  searchInitialValue: {
    type: String,
    default: "",
  },
  itemsPerPage: {
    type: Number,
    default: 10,
  },
  itemsPerPageOptions: {
    type: Array,
    default: () => [
      { value: 10, title: "10" },
      { value: 25, title: "25" },
      { value: 50, title: "50" },
      { value: 100, title: "100" },
    ],
  },
  paginationTotalVisible: {
    type: Number,
    default: 7,
  },
  totalItems: {
    type: Number,
    default: 0,
  },
  showActions: {
    type: Boolean,
    default: false,
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
  density: {
    type: String,
    default: "compact",
  },
  hover: {
    type: Boolean,
    default: true,
  },
  tableClass: {
    type: [String, Array, Object],
    default: "elevation-1",
  },
  loadingText: {
    type: String,
    default: "Loading... Please wait",
  },
  showButton: {
    type: Boolean,
    default: true,
  },
});

// Eventos que emite el componente
const emit = defineEmits([
  "edit-item",
  "delete-item",
  "update:search",
  "pagination-change",
]);

// Estado interno
const paginationPage = ref(1);
const page = ref(1);
const internalSearch = ref(props.searchInitialValue);
const itemsPerPageRef = ref(props.itemsPerPage);

// Sincroniza la búsqueda si cambia desde el padre
watch(
  () => props.searchInitialValue,
  (newVal) => {
    if (newVal !== internalSearch.value) {
      internalSearch.value = newVal;
    }
  }
);

// Emitir evento cuando se cambia la paginación (solo si es backend)
watch([page, itemsPerPageRef], ([newPage, newSize]) => {
  if (props.useBackendPagination) {
    emit("pagination-change", {
      page: newPage - 1,
      size: newSize,
    });
  }
});

// Sincroniza el número de elementos por página si cambia desde el padre
watch(
  () => props.itemsPerPage,
  (newVal) => {
    itemsPerPageRef.value = newVal;
  },
  { immediate: true }
);

// Cálculo del total filtrado (solo para frontend)
const filteredItemsCount = computed(() => {
  if (props.useBackendPagination) {
    return props.items.length;
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

// Cálculo del número total de páginas
const pageCount = computed(() => {
  return props.useBackendPagination
    ? Math.ceil(props.totalItems / itemsPerPageRef.value)
    : Math.ceil(filteredItemsCount.value / itemsPerPageRef.value);
});

// Al cambiar el campo de búsqueda
function onSearchUpdate(value) {
  internalSearch.value = value;
  emit("update:search", value);

  if (!props.useBackendPagination) {
    page.value = 1;
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
        :hover="hover"
        :class="tableClass"
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
