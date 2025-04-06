<template>
  <Navbar />
  <v-main>
    <router-view />
  </v-main>

</template>

<script setup>
  import Navbar from '@/components/general/Navbar.vue'; // Adjust path if needed
</script>
