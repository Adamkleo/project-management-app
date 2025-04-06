<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import DynamicForm from '@/components/general/DynamicForm.vue'; 
import { useEmployeeStore } from '@/stores/employeeStore'; 

// --- Import Validators ---
import {
    validateName,
    validateNIF,
    validatePhone,
    validateEmail,
    validateDateYYYYMMDD 
} from '@/utils/validators'; 


// --- Props (This is needed if editing existing employees) ---
const props = defineProps({
    employeeId: { // Pass employee ID if editing
        type: [String, Number],
        default: null
    },
    initialData: { // Pre-fill form if editing
        type: Object,
        default: () => ({})
    }
});

// --- Router and Store ---
const router = useRouter();
const employeeStore = useEmployeeStore();
const employeeDynamicFormRef = ref(null); // Ref to access DynamicForm methods if needed

// --- UI State ---
const isSubmitting = ref(false);
const showSnackbar = ref(false);
const snackbarText = ref('');
const snackbarColor = ref('success');

// --- Computed properties ---
const isEditing = computed(() => !!props.employeeId);
const formTitle = computed(() => isEditing.value ? 'Editar Empleado' : 'Añadir Nuevo Empleado');
// Use passed initialData if available (for editing), otherwise empty object for creation
const initialEmployeeData = computed(() => isEditing.value ? props.initialData : {});

// --- Form Field Definitions for Employees ---
// Use imported validators and new type/items for selects
const employeeFields = ref([
    // Group 1: Personal Info
    { id: 'firstName', label: 'Nombre', validate: validateName, group: 1 },
    { id: 'lastName1', label: 'Primer Apellido', validate: validateName, group: 1 },
    { id: 'lastName2', label: 'Segundo Apellido', group: 1 }, // Optional, no validation

    // Group 2: IDs and Date
    { id: 'nif', label: 'NIF', validate: validateNIF, group: 2 },
    // Assuming you might want a dedicated date picker component later,
    // but for now, text field with validation and placeholder
    {
        id: 'birthDate',
        label: 'Fecha nacimiento',
        validate: (v) => validateDateYYYYMMDD(v) || 'Formato YYYY-MM-DD requerido',
        group: 2,
        // You could add placeholder: 'YYYY-MM-DD' to the v-text-field directly if needed
        // Or use a mask if using vuetify-mask or similar
    },

    // Group 3: Contact Info
    { id: 'phone1', label: 'Teléfono Principal', validate: validatePhone, group: 3 },
    { id: 'phone2', label: 'Teléfono Secundario', validate: validatePhone, group: 3 }, // Could make validation optional
    { id: 'email', label: 'Correo Electrónico', validate: validateEmail, group: 3 },

    // Group 4: Employment Details & Status (using Selects)
    {
        id: 'startDate',
        label: 'Fecha de alta',
        validate: (v) => validateDateYYYYMMDD(v) || 'Formato YYYY-MM-DD requerido',
        group: 4,
    },
    {
        id: 'civilStatus',
        label: 'Estado Civil',
        type: 'select',
        items: [
            { title: 'Soltero', value: 'S' },
            { title: 'Casado', value: 'C' }
        ],
        validate: (v) => ['S', 'C'].includes(v) ? null : 'Debe seleccionar S (Soltero) o C (Casado)',
        group: 4
    },

    {
        id: 'universityEducation',
        label: 'Formación universitaria',
        type: 'select',
        items: [
            { title: 'Sí', value: 'S' },
            { title: 'No', value: 'N' }
        ],
        validate: (v) => ['S', 'N'].includes(v) ? null : 'Debe seleccionar S (Sí) o N (No)',
        group: 4
    },


    // Example of a non-grouped field (would render full width)
    // { id: 'notes', label: 'Notas Adicionales', type: 'textarea' } // Example for future enhancement
]);

// --- Submit Handler ---
const handleEmployeeSubmit = async (formData) => {
    console.log('EmployeeForm submit handler received:', formData);
    isSubmitting.value = true;

    try {
        if (isEditing.value) {
            // OPTIONAL FOR EDITING 
            // Call update action from store
            // await employeeStore.updateEmployee(props.employeeId, formData); // Ensure this action exists
            // console.log("UPDATE ACTION (needs implementation):", props.employeeId, formData); // Placeholder
            // snackbarText.value = 'Empleado actualizado correctamente!';
        } else {
            // Call add action from store
            await employeeStore.addEmployee(formData); // Ensure this action exists
            snackbarText.value = 'Empleado añadido correctamente!';
        }

        snackbarColor.value = 'success';
        showSnackbar.value = true;

        // Optional: Clear form via ref after successful submission if staying on page
        // employeeDynamicFormRef.value?.resetForm();

        // Navigate back to the list after a delay
        setTimeout(() => {
            router.push('/empleados'); // Adjust if your list route is different
        }, 1500);

    } catch (error) {
        console.error(`Error ${isEditing.value ? 'updating' : 'adding'} employee:`, error);
        snackbarText.value = `Error al ${isEditing.value ? 'actualizar' : 'añadir'} empleado. ${error.message || ''}`;
        snackbarColor.value = 'error';
        showSnackbar.value = true;
    } finally {
        isSubmitting.value = false;
    }
};

const handleCancel = () => {
    router.push('/empleados'); 
}



</script>

<template>
    <v-container>
        <v-card class="mx-auto mt-6" max-width="800" elevation="5">
            <v-card-title class="text-h5 pa-4 bg-primary">
                {{ formTitle }}
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text class="pa-5">
                <DynamicForm :fields="employeeFields" :initial-data="initialEmployeeData" :loading="isSubmitting"
                    submit-button-text="Guardar Empleado" @submit="handleEmployeeSubmit"
                    @cancel="handleCancel" ref="employeeDynamicFormRef" />
            </v-card-text>

            <v-snackbar v-model="showSnackbar" :color="snackbarColor" timeout="4000" location="top right">
                {{ snackbarText }}
                <template v-slot:actions>
                    <v-btn color="white" variant="text" @click="showSnackbar = false" icon="mdi-close"></v-btn>
                </template>
            </v-snackbar>
        </v-card>
    </v-container>
</template>