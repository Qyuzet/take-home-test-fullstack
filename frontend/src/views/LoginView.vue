<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

const router = useRouter();
const username = ref("");
const password = ref("");
const errors = ref({
  username: "",
  password: "",
  general: "",
});
const isLoading = ref(false);

const validateForm = () => {
  let isValid = true;
  errors.value = { username: "", password: "", general: "" };

  if (!username.value || username.value.trim() === "") {
    errors.value.username = "Username is required";
    isValid = false;
  }

  if (!password.value || password.value.trim() === "") {
    errors.value.password = "Password is required";
    isValid = false;
  }

  return isValid;
};

const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8080/api";

const handleLogin = async () => {
  if (!validateForm()) {
    return;
  }

  isLoading.value = true;
  errors.value.general = "";

  try {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username.value,
        password: password.value,
      }),
    });

    if (!response.ok) {
      throw new Error("Invalid credentials");
    }

    const data = await response.json();
    localStorage.setItem("token", data.token);
    router.push("/tasks");
  } catch (error) {
    errors.value.general = "Invalid username or password";
  } finally {
    isLoading.value = false;
  }
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center p-4 bg-zinc-950">
    <!-- Dot pattern -->
    <div
      class="absolute inset-0 bg-[radial-gradient(circle,#ffffff0a_1px,transparent_1px)] bg-[size:2rem_2rem]"
    ></div>

    <!-- Gradient glow -->
    <div
      class="absolute top-0 left-1/2 -translate-x-1/2 w-[800px] h-[400px] bg-blue-600/10 rounded-full blur-3xl"
    ></div>

    <!-- Card -->
    <div class="w-full max-w-md relative z-10">
      <div
        class="backdrop-blur-2xl bg-zinc-900/30 rounded-2xl border border-zinc-700/50 p-8 shadow-2xl"
      >
        <div class="mb-8">
          <h1 class="text-2xl font-bold text-white mb-1">Welcome back</h1>
          <p class="text-zinc-400 text-sm">Sign in to your account</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-4">
          <div class="space-y-2">
            <Label for="username" class="text-zinc-300 text-sm">Username</Label>
            <Input
              id="username"
              v-model="username"
              placeholder="Enter username"
              class="bg-zinc-800/50 border-zinc-700 text-white placeholder:text-zinc-500 focus:border-blue-500 focus:ring-blue-500/20 h-11 rounded-lg"
              :disabled="isLoading"
            />
            <p v-if="errors.username" class="text-sm text-red-400">
              {{ errors.username }}
            </p>
          </div>

          <div class="space-y-2">
            <Label for="password" class="text-zinc-300 text-sm">Password</Label>
            <Input
              id="password"
              type="password"
              v-model="password"
              placeholder="Enter password"
              class="bg-zinc-800/50 border-zinc-700 text-white placeholder:text-zinc-500 focus:border-blue-500 focus:ring-blue-500/20 h-11 rounded-lg"
              :disabled="isLoading"
            />
            <p v-if="errors.password" class="text-sm text-red-400">
              {{ errors.password }}
            </p>
          </div>

          <p v-if="errors.general" class="text-sm text-red-400 text-center">
            {{ errors.general }}
          </p>

          <Button
            type="submit"
            class="w-full h-11 bg-white text-zinc-950 hover:bg-zinc-100 font-medium rounded-lg mt-6"
            :disabled="isLoading"
          >
            {{ isLoading ? "Signing in..." : "Sign In" }}
          </Button>

          <p class="text-xs text-center text-zinc-500 mt-4">
            Demo: <span class="text-zinc-400 font-mono">admin / admin123</span>
          </p>
        </form>
      </div>
    </div>
  </div>
</template>
