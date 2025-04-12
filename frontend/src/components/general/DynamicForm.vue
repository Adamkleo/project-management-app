<script setup>
import { ref, reactive, computed, onMounted, watch } from "vue";

// Props del componente
const props = defineProps({
  fields: {
    type: Array,
    required: true,
  },
  submitButtonText: {
    type: String,
    default: "Submit",
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

// Eventos que el componente puede emitir
const emit = defineEmits(["submit", "reset", "cancel"]);

// Estado reactivo del formulario
const formData = reactive({});
const formRef = ref(null);

// Agrupa los campos según la propiedad 'group'
const groupedFields = computed(() => {
  const result = [];
  let currentGroup = [];

  props.fields.forEach((field, index) => {
    const fieldWithType = { ...field, type: field.type || "text" };
    const prevField = index > 0 ? props.fields[index - 1] : null;

    if (fieldWithType.group && prevField?.group === fieldWithType.group) {
      currentGroup.push(fieldWithType);
    } else {
      if (currentGroup.length > 0) {
        result.push(currentGroup);
      }
      currentGroup = [];

      if (fieldWithType.group) {
        currentGroup.push(fieldWithType);
      } else {
        result.push(fieldWithType);
      }
    }
  });

  if (currentGroup.length > 0) {
    result.push(currentGroup);
  }

  return result;
});

// Determina cuántas columnas usar según el tamaño del grupo
const calculateCols = (groupLength) => {
  if (groupLength <= 1) return 12;
  if (groupLength === 2) return 6;
  if (groupLength === 3) return 4;
  return Math.floor(12 / groupLength);
};

// Envía el formulario si es válido
const handleSubmit = async () => {
  if (!formRef.value) return;

  const { valid } = await formRef.value.validate();

  if (valid) {
    emit("submit", JSON.parse(JSON.stringify(formData)));
  } else {
    console.log("Validación fallida");
  }
};

// Reinicia el formulario
const handleReset = () => {
  if (!formRef.value) return;
  formRef.value.reset();
  formRef.value.resetValidation();
  emit("reset");
  console.log("Formulario reiniciado");
};

// Cancela el formulario y notifica al padre
const handleCancel = () => {
  handleReset();
  emit("cancel");
};

// Expone métodos al componente padre
defineExpose({
  resetForm: handleReset,
  resetValidation: () => formRef.value?.resetValidation(),
  cancelForm: handleCancel,
  validate: () => formRef.value?.validate(),
});
</script>

<template>
  <v-form
    ref="formRef"
    @submit.prevent="handleSubmit"
  >
    <template
      v-for="(item, index) in groupedFields"
      :key="`group-${index}`"
    >
      <v-row v-if="Array.isArray(item)">
        <v-col
          v-for="field in item"
          :key="field.id"
          cols="12"
          :md="calculateCols(item.length)"
        >
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
      <v-col
        cols="12"
        sm="4"
      >
        <v-btn
          @click="handleCancel"
          color="grey-darken-1"
          block
          variant="outlined"
        >
          Cancel
        </v-btn>
      </v-col>
      <v-col
        cols="12"
        sm="4"
      >
        <v-btn
          @click="handleReset"
          color="grey"
          block
          variant="outlined"
        >
          Reset
        </v-btn>
      </v-col>
      <v-col
        cols="12"
        sm="4"
      >
        <v-btn
          type="submit"
          color="primary"
          block
          :loading="loading"
          variant="flat"
        >
          {{ submitButtonText }}
        </v-btn>
      </v-col>
    </v-row>
  </v-form>
</template>

<style scoped></style>
