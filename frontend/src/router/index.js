import { createRouter, createWebHistory } from "vue-router";
import LoginView from "../views/LoginView.vue";
import TaskView from "../views/TaskViewNew.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/login",
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
      meta: { requiresGuest: true },
    },
    {
      path: "/tasks",
      name: "tasks",
      component: TaskView,
      meta: { requiresAuth: true },
    },
  ],
});

// Navigation guard
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");

  if (to.meta.requiresAuth && !token) {
    // Redirect to login if route requires auth and no token
    next("/login");
  } else if (to.meta.requiresGuest && token) {
    // Redirect to tasks if already logged in
    next("/tasks");
  } else {
    next();
  }
});

export default router;
