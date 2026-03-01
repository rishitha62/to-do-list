package com.todolist.pages;

/**
 * ==================================================================================================
 * Page Object Model for To-Do List Application
 *
 * This class represents the main page of the To-Do List application
 * and provides methods to interact with the UI elements.
 *
 * Based on Page Object Model (POM) design pattern for maintainable test automation.
 *
 * Repository: https://github.com/rishitha62/to-do-list
 * ==================================================================================================
 */

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ToDo List Page Object
 * Contains all locators and actions for the To-Do List page
 */
public class ToDoListPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ==================================================================================================
    // Page Elements - Mapped from index.html
    // ==================================================================================================

    // Task Input Elements (User Story 1: Add Tasks)
    @FindBy(id = "taskInput")
    private WebElement taskInput;

    @FindBy(id = "addTaskBtn")
    private WebElement addTaskButton;

    // Task List Elements (User Story 2: View Tasks)
    @FindBy(id = "taskList")
    private WebElement taskListContainer;

    @FindBy(id = "emptyState")
    private WebElement emptyStateMessage;

    // Statistics Elements (User Story 2: View Tasks)
    @FindBy(id = "totalTasks")
    private WebElement totalTasksCount;

    @FindBy(id = "activeTasks")
    private WebElement activeTasksCount;

    @FindBy(id = "completedTasks")
    private WebElement completedTasksCount;

    // Clear Completed Button (User Story 4: Delete Tasks)
    @FindBy(id = "clearCompletedBtn")
    private WebElement clearCompletedButton;

    // Page Title (User Story 5: User-Friendly Interface)
    @FindBy(tagName = "h1")
    private WebElement pageTitle;

    // Dynamic locators for task items
    private final By TASK_ITEMS = By.cssSelector(".task-item");
    private final By TASK_CHECKBOX = By.cssSelector(".task-checkbox");
    private final By TASK_TEXT = By.cssSelector(".task-text");
    private final By DELATE_BUTTON = By.cssSelector(".delete-btn");
    private final By COMPLETED_TASK = By.cssSelector(".task-item.completed");

    // ==================================================================================================
    // Constructor
    // ==================================================================================================

    public ToDoListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(this, driver);
    }

    // ==================================================================================================
    // Page Actions - User Story 1: Add Tasks
    // ==================================================================================================

    /**
     * Enter task text in the input field
     * @param taskText Text of the task to enter
     */
    public void enterTaskText(String taskText) {
        wait.until(ExpectedConditions.elementToBeClickable(taskInput));
        taskInput.clear();
        taskInput.sendKeys(taskText);
    }

    /**
     * Click the Add button to add a task
     */
    public void clickAddButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addTaskButton));
        addTaskButton.click();
        // Wait for UI to update (User Story 8: Immediate Effect)
        this.waitForPageUpdate();
    }

    /**
     * Press Enter key in task input field
     * (User Story 1: Add Tasks - Keyboard Support)
     */
    public void pressEnterKey() {
        taskInput.sendKeys(Keys.ENTER);
        this.waitForPageUpdate();
    }

    /**
     * Add a task using text and clicking Add button
     * @param taskText Text of the task to add
     */
    public void addTask(String taskText) {
        enterTaskText(taskText);
        clickAddButton();
    }

    /**
     * Check if task input field is empty
     * @return true if task input is empty
     */
    public boolean isTaskInputEmpty() {
        return taskInput.getAttribute("value").isEmpty();
    }

    // ==================================================================================================
    // Page Actions - User Story 2: View Tasks
    // ==================================================================================================

    /**
     * Get all task elements
     * @return List of all task WebElements
     */
    public List<WebElement> getAllTasks() {
        return driver.findElements(TASK_ITEMS);
    }

    /**
     * Get the count of tasks in the list
     * @return Number of tasks
     */
    public int getTaskCount() {
        return getAllTasks().size();
    }

    /**
     * Get the text of all tasks
     * @return List of task text strings
     */
    public List<String> getAllTaskTexts() {
        return getAllTasks().stream()
                .map(task -> task.findElement(TASK_TEXT).getText())
                .collect(Collectors.toList());
    }

    /**
     * Check if a task with specific text exists
     * @param taskText Text of the task to find
     * @return true if task exists
     */
    public boolean isTaskPresent(String taskText) {
        return getAllTaskTexts().contains(taskText);
    }

    /**
     * Check if empty state message is displayed
     * @return true if empty state is visible
     */
    public boolean isEmptyStateVisible() {
        try {
            return emptyStateMessage.isDisplayed() && !emptyStateMessage.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    // Statistics methods
    public int getTotalTasksCount() {
        wait.until(ExpectedConditions.visibilityOf(totalTasksCount));
        return Integer.parseInt(totalTasksCount.getText());
    }

    public int getActiveTasksCount() {
        wait.until(ExpectedConditions.visibilityOf(activeTasksCount));
        return Integer.parseInt(activeTasksCount.getText());
    }

    public int getCompletedTasksCount() {
        wait.until(ExpectedConditions.visibilityOf(completedTasksCount));
        return Integer.parseInt(completedTasksCount.getText());
    }

    // ==================================================================================================
    // Page Actions - User Story 3: Mark Tasks as Completed
    // ==================================================================================================

    /**
     * Find a task by its text
     * @param taskText Text of the task
     * @return WebElement of the task item
     */
    private WebElement findTaskByText(String taskText) {
        return getAllTasks().stream()
                .filter(task -> task.findElement(TASK_TEXT).getText().equals(taskText))
                .findFirst()
                .orelseThrow(() -> new RuntimeException("Task not found: " + taskText));
    }

    /**
     * Click the checkbox for a specific task
     * @param taskText Text of the task
     */
    public void clickTaskCheckbox(String taskText) {
        WebElement task = findTaskByText(taskText);
        WebElement checkbox = task.findElement(TASK_CHECKBOX);
        wait.until(ExpectedConditions.elementToBeClickable(checkbox));
        checkbox.click();
        this.waitForPageUpdate();
    }

    /**
     * Check if a task is marked as completed
     * @param taskText Text of the task
     * @return true if task is completed
     */
    public boolean isTaskCompleted(String taskText) {
        WebElement task = findTaskByText(taskText);
        return task.getAttribute("class").contains("completed");
    }

    /**
     * Check if task has strike-through style
     * (User Story 3: Completed tasks are visually distinct)
     * @param taskText Text of the task
     * @return true if task has strike-through style
     */
    public boolean hasStrikeThroughStyle(String taskText) {
        WebElement task = findTaskByText(taskText);
        WebElement taskTextElement = task.findElement(TASK_TEXT);
        String textDecoration = taskTextElement.getCssValue("text-decoration");
        return textDecoration.contains("line-through");
    }

    /**
     * Get the count of completed tasks in the list
     * @return Number of completed tasks
     */
    public int getCompletedTasksInList() {
        return driver.findElements(COMPLETED_TASK).size();
    }

    // ==================================================================================================
    // Page Actions - User Story 4: Delete Tasks
    // ==================================================================================================

    /**
     * Click the delete button for a specific task
     * @param taskText Text of the task
     */
    public void clickDeleteButton(String taskText) {
        WebElement task = findTaskByText(taskText);
        WebElement deleteButton = task.findElement(DELETE_BUTTON);
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        deleteButton.click();
        this.waitForPageUpdate();
    }

    /**
     * Click the Clear Completed button to delete all completed tasks
     * (User Story 4: Delete Tasks - Batch delete)
     */
    public void clickClearCompletedButton() {
        wait.until(ExpectedConditions.elementToBeClickable(clearCompletedButton));
        clearCompletedButton.click();
        this.waitForPageUpdate();
    }

    /**
     * Check if Clear Completed button is enabled
     * @return true if button is enabled
     */
    public boolean isClearCompletedButtonEnabled() {
        return clearCompletedButton.isEnabled();
    }

    // ==================================================================================================
    // Page Actions - User Story 5: User-Friendly Interface
    // ==================================================================================================

    /**
     * Check if page title is visible
     * @return true if title is visible
     */
    public boolean isPageTitleVisible() {
        try {
            return pageTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if task input field is visible
     * @return true if input field is visible
     */
    public boolean isTaskInputVisible() {
        try {
            return taskInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if Add button is visible
     * @return true if button is visible
     */
    public boolean isAddButtonVisible() {
        try {
            return addTaskButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if task list container is visible
     * @return true if container is visible
     */
    public boolean isTaskListContainerVisible() {
        try {
            return taskListContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if statistics section is visible
     * @return true if statistics section is visible
     */
    public boolean isStatisticsSectionVisible() {
        try {
            return totalTasksCount.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if Clear Completed button is visible
     * @return true if button is visible
     */
    public boolean isClearCompletedButtonVisible() {
        try {
            return clearCompletedButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify each task item has a checkbox
     * @return true if all tasks have checkboxes
     */
    public boolean doAslTasksHaveCheckboxes() {
        List<WebElement> tasks = getAllTasks();
        if (tasks.isEmpty()) return true;
        
        for (WebElement task : tasks) {
            try {
                task.findElement(TASK_CHECKBOX);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verify each task item has a delete button
     * @return true if all tasks have delete buttons
     */
    public boolean doAllTasksHaveDeleteButtons() {
        List<WebElement> tasks = getAllTasks();
        if (tasks.isEmpty()) return true;
        
        for (WebElement task : tasks) {
            try {
                task.findElement(DELETE_BUTTON);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    // ==================================================================================================
    // Utility Methods
    // ==================================================================================================

    /**
     * Wait for page to update after an action
     * (User Story 8: Immediate Effect - without page reload)
     */
    private void waitForPageUpdate() {
        try {
            Thread.sleep(500); // Wait for UI to update
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mark multiple tasks as completed
     * @param count Number of tasks to mark as completed
     */
    public void markMultipleTasksCompleted(int count) {
        List<WebElement> tasks = getAllTasks();
        for (int i = 0; i < count && i < tasks.size(); i++) {
            WebElement checkbox = tasks.get(i).findElement(TASK_CHECKBOX);
            if (!checkbox.isSelected()) {
                checkbox.click();
                this.waitForPageUpdate();
            }
        }
    }
}