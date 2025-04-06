/**
 * plugins/vuetify.js
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Composables
import { createVuetify } from 'vuetify'


const myCustomTheme = {
  dark: false,
  colors: {
    primary: '#026FC1',
    secondary: '#008ED6',
    accent: '#028CF5',
    background: '#F3F7FB',
    surface: '#F3F7FB',
    error: '#FF5252',
    success: '#4CAF50',
    warning: '#FB8C00',
    darkText: '#001021',
  },
}


// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  theme: {
    defaultTheme: 'myCustomTheme',
    themes: {
      myCustomTheme,
    },
  },
  defaults: {
    global: {
      style: {
        fontFamily: 'Montserrat, sans-serif',
      },
    },
  },
})
