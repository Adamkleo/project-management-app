<script setup>
import { computed } from 'vue';

// --- Props ---
const props = defineProps({
  /**
   * The current state, typically bound with v-model.
   * true = primary state (e.g., Grid), false = secondary state (e.g., List).
   */
  modelValue: {
    type: Boolean,
    required: true
  },
  /**
   * Text associated with the primary state (when modelValue is true).
   */
  primaryText: {
    type: String,
    default: 'Grid'
  },
  /**
   * Text associated with the secondary state (when modelValue is false).
   */
  secondaryText: {
    type: String,
    default: 'List'
  },
   /**
   * Icon associated with the primary state (when modelValue is true).
   */
  primaryIcon: {
    type: String,
    default: 'mdi-view-grid' // Default Grid icon
  },
  /**
   * Icon associated with the secondary state (when modelValue is false).
   */
  secondaryIcon: {
    type: String,
    default: 'mdi-view-list' // Default List icon
  },
  /**
   * Text label for the button's action.
   */
  buttonLabel: {
    type: String,
    default: 'Switch to' // Default label prefix
  }
});

// --- Emits ---
// Define the event emitted for v-model compatibility
const emit = defineEmits(['update:modelValue']);

// --- Computed Properties ---
// Determine the text and icon to display based on the *target* state
const buttonText = computed(() => `${props.buttonLabel} ${props.modelValue ? props.secondaryText : props.primaryText}`);
const buttonIcon = computed(() => props.modelValue ? props.secondaryIcon : props.primaryIcon);

// --- Methods ---
// Emit the update event to toggle the state in the parent component
const toggleView = () => {
  emit('update:modelValue', !props.modelValue);
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

<style scoped>

</style>

