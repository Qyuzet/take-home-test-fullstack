import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8080/api";
const API_URL = `${API_BASE_URL}/tasks`;

export default {
  // Get all tasks
  getAllTasks() {
    return axios.get(API_URL);
  },

  // Get task by ID
  getTaskById(id) {
    return axios.get(`${API_URL}/${id}`);
  },

  // Create new task
  createTask(task) {
    return axios.post(API_URL, task);
  },

  // Update task
  updateTask(id, task) {
    return axios.put(`${API_URL}/${id}`, task);
  },

  // Delete task
  deleteTask(id) {
    return axios.delete(`${API_URL}/${id}`);
  },
};
