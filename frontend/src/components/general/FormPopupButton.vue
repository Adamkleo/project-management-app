
<script>
import Swal from 'sweetalert2'

export default {
  name: 'FormPopupButton',

  props: {
    label: {
      type: String,
      default: 'Open Form'
    },
    color: {
      type: String,
      default: 'white'
    },
    fields: {
      type: Array,
      required: true
      // Each field: { id: 'firstName', placeholder: 'Nombre', validate?: (value) => string | null }
    }
  },

  methods: {
    async showForm() {
      const html = this.fields
        .map(field => `<input id="swal-${field.id}" class="swal2-input" placeholder="${field.placeholder}" />`)
        .join('');

      const { value: formValues } = await Swal.fire({
        title: this.label,
        html,
        focusConfirm: false,
        preConfirm: () => {
          const result = {};

          for (const field of this.fields) {
            const value = document.getElementById(`swal-${field.id}`).value.trim();

            // Custom validation
            if (field.validate) {
              const error = field.validate(value);
              if (error) {
                Swal.showValidationMessage(error);
                return false;
              }
            } else if (!value) {
              Swal.showValidationMessage('Todos los campos son obligatorios');
              return false;
            }

            result[field.id] = value;
          }

          return result;
        }
      });

      if (formValues) {
        this.$emit('submit', formValues);
      }
    }
  }
}
</script>


<template>
  <v-btn :color="color" @click="showForm">{{ label }}</v-btn>
</template>
