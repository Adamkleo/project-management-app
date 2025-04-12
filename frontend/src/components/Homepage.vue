<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const navigateTo = (path) => {
  router.push(path);
};
</script>

<template>
  <v-container
    fluid
    class="pa-6"
  >
    <v-row
      justify="center"
      class="text-center mb-12"
    >
      <v-col
        cols="12"
        md="8"
      >
        <h1 class="text-h3 font-weight-bold mb-4 text-primary">
          Gestión de Proyectos
        </h1>
        <p class="text-h6 font-weight-light">
          Gestiona empleados y proyectos en un solo lugar.
        </p>
      </v-col>
    </v-row>

    <v-row
      justify="center"
      dense
    >
      <!-- Empleados Card -->
      <v-col
        cols="12"
        md="5"
        lg="4"
      >
        <v-card
          class="mx-auto pa-4 d-flex flex-column fill-height"
          elevation="3"
          hover
          @click="navigateTo('/empleados')"
        >
          <v-card-item class="text-center">
            <v-icon
              size="64"
              color="primary"
              class="mb-4"
              >mdi-account-group-outline</v-icon
            >
            <v-card-title class="text-h5 mb-2"
              >Gestionar Empleados</v-card-title
            >
            <v-card-subtitle
              >Ver, añadir y gestionar detalles de empleados.</v-card-subtitle
            >
          </v-card-item>
          <v-spacer></v-spacer>
          <v-card-actions class="justify-center">
            <v-btn
              variant="tonal"
              color="primary"
            >
              Ir a Empleados
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>

      <!-- Proyectos Card -->
      <v-col
        cols="12"
        md="5"
        lg="4"
      >
        <v-card
          class="mx-auto pa-4 d-flex flex-column fill-height"
          elevation="3"
          hover
          @click="navigateTo('/proyectos')"
        >
          <v-card-item class="text-center">
            <v-icon
              size="64"
              color="secondary"
              class="mb-4"
              >mdi-briefcase-variant-outline</v-icon
            >
            <v-card-title class="text-h5 mb-2"
              >Gestionar Proyectos</v-card-title
            >
            <v-card-subtitle
              >Supervisa detalles y asignaciones de proyectos.</v-card-subtitle
            >
          </v-card-item>
          <v-spacer></v-spacer>
          <v-card-actions class="justify-center">
            <v-btn
              variant="tonal"
              color="secondary"
            >
              Ir a Proyectos
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.v-card {
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
  cursor: pointer;
}

.v-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.fill-height {
  height: 100%;
}
</style>
