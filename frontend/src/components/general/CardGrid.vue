<script setup>
import { computed } from "vue";

// Props del componente
const props = defineProps({
  items: {
    type: Array,
    required: true,
    default: () => []
  },
  // Prop para la imagen por defecto
  defaultImage: {
    type: String,
    required: true
  },
  // Props para mapear campos del objeto item
  itemKey: { // Campo a usar como :key en v-for
    type: String,
    default: 'id'
  },
  titleProp: { // Campo a usar para v-card-title
    type: String,
    default: 'title'
  },
  subtitleProp: { // Campo a usar para v-card-subtitle
    type: String,
    default: 'subtitle'
  },
  imageProp: { // Campo a usar para v-img :src (si existe en el item)
    type: String,
    default: 'image'
  },
  linkProp: { // Campo a usar para el href del card (si existe)
      type: String,
      default: 'link'
  }
});

// Verifica si hay elementos para mostrar
const hasItems = computed(() => props.items.length > 0);


</script>

<template>
  <v-container fluid>
    <v-row v-if="!hasItems" justify="center">
      <v-col cols="12" md="8" lg="6">
        <v-alert type="info" variant="tonal" class="text-center my-8">
          No hay elementos para mostrar.
        </v-alert>
      </v-col>
    </v-row>

    <v-row v-else>
      <v-col
        v-for="(item) in items"
        :key="item[itemKey] ?? JSON.stringify(item)"  
        cols="12"
        sm="6"
        md="4"
        lg="3"
        class="d-flex"
      >
        <v-card
          class="mx-auto card-item"
          elevation="2"
          hover
          :href="item[linkProp] || null"
          :target="item[linkProp] ? '_blank' : null"
          :rel="item[linkProp] ? 'noopener noreferrer' : null"
        >
          <v-img
            :src="item[imageProp] || defaultImage"
            height="180px"  
            aspect-ratio="16/9"
            cover
            gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
          >
            <template #placeholder>
              <v-row class="fill-height ma-0" align="center" justify="center">
                <v-progress-circular indeterminate color="grey-lighten-5" />
              </v-row>
            </template>
            <v-card-title v-if="item[titleProp]" class="text-white card-title">
              {{ item[titleProp] }}
            </v-card-title>
          </v-img>

          <v-card-subtitle v-if="item[subtitleProp]" class="pt-4">
            {{ item[subtitleProp] }}
          </v-card-subtitle>

          <slot name="content" :item="item"></slot>

          <v-card-actions class="card-actions-container">
            <v-spacer v-if="!$slots.actions" /> 
            <slot name="actions" :item="item">
                <v-btn
                    v-if="item[linkProp]"
                    color="primary"
                    variant="text"
                    :href="item[linkProp]"
                    target="_blank"
                    rel="noopener noreferrer"
                    append-icon="mdi-open-in-new"
                    size="small"
                    >
                    Detalles
                 </v-btn>
                 <v-btn v-else color="grey" variant="text" disabled size="small">
                    Sin acción
                 </v-btn>
            </slot>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.card-item {
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
  display: flex;
  flex-direction: column;
  height: 100%; 
  width: 100%;
}

.card-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15) !important;
}

.card-title {
  line-height: 1.3;
  font-size: 1.1rem;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.7);
  word-break: break-word; 
  padding: 12px 16px; 
  position: absolute; 
  bottom: 0;
  left: 0;
  right: 0;
}

.v-card-subtitle {
    padding-bottom: 8px; 
}

.card-actions-container {
  margin-top: auto; 
  padding: 8px 16px 12px 16px; 
}


.d-flex {
  display: flex;
}
</style>