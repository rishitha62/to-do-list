// ============================================================================================================================================
// DATA MODEL AND STATE MANAGEMENT
// Design Spec 3.4: Client State/Data Store
// User Story 7: No Backend Requirement - localStorage for persistence
// ============================================================================================================================================

/**
 * Task Data Model (Design Spec 3.4):
 * {
 *   id: "unique-string",
 *   text: "Task description",
 *   completed: false,
 *   createdAt: timestamp
 * }
 */

// Initialize tasks array from localStorage or empty array
let tasks = [];

// Load tasks from localStorage on page load
function loadTasks() {
    const storedTasks = localStorage.getItem('todoTasks');
    if (storedTasks) {
        try {
            tasks = JSON.parse(storedTasks);
        } catch (error) {
            console.error('Error loading tasks from localStorage:', error);
            tasks = [];
        }
    }
}

// Save tasks to localStorage
// Design Spec 3.4: Persistence via localStorage
function saveTasks() {
    try {
        localStorage.setItem('todoTasks', JSON.stringify(tasks));
    } catch (error) {
        console.error('Error saving tasks to localStorage:', error);
    }
}

// ============================================================================================================================================
// TASK OPERATIONS
// User Story 1: Add Tasks
// User Story 3: Mark Tasks as Completed
// User Story 4: Delete Tasks
// ============================================================================================================================================

/**
 * Add a new task
 * User Story 1: Add Tasks
 * Design Spec 4.1: Add Task Sequence
 * @param {string} taskText - The text of the task to add
 */
function addTask(taskText) {
    // Validate input
    if (!taskText || taskText.trim() === '') {
        return;
    }

    // Create new task object (Design Spec 3.4: Data Model)
    const newTask = {
        id: generateUniqueId(),
        text: taskText.trim(),
        completed: false,
        createdAt: Date.now()
    };

    // Add to tasks array
    tasks.push(newTask);

    // Persist to localStorage (Design Spec 3.4)
    saveTasks();

    // User Story 8: Immediate Effect - Update UI without page reload
    renderTasks();
    updateStats();
}

/**
 * Toggle task completion status
 * User Story 3: Mark Tasks as Completed
 * Design Spec 4.2: Mark Task as Completed Sequence
 * @param {string} taskId - The ID of the task to toggle
 */
function toggleTaskComplete(taskId) {
    const task = tasks.find(t => t.id === taskId);
    if (task) {
        task.completed = !task.completed;
        
        // Persist changes (Design Spec 3.4)
        saveTasks();
        
        // User Story 8: Immediate Effect - Update UI instantly
        renderTasks();
        updateStats();
    }
}

/**
 * Delete a task
 * User Story 4: Delete Tasks
 * Design Spec 4.3: Delete Task Sequence
 * @param {string} taskId - The ID of the task to delete
 */
function deleteTask(taskId) {
    // Remove task from array
    tasks = tasks.filter(t => t.id !== taskId);
    
    // Persist changes (Design Spec 3.4)
    saveTasks();
    
    // User Story 8: Immediate Effect - Update UI instantly
    renderTasks();
    updateStats();
}

/**
 * Clear all completed tasks
 * User Story 4: Delete Tasks - Batch delete completed
 */
function clearCompletedTasks() {
    tasks = tasks.filter(t => !t.completed);
    
    // Persist changes
    saveTasks();
    
    // Update UI
    renderTasks();
    updateStats();
}

// ============================================================================================================================================
// UI RENDERING
// User Story 2: View Added Tasks
// Design Spec 3.2 & 3.3: Task List and Task Item Components
// ============================================================================================================================================

/**
 * Render all tasks to the DOM
 * User Story 2: View Added Tasks
 * Design Spec 4.4: View Tasks Sequence
 */
function renderTasks() {
    const taskList = document.getElementById('taskList');
    const emptyState = document.getElementById('emptyState');
    
    // Clear current list
    taskList.innerHTML = '';
    
    // Show/hide empty state based on task count
    if (tasks.length === 0) {
        emptyState.classList.remove('hidden');
        return;
    } else {
        emptyState.classList.add('hidden');
    }
    
    // Render each task (Design Spec 3.3: Task Item Component)
    tasks.forEach(task => {
        const taskItem = createTaskElement(task);
        taskList.appendChild(taskItem);
    });
}

/**
 * Create a task list item element
 * Design Spec 3.3: Task Item Component
 * User Story 3: Mark Tasks as Completed - Checkbox
 * User Story 4: Delete Tasks - Delete button
 * @param {Object} task - The task object
 * @returns {HTMLElement} - The task list item element
 */
function createTaskElement(task) {
    // Create list item
    const li = document.createElement('li');
    li.className = 'task-item';
    li.setAttribute('role', 'listitem');
    
    // User Story 3: Mark Tasks as Completed - Visual distinction
    if (task.completed) {
        li.classList.add('completed');
    }
    
    // Create checkbox for completion toggle
    // User Story 3: Mark Tasks as Completed
    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.className = 'task-checkbox';
    checkbox.checked = task.completed;
    checkbox.setAttribute('aria-label', `Mark task "${task.text}" as ${task.completed ? 'incomplete' : 'complete'}`);
    
    // Design Spec 3.5: Event listener for completion toggle
    checkbox.addEventListener('change', () => {
        toggleTaskComplete(task.id);
    });
    
    // Create task text span
    // User Story 2: View Added Tasks
    const taskText = document.createElement('span');
    taskText.className = 'task-text';
    taskText.textContent = task.text;
    
    // Create delete button
    // User Story 4: Delete Tasks
    const deleteBtn = document.createElement('button');
    deleteBtn.className = 'delete-btn';
    deleteBtn.textContent = 'Delete';
    deleteBtn.setAttribute('aria-label', `Delete task "${task.text}"`);
    
    // Design Spec 3.5: Event listener for delete
    deleteBtn.addEventListener('click', () => {
        deleteTask(task.id);
    });
    
    // Assemble task item
    li.appendChild(checkbox);
    li.appendChild(taskText);
    li.appendChild(deleteBtn);
    
    return li;
}

/**
 * Update task statistics
 * User Story 2: View Added Tasks - Display task counts
 */
function updateStats() {
    const totalTasks = tasks.length;
    const completedTasks = tasks.filter(t => t.completed).length;
    const activeTasks = totalTasks - completedTasks;
    
    document.getElementById('totalTasks').textContent = totalTasks;
    document.getElementById('activeTasks').textContent = activeTasks;
    document.getElementById('completedTasks').textContent = completedTasks;
    
    // Enable/disable clear completed button
    const clearBtn = document.getElementById('clearCompletedBtn');
    clearBtn.disabled = completedTasks === 0;
}

// ============================================================================================================================================
// EVENT HANDLERS
// Design Spec 3.5: Interaction Controller
// User Story 8: Immediate Effect - Event-driven updates
// ============================================================================================================================================

/**
 * Handle add task button click
 * User Story 1: Add Tasks
 */
function handleAddTask() {
    const taskInput = document.getElementById('taskInput');
    const taskText = taskInput.value;
    
    // Add task
    addTask(taskText);
    
    // Clear input field
    taskInput.value = '';
    taskInput.focus();
}

/**
 * Handle Enter key press in task input
 * User Story 1: Add Tasks - Keyboard support
 */
function handleKeyPress(event) {
    if (event.key === 'Enter') {
        handleAddTask();
    }
}

// ============================================================================================================================================
// UTILITY FUNCTIONS
// ============================================================================================================================================

/**
 * Generate a unique ID for tasks
 * @returns {string} - Unique identifier
 */
function generateUniqueId() {
    return `task_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
}

// ============================================================================================================================================
// INITIALIZATION
// Design Spec 3.5: Set up event listeners
// User Story 6: No Login or Registration - Direct access on load
// ============================================================================================================================================

/**
 * Initialize the application
 * Called when DOM is fully loaded
 */
function initApp() {
    // Load tasks from localStorage (Design Spec 3.4)
    loadTasks();
    
    // Render initial task list (User Story 2: View Added Tasks)
    renderTasks();
    updateStats();
    
    // Set up event listeners (Design Spec 3.5: Interaction Controller)
    
    // User Story 1: Add Tasks - Add button click
    const addTaskBtn = document.getElementById('addTaskBtn');
    addTaskBtn.addEventListener('click', handleAddTask);
    
    // User Story 1: Add Tasks - Enter key press
    const taskInput = document.getElementById('taskInput');
    taskInput.addEventListener('keypress', handleKeyPress);
    
    // User Story 4: Delete Tasks - Clear completed button
    const clearCompletedBtn = document.getElementById('clearCompletedBtn');
    clearCompletedBtn.addEventListener('click', clearCompletedTasks);
    
    // Focus on input field for immediate use
    taskInput.focus();
}

// Run initialization when DOM is ready
// User Story 6: No Login or Registration - Immediate access
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initApp);
} else {
    initApp();
}

// ============================================================================================================================================
// REPOSITORY INFORMATION
// Git Repository: https://github.com/rishitha62/to-do-list
// Username: rishitha62
// Repository: to-do-list
// 
// All code successfully pushed to repository using GitHub API
// Files pushed: index.html, styles.css, script.js, README.md
// ============================================================================================================================================
