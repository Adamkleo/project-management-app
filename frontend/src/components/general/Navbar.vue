<script setup>
// No script logic needed for this layout fix
</script>

<template>
    <v-app-bar flat class="border-b" color="transparent" density="compact">
        <template v-slot:prepend>
            <router-link to="/">
                <v-img src="@/assets/fs_logo_full.png" alt="Logo" height="42" width="128" class="noselect ml-4 mr-2" contain />
            </router-link>
        </template>


        <v-spacer></v-spacer>

        <div class="d-flex align-center">
            <v-btn variant="text" class="nav-item mx-2 font-weight-medium text-capitalize" to="/" router>
                Home
            </v-btn>

            <v-btn variant="text" class="nav-item mx-2 font-weight-medium text-capitalize" to="/empleados" router>
                Empleados
            </v-btn>

            <v-btn variant="text" class="nav-item mx-2 font-weight-medium text-capitalize" to="/proyectos" router>
                Proyectos
            </v-btn>

            <v-btn variant="text" class="nav-item mx-2 font-weight-medium text-capitalize">
                Services
            </v-btn>
        </div>

        <v-spacer></v-spacer>

        <template v-slot:append>
            <v-btn icon class="ml-1">
                <v-icon>mdi-magnify</v-icon>
            </v-btn>
            <v-btn icon class="ml-1 mr-3">
                <v-icon>mdi-account-circle</v-icon>
            </v-btn>
        </template>
    </v-app-bar>
</template>



<style scoped>
/* Optional: Adjust v-btn min-width if default is too wide */
.nav-item.v-btn {
    min-width: auto;
    padding-left: 12px;
    /* Fine-tune padding */
    padding-right: 12px;
}

/* Ensure image is vertically centered if needed */
.v-img {
    align-self: center;
    cursor: pointer;

}

.nav-item {
    cursor: pointer;
}

.noselect {
  -webkit-touch-callout: none; /* iOS Safari */
    -webkit-user-select: none; /* Safari */
     -khtml-user-select: none; /* Konqueror HTML */
       -moz-user-select: none; /* Old versions of Firefox */
        -ms-user-select: none; /* Internet Explorer/Edge */
            user-select: none; /* Non-prefixed version, currently
                                  supported by Chrome, Edge, Opera and Firefox */
}
</style>