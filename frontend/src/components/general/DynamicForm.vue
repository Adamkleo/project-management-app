<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';

// Define component props
const props = defineProps({
  fields: {
    type: Array, // Array of field config objects
    required: true,
  },
  initialData: {
    type: Object,
    default: () => ({})
  },
  submitButtonText: {
    type: String,
    default: 'Submit'
  },
  loading: { // Allow parent to control loading state for the button
    type: Boolean,
    default: false
  }
});

// Define component emits
const emit = defineEmits(['submit', 'reset', 'cancel']); // Emit submit with data, and reset/cancel events

// Reactive state for form data, keyed by field id
const formData = reactive({});

// Ref for the v-form component to access validation methods
const formRef = ref(null);

// Initialize formData based on fields prop and potentially initialData
const initializeFormData = () => {
  props.fields.forEach(field => {
    // Use initialData if provided for this field, otherwise default based on type or null
    const initialValue = props.initialData?.[field.id];
    // For selects, ensure the initial value is one of the valid item values if provided
    if (field.type === 'select' && initialValue !== undefined && field.items) {
        const isValidInitial = field.items.some(item => item.value === initialValue);
        formData[field.id] = isValidInitial ? initialValue : null;
    } else {
        formData[field.id] = initialValue ?? null;
    }
  });
};

// Call initialization on mount and when fields/initialData potentially change
onMounted(initializeFormData);
// Watch deeply for changes in fields or initialData to re-initialize
watch(() => [props.fields, props.initialData], initializeFormData, { deep: true });


// Computed property to group fields based on the 'group' property
const groupedFields = computed(() => {
  const result = [];
  let currentGroup = [];

  props.fields.forEach((field, index) => {
    // Ensure field type defaults to 'text' if not provided
    const fieldWithType = { ...field, type: field.type || 'text' };
    const prevField = index > 0 ? props.fields[index - 1] : null;

    // Check if field has a group and it matches the previous field's group
    if (fieldWithType.group && prevField?.group === fieldWithType.group) {
      currentGroup.push(fieldWithType);
    } else {
      // If a group was being built, push it to results first
      if (currentGroup.length > 0) {
        result.push(currentGroup);
      }
      // Reset currentGroup
      currentGroup = [];

      // Start a new group or add a single field
      if (fieldWithType.group) {
        currentGroup.push(fieldWithType); // Start a new group with the current field
      } else {
        result.push(fieldWithType); // Add the field as a standalone item
      }
    }
  });

  // Add the last group if it wasn't pushed yet
  if (currentGroup.length > 0) {
    result.push(currentGroup);
  }

  return result;
});

// Simple column calculation for responsiveness in groups
const calculateCols = (groupLength) => {
  if (groupLength <= 1) return 12;
  if (groupLength === 2) return 6;
  if (groupLength === 3) return 4;
  return Math.floor(12 / groupLength); // Fallback for 4+ items
}

// Form submission handler
const handleSubmit = async () => {
  if (!formRef.value) return;

  // Validate the form using Vuetify's built-in method
  const { valid } = await formRef.value.validate();

  if (valid) {
    // If form is valid, emit the submit event with a clean copy of the form data
    emit('submit', JSON.parse(JSON.stringify(formData)));
  } else {
    console.log('DynamicForm validation failed');
  }
};

// Form reset handler
const handleReset = () => {
  if (!formRef.value) return;
  formRef.value.reset(); // Resets field values
  formRef.value.resetValidation(); // Resets validation states
  // Re-initialize the reactive formData to default values
  initializeFormData();
  emit('reset'); // Notify parent that reset was clicked
  console.log('DynamicForm reset');
};

const handleCancel = () => {
  handleReset(); // Reset the form first
  emit('cancel'); // Emit cancel event
};

// Expose methods like reset if parent needs to trigger them programmatically
defineExpose({
  resetForm: handleReset,
  resetValidation: () => formRef.value?.resetValidation(),
  cancelForm: handleCancel,
  validate: () => formRef.value?.validate()
});

</script>

<template>
  <v-form ref="formRef" @submit.prevent="handleSubmit">
    <template v-for="(item, index) in groupedFields" :key="`group-${index}`">
      <v-row v-if="Array.isArray(item)">
        <v-col v-for="field in item" :key="field.id" cols="12" :md="calculateCols(item.length)">
          <v-text-field
            v-if="field.type === 'text'"
            v-model="formData[field.id]"
            :label="field.label"
            :rules="field.validate ? [field.validate] : []"
            :required="!!field.validate"
            variant="outlined"
            density="compact"
            class="mb-2"
            clearable
          ></v-text-field>

          <v-select
            v-else-if="field.type === 'select'"
            v-model="formData[field.id]"
            :label="field.label"
            :items="field.items || []"
            item-title="title"
            item-value="value"
            :rules="field.validate ? [field.validate] : []"
            :required="!!field.validate"
            variant="outlined"
            density="compact"
            class="mb-2"
            clearable
          ></v-select>

          </v-col>
      </v-row>
      <v-row v-else>
        <v-col cols="12">
           <v-text-field
            v-if="item.type === 'text'"
            v-model="formData[item.id]"
            :label="item.label"
            :rules="item.validate ? [item.validate] : []"
            :required="!!item.validate"
            variant="outlined"
            density="compact"
            class="mb-2"
            clearable
          ></v-text-field>

          <v-select
            v-else-if="item.type === 'select'"
            v-model="formData[item.id]"
            :label="item.label"
            :items="item.items || []"
            item-title="title"
            item-value="value"
            :rules="item.validate ? [item.validate] : []"
            :required="!!item.validate"
            variant="outlined"
            density="compact"
            class="mb-2"
            clearable
          ></v-select>

          </v-col>
      </v-row>
    </template>

    <v-row>
       <v-col cols="12" sm="4">
         <v-btn @click="handleCancel" color="grey-darken-1" block variant="outlined">
           Cancel
         </v-btn>
       </v-col>
       <v-col cols="12" sm="4">
         <v-btn @click="handleReset" color="grey" block variant="outlined">
           Reset
         </v-btn>
       </v-col>
       <v-col cols="12" sm="4">
         <v-btn type="submit" color="primary" block :loading="loading" variant="flat">
           {{ submitButtonText }}
         </v-btn>
       </v-col>
     </v-row>
  </v-form>
</template>

<style scoped>

</style>