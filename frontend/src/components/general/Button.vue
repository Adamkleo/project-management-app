<script setup>
import { computed } from "vue";

// --- Props ---
// Definición de las propiedades que recibe el componente
const props = defineProps({
  /**
   * El estado actual, tipicamente vinculado con v-model.
   * true = estado primario (por ejemplo, Grid), false = estado secundario (por ejemplo, Lista).
   */
  modelValue: {
    type: Boolean,
    required: true,
  },
  // Texto asociado al estado primario (cuando modelValue es true)
  primaryText: {
    type: String,
    default: "Grid",
  },
  // Texto asociado al estado secundario (cuando modelValue es false)
  secondaryText: {
    type: String,
    default: "List",
  },
  // Icono asociado al estado primario (cuando modelValue es true)
  primaryIcon: {
    type: String,
    default: "mdi-view-grid",
  },
  // Icono asociado al estado secundario (cuando modelValue es false)
  secondaryIcon: {
    type: String,
    default: "mdi-view-list",
  },
  // Etiqueta de texto para la acción del boton
  buttonLabel: {
    type: String,
    default: "Cambiar a",
  },
});

// --- Emits ---
// Definicion del evento emitido para compatibilidad con v-model
const emit = defineEmits(["update:modelValue"]);

// --- Propiedades computadas ---
// Determina el texto y el ícono a mostrar basado en el estado objetivo
const buttonText = computed(
  () =>
    `${props.buttonLabel} ${
      props.modelValue ? props.secondaryText : props.primaryText
    }`
);
const buttonIcon = computed(() =>
  props.modelValue ? props.secondaryIcon : props.primaryIcon
);

// Emite el evento de actualizacion para cambiar el estado en el componente padre
const toggleView = () => {
  emit("update:modelValue", !props.modelValue);
};
</script>

<template>
  <v-btn
    @click="toggleView"
    :prepend-icon="buttonIcon"
    variant="outlined"
    color="primary"
    v-bind="$attrs"
  >
    {{ buttonText }}
  </v-btn>
</template>

<style scoped></style>
