<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import DynamicForm from "@/components/general/DynamicForm.vue";
import { useProjectStore } from "@/stores/projectStore";

import {
  validateName,
  validateNotBlank,
  validateDateYYYYMMDD,
} from "@/utils/validators";

// --- Router y Store ---
const router = useRouter();
const projectStore = useProjectStore();
const projectDynamicFormRef = ref(null);

// --- Estado UI ---
const isSubmitting = ref(false);
const showSnackbar = ref(false);
const snackbarText = ref("");
const snackbarColor = ref("success");

const formTitle = "Añadir Proyecto"; 

// --- Campos del formulario, son los atributos necesarios para crear un proyecto
// Cada una utiliza un funcion de validacion
const projectFields = ref([
  { id: "description", label: "Descripcion", validate: validateName, group: 1 },
  {
    id: "startDate",
    label: "Fecha Inicio",
    validate: (v) => validateDateYYYYMMDD(v) || "Formato YYYY-MM-DD requerido",
    group: 1,
  },

  { id: "location", label: "Ubicacion", validate: validateName, group: 2 },
  {
    id: "observations",
    label: "Observaciones",
    validate: validateNotBlank,
    group: 2,
  },
]);

// --- Submit Handler ---
const handleProjectSubmit = async (formData) => {
  isSubmitting.value = true;

  try {
    // Call add action from store
    await projectStore.addProject(formData); // Ensure this action exists
    snackbarText.value = "Proyecto añadido correctamente!";

    snackbarColor.value = "success";
    showSnackbar.value = true;

    // If you want to stay in the form after submit, reset its fields
    // I navigate back to the employees so no need
    // projectDynamicFormRef.value?.resetForm();

    // Navigate back to the list after a delay
    setTimeout(() => {
      router.push("/proyectos"); // Adjust if your list route is different
    }, 1500);
  } catch (error) {
    console.error(`Error adding project:`, error);
    snackbarText.value = `Error al añadir proyecto. ${error.message || ""}`;
    snackbarColor.value = "error";
    showSnackbar.value = true;
  } finally {
    isSubmitting.value = false;
  }
};

const handleCancel = () => {
  router.push("/proyectos"); 
};


</script>

<template>
  <v-container>
    <v-card
      class="mx-auto mt-6"
      max-width="800"
      elevation="5"
    >
      <v-card-title class="text-h5 pa-4 bg-primary">
        {{ formTitle }}
      </v-card-title>
      <v-divider></v-divider>
      <v-card-text class="pa-5">
        <DynamicForm
          :fields="projectFields"
          :loading="isSubmitting"
          submit-button-text="Guardar Proyecto"
          @submit="handleProjectSubmit"
          @cancel="handleCancel"
          ref="projectDynamicFormRef"
        />
      </v-card-text>

      <v-snackbar
        v-model="showSnackbar"
        :color="snackbarColor"
        timeout="4000"
        location="top right"
      >
        {{ snackbarText }}
        <template v-slot:actions>
          <v-btn
            color="white"
            variant="text"
            @click="showSnackbar = false"
            icon="mdi-close"
          ></v-btn>
        </template>
      </v-snackbar>
    </v-card>
  </v-container>
</template>
