<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import DynamicForm from '@/components/general/DynamicForm.vue';
import { useProjectStore } from '@/stores/projectStore';

// --- Import Validators ---
import {
    validateName,
    validateNotBlank,
    validateDateYYYYMMDD // Assuming you have or create this
} from '@/utils/validators';


// --- Props (This is needed if editing existing employees) ---
const props = defineProps({
    projectId: { // Pass project ID if editing
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
const projectStore = useProjectStore();
const projectDynamicFormRef = ref(null); // Ref to access DynamicForm methods if needed

// --- UI State ---
const isSubmitting = ref(false);
const showSnackbar = ref(false);
const snackbarText = ref('');
const snackbarColor = ref('success');

// --- Computed properties ---
const isEditing = computed(() => !!props.projectId);
const formTitle = computed(() => isEditing.value ? 'Editar Proyecto' : 'Añadir Nuevo Proyecto');
// Use passed initialData if available (for editing), otherwise empty object for creation
const initialProjectData = computed(() => isEditing.value ? props.initialData : {});

// --- Form Field Definitions for Employees ---
// Use imported validators and new type/items for selects
const projectFields = ref([
    { id: 'description', label: 'Descripcion', validate: validateName, group: 1 },
    {
        id: 'startDate',
        label: 'Fecha Inicio',
        validate: (v) => validateDateYYYYMMDD(v) || 'Formato YYYY-MM-DD requerido',
        group: 1,
    },

    { id: 'location', label: 'Ubicacion', validate: validateName, group: 2 },
    { id: 'observations', label: 'Observaciones', validate: validateNotBlank, group: 2 },
]);

// --- Submit Handler ---
const handleProjectSubmit = async (formData) => {
    console.log('Project submit handler received:', formData);
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
            await projectStore.addProject(formData); // Ensure this action exists
            snackbarText.value = 'Proyecto añadido correctamente!';
        }

        snackbarColor.value = 'success';
        showSnackbar.value = true;

        // Optional: Clear form via ref after successful submission if staying on page
        // employeeDynamicFormRef.value?.resetForm();

        // Navigate back to the list after a delay
        setTimeout(() => {
            router.push('/proyectos'); // Adjust if your list route is different
        }, 1500);

    } catch (error) {
        console.error(`Error ${isEditing.value ? 'updating' : 'adding'} project:`, error);
        snackbarText.value = `Error al ${isEditing.value ? 'actualizar' : 'añadir'} proyecto. ${error.message || ''}`;
        snackbarColor.value = 'error';
        showSnackbar.value = true;
    } finally {
        isSubmitting.value = false;
    }
};

const handleCancel = () => {
    router.push('/proyectos'); 
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
                <DynamicForm :fields="projectFields" :initial-data="initialProjectData" :loading="isSubmitting"
                    submit-button-text="Guardar Proyecto" @submit="handleProjectSubmit"
                    @cancel="handleCancel" ref="projectDynamicFormRef" />
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