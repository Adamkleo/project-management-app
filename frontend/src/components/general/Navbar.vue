<script setup></script>

<template>
  <v-app-bar
    flat
    class="border-b"
    density="compact"
  >
    <template v-slot:prepend>
      <router-link to="/">
        <v-img
          src="@/assets/fs_logo_full.png"
          alt="Logo"
          height="42"
          width="128"
          class="noselect ml-4 mr-2"
          contain
        />
      </router-link>
    </template>

    <v-spacer></v-spacer>

    <div class="d-flex align-center">
      <v-btn
        variant="text"
        class="nav-item mx-2 font-weight-medium text-capitalize"
        to="/"
        router
      >
        Home
      </v-btn>

      <v-btn
        variant="text"
        class="nav-item mx-2 font-weight-medium text-capitalize"
        to="/empleados"
        router
      >
        Empleados
      </v-btn>

      <v-btn
        variant="text"
        class="nav-item mx-2 font-weight-medium text-capitalize"
        to="/proyectos"
        router
      >
        Proyectos
      </v-btn>

      <v-btn
        variant="text"
        class="nav-item mx-2 font-weight-medium text-capitalize"
      >
        Estatisticas
      </v-btn>
    </div>

    <v-spacer></v-spacer>

    <template v-slot:append>
      <v-btn
        icon
        class="ml-1"
      >
        <v-icon>mdi-magnify</v-icon>
      </v-btn>
      <v-btn
        icon
        class="ml-1 mr-3"
      >
        <v-icon>mdi-account-circle</v-icon>
      </v-btn>
    </template>
  </v-app-bar>
</template>

<style scoped>
.nav-item.v-btn {
  min-width: auto;
  padding-left: 12px;
  padding-right: 12px;
}

.v-img {
  align-self: center;
  cursor: pointer;
}

.nav-item {
  cursor: pointer;
}

.noselect {
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
</style>
