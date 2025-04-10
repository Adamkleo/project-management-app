# 🧩 Project Management App — Frontend

This is the **frontend** for a Project Management system built using:

- **Vue 3 (Options + Composition API)**
- **Vuetify 3** — UI components
- **Pinia** — for state management
- **Axios** — for API requests
- **SCSS** — for styling

---

## 📁 Project Structure

```
src/
├── assets/                    # Static assets (images, logos, etc.)
├── components/               # All reusable UI components
│   ├── employee/             # Components related to employees
│   │   ├── EmployeeForm.vue
│   │   └── Employees.vue
│   ├── general/              # General-purpose shared components
│   │   ├── Button.vue
│   │   ├── CardGrid.vue
│   │   ├── DataTable.vue
│   │   ├── DynamicForm.vue
│   │   └── Navbar.vue
│   ├── project/              # Components related to projects
│   │   ├── ProjectAssignment.vue
│   │   ├── ProjectForm.vue
│   │   ├── Projects.vue
│   │   ├── ProjectsGrid.vue
│   │   └── ProjectsList.vue
│   └── Homepage.vue
├── layouts/                  # App shell layouts (default.vue)
├── pages/                    # Route views
│   ├── empleados/
│   │   ├── add.vue
│   │   ├── index.vue
│   ├── proyectos/
│   │   ├── [id]/
│   │   │   ├── index.vue
│   │   ├── grid.vue
│   │   ├── add.vue
│   │   ├── index.vue
│   └── index.vue
├── plugins/                  # Axios, Vuetify setup
├── router/                   # Vue Router config
├── stores/                   # Pinia stores
│   ├── assignmentStore.js
│   ├── employeeStore.js
│   └── projectStore.js
├── styles/                   # SCSS variables & global styles
│   └── settings.scss
├── utils/                    # Helper functions
├── App.vue                   # Root component
└── main.js                   # App entry point
```

---

## 📦 State Management (Pinia Stores)

| Store             | Purpose                                      |
|------------------|----------------------------------------------|
| `employeeStore`  | Manages employee list, add, terminate        |
| `assignmentStore`| Handles employee-project assignments         |
| `projectStore`   | CRUD operations for projects                 |

---

## 🧩 Key Components

### General
- **DataTable.vue** – Searchable, paginated table with action slots
- **Navbar.vue** – Navigation header
- **DynamicForm.vue** – Generic form builder
- **CardGrid.vue** – Grid card display (used in project view switch)

### Feature-Specific
- **Employees.vue** – List & manage employees
- **Projects.vue** – Switch between list/grid views
- **ProjectForm.vue** – Add/edit projects
- **EmployeeForm.vue** – Add/edit employees

---

## ⚙️ Setup Instructions

### 1. Install dependencies
```bash
npm install
```

### 2. Run the dev server
```bash
npm run dev
```

App runs at: [http://localhost:3000](http://localhost:3000)

### 3. API Base URL
Update it in `src/plugins/axios.js` if needed:
```js
baseURL: 'http://localhost:8080/'
```

---

