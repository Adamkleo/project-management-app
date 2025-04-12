<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import DynamicForm from "@/components/general/DynamicForm.vue";
import { useEmployeeStore } from "@/stores/employeeStore";

// --- Import Validators ---
import {
  validateName,
  validateNIF,
  validatePhone,
  validateEmail,
  validateDateYYYYMMDD,
} from "@/utils/validators";

// --- Router y Store ---
const router = useRouter();
const employeeStore = useEmployeeStore();
const employeeDynamicFormRef = ref(null);

// --- Estado UI ---
const isSubmitting = ref(false);
const showSnackbar = ref(false);
const snackbarText = ref("");
const snackbarColor = ref("success");

// --- Atributos UI del formulario ---
const formTitle = "Añadir Nuevo Empleado";

// --- Campos del formulario, son los atributos necesarios para crear un usuario
// Cada una utiliza un funcion de validacion
const employeeFields = ref([
  // Group 1: Detalles personales
  { id: "firstName", label: "Nombre", validate: validateName, group: 1 },
  {
    id: "lastName1",
    label: "Primer Apellido",
    validate: validateName,
    group: 1,
  },
  {
    id: "lastName2",
    label: "Segundo Apellido",
    validate: validateName,
    group: 1,
  }, // Optional, no validation

  // Group 2: IDs y fechas
  { id: "nif", label: "NIF", validate: validateNIF, group: 2 },
  {
    id: "birthDate",
    label: "Fecha nacimiento",
    validate: (v) => validateDateYYYYMMDD(v) || "Formato YYYY-MM-DD requerido",
    group: 2,
  },

  // Group 3: Informacion de contacto
  {
    id: "phone1",
    label: "Teléfono Principal",
    validate: validatePhone,
    group: 3,
  },
  {
    id: "phone2",
    label: "Teléfono Secundario",
    validate: validatePhone,
    group: 3,
  },
  {
    id: "email",
    label: "Correo Electrónico",
    validate: validateEmail,
    group: 3,
  },

  // Group 4: Detalles de empleyo y estado del empleado (civil y educaion)
  {
    id: "startDate",
    label: "Fecha de alta",
    validate: (v) => validateDateYYYYMMDD(v) || "Formato YYYY-MM-DD requerido",
    group: 4,
  },
  {
    id: "civilStatus",
    label: "Estado Civil",
    type: "select",
    items: [
      { title: "Soltero", value: "S" },
      { title: "Casado", value: "C" },
    ],
    validate: (v) =>
      ["S", "C"].includes(v)
        ? null
        : "Debe seleccionar S (Soltero) o C (Casado)",
    group: 4,
  },
  {
    id: "universityEducation",
    label: "Formación universitaria",
    type: "select",
    items: [
      { title: "Sí", value: "S" },
      { title: "No", value: "N" },
    ],
    validate: (v) =>
      ["S", "N"].includes(v) ? null : "Debe seleccionar S (Sí) o N (No)",
    group: 4,
  },
]);

// --- Handle Submit con los datos recibidos del formulario ---
const handleEmployeeSubmit = async (formData) => {
  isSubmitting.value = true;

  try {
    // añade el empleado nuevo
    await employeeStore.addEmployee(formData);

    // Snackbar para mostrar que se ha añadido correctamente
    snackbarText.value = "Empleado añadido correctamente!";
    snackbarColor.value = "success";
    showSnackbar.value = true;

    // If you want to stay in the form after submit, reset its fields
    // I navigate back to the employees so no need
    // employeeDynamicFormRef.value?.resetForm();

    // Vuelve a la pagina de empleados
    setTimeout(() => {
      router.push("/empleados");
    }, 1500);
  } catch (error) {
    console.error(`Error adding employee:`, error);
    snackbarText.value = `Error al añadir empleado. ${error.message || ""}`;
    snackbarColor.value = "error";
    showSnackbar.value = true;
  } finally {
    isSubmitting.value = false;
  }
};

// -- Handle Cancel, vuelve a la pagina de empleados
const handleCancel = () => {
  router.push("/empleados");
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
          :fields="employeeFields"
          :loading="isSubmitting"
          submit-button-text="Guardar Empleado"
          @submit="handleEmployeeSubmit"
          @cancel="handleCancel"
          ref="employeeDynamicFormRef"
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
