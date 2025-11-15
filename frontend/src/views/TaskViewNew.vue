<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import taskService from "@/services/taskService";
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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

const router = useRouter();
const tasks = ref([]);
const newTask = ref({
  title: "",
  description: "",
  status: "pending",
  type: "Feature",
  priority: "Medium",
});
const editingTask = ref(null);
const showCreateForm = ref(false);
const filterText = ref("");
const currentPage = ref(1);
const rowsPerPage = ref(10);

// Validation errors
const createErrors = ref({
  title: "",
  description: "",
});
const editErrors = ref({
  title: "",
  description: "",
});

// Validation functions
const validateCreateForm = () => {
  let isValid = true;
  createErrors.value = { title: "", description: "" };

  if (!newTask.value.title || newTask.value.title.trim() === "") {
    createErrors.value.title = "Title is required";
    isValid = false;
  } else if (newTask.value.title.length < 3) {
    createErrors.value.title = "Title must be at least 3 characters";
    isValid = false;
  } else if (newTask.value.title.length > 100) {
    createErrors.value.title = "Title must not exceed 100 characters";
    isValid = false;
  }

  if (!newTask.value.description || newTask.value.description.trim() === "") {
    createErrors.value.description = "Description is required";
    isValid = false;
  } else if (newTask.value.description.length < 10) {
    createErrors.value.description =
      "Description must be at least 10 characters";
    isValid = false;
  } else if (newTask.value.description.length > 500) {
    createErrors.value.description =
      "Description must not exceed 500 characters";
    isValid = false;
  }

  return isValid;
};

const validateEditForm = () => {
  let isValid = true;
  editErrors.value = { title: "", description: "" };

  if (!editingTask.value.title || editingTask.value.title.trim() === "") {
    editErrors.value.title = "Title is required";
    isValid = false;
  } else if (editingTask.value.title.length < 3) {
    editErrors.value.title = "Title must be at least 3 characters";
    isValid = false;
  } else if (editingTask.value.title.length > 100) {
    editErrors.value.title = "Title must not exceed 100 characters";
    isValid = false;
  }

  if (
    !editingTask.value.description ||
    editingTask.value.description.trim() === ""
  ) {
    editErrors.value.description = "Description is required";
    isValid = false;
  } else if (editingTask.value.description.length < 10) {
    editErrors.value.description = "Description must be at least 10 characters";
    isValid = false;
  } else if (editingTask.value.description.length > 500) {
    editErrors.value.description = "Description must not exceed 500 characters";
    isValid = false;
  }

  return isValid;
};

const fetchTasks = async () => {
  try {
    const response = await taskService.getAllTasks();
    tasks.value = response.data;
  } catch (error) {
    console.error("Error fetching tasks:", error);
  }
};

const createTask = async () => {
  if (!validateCreateForm()) {
    return;
  }

  try {
    await taskService.createTask(newTask.value);
    newTask.value = {
      title: "",
      description: "",
      status: "pending",
      type: "Feature",
      priority: "Medium",
    };
    createErrors.value = { title: "", description: "" };
    showCreateForm.value = false;
    fetchTasks();
  } catch (error) {
    console.error("Error creating task:", error);
  }
};

const startEdit = (task) => {
  editingTask.value = { ...task };
};

const cancelEdit = () => {
  editingTask.value = null;
};

const updateTask = async () => {
  if (!validateEditForm()) {
    return;
  }

  try {
    await taskService.updateTask(editingTask.value.id, editingTask.value);
    editingTask.value = null;
    editErrors.value = { title: "", description: "" };
    fetchTasks();
  } catch (error) {
    console.error("Error updating task:", error);
  }
};

const deleteTask = async (id) => {
  if (confirm("Are you sure you want to delete this task?")) {
    try {
      await taskService.deleteTask(id);
      fetchTasks();
    } catch (error) {
      console.error("Error deleting task:", error);
    }
  }
};

const getStatusVariant = (status) => {
  const variants = {
    pending: "secondary",
    "in-progress": "default",
    completed: "outline",
  };
  return variants[status] || "secondary";
};

const getStatusLabel = (status) => {
  const labels = {
    pending: "Todo",
    "in-progress": "In Progress",
    completed: "Done",
  };
  return labels[status] || status;
};

const getPriorityVariant = (priority) => {
  const variants = {
    High: "destructive",
    Medium: "default",
    Low: "secondary",
  };
  return variants[priority] || "secondary";
};

const getTypeVariant = (type) => {
  const variants = {
    Bug: "destructive",
    Feature: "default",
    Documentation: "secondary",
  };
  return variants[type] || "secondary";
};

// Filtered and paginated tasks
const filteredTasks = computed(() => {
  if (!filterText.value) return tasks.value;
  const search = filterText.value.toLowerCase();
  return tasks.value.filter(
    (task) =>
      task.title?.toLowerCase().includes(search) ||
      task.description?.toLowerCase().includes(search) ||
      task.status?.toLowerCase().includes(search) ||
      task.type?.toLowerCase().includes(search) ||
      task.priority?.toLowerCase().includes(search) ||
      task.id?.toString().includes(search)
  );
});

const paginatedTasks = computed(() => {
  const start = (currentPage.value - 1) * rowsPerPage.value;
  const end = start + rowsPerPage.value;
  return filteredTasks.value.slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(filteredTasks.value.length / rowsPerPage.value);
});

const handleLogout = () => {
  localStorage.removeItem("token");
  router.push("/login");
};

onMounted(() => {
  fetchTasks();
});
</script>

<template>
  <div class="min-h-screen bg-zinc-950 relative">
    <!-- Dot pattern -->
    <div
      class="absolute inset-0 bg-[radial-gradient(circle,#ffffff0a_1px,transparent_1px)] bg-[size:2rem_2rem]"
    ></div>

    <!-- Gradient glow -->
    <div
      class="absolute top-0 left-1/2 -translate-x-1/2 w-[800px] h-[400px] bg-blue-600/10 rounded-full blur-3xl"
    ></div>

    <div class="container mx-auto py-8 px-6 max-w-7xl relative z-10">
      <!-- Header -->
      <div class="mb-8 flex justify-between items-center">
        <h1 class="text-3xl font-bold text-white">Tasks</h1>
        <Button
          @click="handleLogout"
          class="bg-zinc-800 text-white border-zinc-700 hover:bg-zinc-700"
        >
          Logout
        </Button>
      </div>

      <!-- Add Task Button -->
      <div class="mb-6">
        <Button
          @click="showCreateForm = !showCreateForm"
          class="bg-white text-zinc-950 hover:bg-zinc-100"
        >
          {{ showCreateForm ? "Cancel" : "+ Add Task" }}
        </Button>
      </div>

      <!-- Create Task Form (Collapsible) -->
      <div
        v-if="showCreateForm"
        class="mb-6 backdrop-blur-2xl bg-zinc-900/30 rounded-2xl border border-zinc-700/50 p-6 shadow-2xl"
      >
        <h2 class="text-xl font-bold text-white mb-4">Create New Task</h2>
        <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
          <div>
            <Label for="title" class="text-zinc-300 text-sm">Title *</Label>
            <Input
              id="title"
              v-model="newTask.title"
              placeholder="Task title"
              class="bg-zinc-800/50 border-zinc-700 text-white placeholder:text-zinc-500 focus:border-blue-500 focus:ring-blue-500/20"
              :class="{ 'border-red-500': createErrors.title }"
            />
            <p v-if="createErrors.title" class="text-sm text-red-400 mt-1">
              {{ createErrors.title }}
            </p>
          </div>
          <div>
            <Label for="description" class="text-zinc-300 text-sm"
              >Description *</Label
            >
            <Input
              id="description"
              v-model="newTask.description"
              placeholder="Task description"
              class="bg-zinc-800/50 border-zinc-700 text-white placeholder:text-zinc-500 focus:border-blue-500 focus:ring-blue-500/20"
              :class="{ 'border-red-500': createErrors.description }"
            />
            <p
              v-if="createErrors.description"
              class="text-sm text-red-400 mt-1"
            >
              {{ createErrors.description }}
            </p>
          </div>
          <div>
            <Label for="type" class="text-zinc-300 text-sm">Type</Label>
            <Select v-model="newTask.type">
              <SelectTrigger class="bg-zinc-800/50 border-zinc-700 text-white">
                <SelectValue placeholder="Select type" />
              </SelectTrigger>
              <SelectContent class="bg-zinc-800 border-zinc-700">
                <SelectItem value="Bug">Bug</SelectItem>
                <SelectItem value="Feature">Feature</SelectItem>
                <SelectItem value="Documentation">Documentation</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div>
            <Label for="status" class="text-zinc-300 text-sm">Status</Label>
            <Select v-model="newTask.status">
              <SelectTrigger class="bg-zinc-800/50 border-zinc-700 text-white">
                <SelectValue placeholder="Select status" />
              </SelectTrigger>
              <SelectContent class="bg-zinc-800 border-zinc-700">
                <SelectItem value="pending">Todo</SelectItem>
                <SelectItem value="in-progress">In Progress</SelectItem>
                <SelectItem value="completed">Done</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div>
            <Label for="priority" class="text-zinc-300 text-sm">Priority</Label>
            <Select v-model="newTask.priority">
              <SelectTrigger class="bg-zinc-800/50 border-zinc-700 text-white">
                <SelectValue placeholder="Select priority" />
              </SelectTrigger>
              <SelectContent class="bg-zinc-800 border-zinc-700">
                <SelectItem value="High">High</SelectItem>
                <SelectItem value="Medium">Medium</SelectItem>
                <SelectItem value="Low">Low</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </div>
        <Button
          @click="createTask"
          class="mt-4 bg-white text-zinc-950 hover:bg-zinc-100"
          >Add Task</Button
        >
      </div>

      <!-- Task Table -->
      <div class="space-y-4">
        <!-- Filter and Controls -->
        <div class="flex items-center justify-between gap-4">
          <Input
            v-model="filterText"
            placeholder="Filter tasks..."
            class="max-w-sm bg-zinc-800/50 border-zinc-700 text-white placeholder:text-zinc-500 focus:border-blue-500 focus:ring-blue-500/20"
          />
          <div class="flex items-center gap-4 text-sm text-zinc-400">
            <span
              >{{ filteredTasks.length }} of {{ tasks.length }} row(s)
              selected.</span
            >
          </div>
        </div>

        <!-- Table -->
        <div
          class="backdrop-blur-2xl bg-zinc-900/30 rounded-2xl border border-zinc-700/50 overflow-hidden shadow-2xl"
        >
          <Table>
            <TableHeader>
              <TableRow class="hover:bg-transparent border-zinc-800">
                <TableHead class="w-[100px] text-zinc-300">Task</TableHead>
                <TableHead class="w-[150px] text-zinc-300">Title</TableHead>
                <TableHead class="text-zinc-300">Description</TableHead>
                <TableHead class="w-[120px] text-zinc-300">Type</TableHead>
                <TableHead class="w-[120px] text-zinc-300">Status</TableHead>
                <TableHead class="w-[120px] text-zinc-300">Priority</TableHead>
                <TableHead class="text-right w-20 text-zinc-300"
                  >Actions</TableHead
                >
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow
                v-if="paginatedTasks.length === 0"
                class="hover:bg-transparent border-zinc-800"
              >
                <TableCell colspan="7" class="text-center py-12 text-zinc-500">
                  No tasks found.
                </TableCell>
              </TableRow>
              <TableRow
                v-for="task in paginatedTasks"
                :key="task.id"
                class="border-zinc-800 hover:bg-zinc-800/30"
              >
                <!-- Edit Mode -->
                <template v-if="editingTask?.id === task.id">
                  <TableCell colspan="7" class="text-white">
                    <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
                      <div>
                        <Label class="text-zinc-300 text-sm">Title *</Label>
                        <Input
                          v-model="editingTask.title"
                          class="bg-zinc-800/50 border-zinc-700 text-white placeholder:text-zinc-500 focus:border-blue-500 focus:ring-blue-500/20"
                          :class="{ 'border-red-500': editErrors.title }"
                        />
                        <p
                          v-if="editErrors.title"
                          class="text-sm text-red-400 mt-1"
                        >
                          {{ editErrors.title }}
                        </p>
                      </div>
                      <div>
                        <Label class="text-zinc-300 text-sm"
                          >Description *</Label
                        >
                        <Input
                          v-model="editingTask.description"
                          class="bg-zinc-800/50 border-zinc-700 text-white placeholder:text-zinc-500 focus:border-blue-500 focus:ring-blue-500/20"
                          :class="{ 'border-red-500': editErrors.description }"
                        />
                        <p
                          v-if="editErrors.description"
                          class="text-sm text-red-400 mt-1"
                        >
                          {{ editErrors.description }}
                        </p>
                      </div>
                      <div>
                        <Label class="text-zinc-300 text-sm">Type</Label>
                        <Select v-model="editingTask.type">
                          <SelectTrigger
                            class="bg-zinc-800/50 border-zinc-700 text-white"
                          >
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent class="bg-zinc-800 border-zinc-700">
                            <SelectItem value="Bug">Bug</SelectItem>
                            <SelectItem value="Feature">Feature</SelectItem>
                            <SelectItem value="Documentation"
                              >Documentation</SelectItem
                            >
                          </SelectContent>
                        </Select>
                      </div>
                      <div>
                        <Label class="text-zinc-300 text-sm">Status</Label>
                        <Select v-model="editingTask.status">
                          <SelectTrigger
                            class="bg-zinc-800/50 border-zinc-700 text-white"
                          >
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent class="bg-zinc-800 border-zinc-700">
                            <SelectItem value="pending">Todo</SelectItem>
                            <SelectItem value="in-progress"
                              >In Progress</SelectItem
                            >
                            <SelectItem value="completed">Done</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div>
                        <Label class="text-zinc-300 text-sm">Priority</Label>
                        <Select v-model="editingTask.priority">
                          <SelectTrigger
                            class="bg-zinc-800/50 border-zinc-700 text-white"
                          >
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent class="bg-zinc-800 border-zinc-700">
                            <SelectItem value="High">High</SelectItem>
                            <SelectItem value="Medium">Medium</SelectItem>
                            <SelectItem value="Low">Low</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                    <div class="flex gap-2 mt-4">
                      <Button
                        @click="updateTask"
                        size="sm"
                        class="bg-white text-zinc-950 hover:bg-zinc-100"
                        >Save</Button
                      >
                      <Button
                        @click="cancelEdit"
                        size="sm"
                        class="bg-zinc-800 text-white border-zinc-700 hover:bg-zinc-700"
                        >Cancel</Button
                      >
                    </div>
                  </TableCell>
                </template>

                <!-- Display Mode -->
                <template v-else>
                  <TableCell class="font-mono text-sm text-zinc-400"
                    >TASK-{{ task.id }}</TableCell
                  >
                  <TableCell class="font-medium text-white">{{
                    task.title
                  }}</TableCell>
                  <TableCell class="text-sm text-zinc-400">{{
                    task.description
                  }}</TableCell>
                  <TableCell>
                    <Badge
                      :variant="getTypeVariant(task.type)"
                      class="font-normal text-white"
                    >
                      {{ task.type }}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Badge
                      :variant="getStatusVariant(task.status)"
                      class="font-normal text-white"
                    >
                      {{ getStatusLabel(task.status) }}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Badge
                      :variant="getPriorityVariant(task.priority)"
                      class="font-normal text-white"
                    >
                      {{ task.priority }}
                    </Badge>
                  </TableCell>
                  <TableCell class="text-right">
                    <DropdownMenu>
                      <DropdownMenuTrigger as-child>
                        <Button
                          size="sm"
                          class="h-8 w-8 p-0 bg-transparent hover:bg-zinc-800 text-white"
                        >
                          <span class="sr-only">Actions</span>
                          <span class="text-lg">⋯</span>
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent
                        align="end"
                        class="bg-zinc-800 border-zinc-700"
                      >
                        <DropdownMenuItem
                          @click="startEdit(task)"
                          class="text-white hover:bg-zinc-700 focus:bg-zinc-700"
                        >
                          Edit
                        </DropdownMenuItem>
                        <DropdownMenuSeparator class="bg-zinc-700" />
                        <DropdownMenuItem
                          @click="deleteTask(task.id)"
                          class="text-red-400 hover:bg-zinc-700 focus:bg-zinc-700 focus:text-red-400"
                        >
                          Delete
                        </DropdownMenuItem>
                      </DropdownMenuContent>
                    </DropdownMenu>
                  </TableCell>
                </template>
              </TableRow>
            </TableBody>
          </Table>
        </div>

        <!-- Pagination -->
        <div class="flex items-center justify-between px-2 py-4">
          <div class="text-sm text-zinc-400">
            Page {{ currentPage }} of {{ totalPages }}
          </div>
          <div class="flex items-center gap-2">
            <Button
              size="sm"
              @click="currentPage = 1"
              :disabled="currentPage === 1"
              class="bg-zinc-800 text-white border-zinc-700 hover:bg-zinc-700 disabled:opacity-50"
            >
              ≪
            </Button>
            <Button
              size="sm"
              @click="currentPage--"
              :disabled="currentPage === 1"
              class="bg-zinc-800 text-white border-zinc-700 hover:bg-zinc-700 disabled:opacity-50"
            >
              ‹
            </Button>
            <Button
              size="sm"
              @click="currentPage++"
              :disabled="currentPage === totalPages"
              class="bg-zinc-800 text-white border-zinc-700 hover:bg-zinc-700 disabled:opacity-50"
            >
              ›
            </Button>
            <Button
              size="sm"
              @click="currentPage = totalPages"
              :disabled="currentPage === totalPages"
              class="bg-zinc-800 text-white border-zinc-700 hover:bg-zinc-700 disabled:opacity-50"
            >
              ≫
            </Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
