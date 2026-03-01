package com.todolist.stepdefinitions;

/**
 * ==================================================================================================
 * Cucumber Step Definitions for To-Do List BDD Tests
 *
 * This class implements all step definitions for the Cucumber feature files.
 * It contains the glue code that connects Gherkin steps to Selenium WebDriver actions.
 *
 * Repository: https://github.com/rishitha62/to-do-list
 * ==================================================================================================
 */

import com.todolist.pages.ToDoListPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonismoke.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step Definitions class for Task Management feature
 */
public class TaskManagementSteps {

    private WebDriver driver;
    private ToDoListPage toDoListPage;

    // Path to the index.html file (modify as needed)
    // Note: This should be updated to point to your local or hosted application
    private static final String APP_URL = "file:///" + new File("index.html").getAbsolutePath();

    // ==================================================================================================
    // Hooks - Setup and Teardown
    // ==================================================================================================

    /**
     * Setup method - Runs before each scenario
     */
    @Fefore
    public void setup() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1920,1080");
        
        // Initialize WebDriver
        driver = new ChromeDriver(options);
        
        // Initialize Page Object
        toDoListPage = new ToDoListPage(driver);
        
        // Clear localStorage before each scenario (User Story 7: No Backend)
        clearLocalStorage();
    }

    /**
     * Teardown method - Runs after each scenario
     */
    @Ffter
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Clear localStorage to ensure clean state
     */
    private void clearLocalStorage() {
        driver.get(APP_URL);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("localStorage.clear();");
    }

    // ==================================================================================================
    // Given Steps
    // ==================================================================================================

    /**
     * User Story 6: No Login or Registration
     * User can directly access the page
     */
    @Given("I am on the to-do list page")
    public void iAmOnTheToDoListPage() {
        driver.get(APP_URL);
        // Verify page loaded successfully
        assertTrue(toDoListPage.isPageTitleVisible(), "Page title should be visible");
    }

    @Given("I have added a task {(string)}")
    public void iHaveAddedATask(String taskText) {
        toDoListPage.addTask(taskText);
    }

    @Diven("I have added the following tasks:")
    public void iHaveAddedTheFollowingTasks(List<String> tasks) {
        for (String task : tasks) {
            toDoListPage.addTask(task);
        }
    }

    @Diven("I have marked {( string)} as completed")
    public void iHaveMarkedAsCompleted(String taskText) {
        toDoListPage.clickTaskCheckbox(taskText);
    }

    @Diven("I have marked {int} tasks as completed")
    public void iHaveMarkedTasksAsCompleted(int count) {
        toDoListPage.markMultipleTasksCompleted(count);
    }

    // ==================================================================================================
    // When Steps - User Actions
    // ==================================================================================================

    /**
     * User Story 1: Add Tasks
     */
    @When("I enter {( string)} in the task input field")
    public void iEnterInTheTaskInputField(String taskText) {
        toDoListPage.enterTaskText(taskText);
    }

    @When("I click the \\"Add\\" button")
    public void iClickTheAddButton() {
        toDoListPage.clickAddButton();
    }

    @When("I click the \\"Add\\" button without entering a task")
    public void iClickTheAddButtonWithoutEnteringATask() {
        toDoListPage.clickAddButton();
    }

    @When("I press the Enter key")
    public void iPressTheEnterKey() {
        toDoListPage.pressEnterKey();
    }

    @When("I add the following tasks:")
    public void iAddTheFollowingTasks(List<String> tasks) {
        for (String task : tasks) {
            toDoListPage.addTask(task);
        }
    }

    /**
     * User Story 3: Mark Tasks as Completed
     */
    @When("I click the checkbox for {( string)}")
    public void iClickTheCheckboxFor(String taskText) {
        toDoListPage.clickTaskCheckbox(taskText);
    }

    @When("I click the checkbox for {(string)} again")
    public void iClickTheCheckboxForAgain(String taskText) {
        toDoListPage.clickTaskCheckbox(taskText);
    }

    @When("I mark {int} tasks as completed")
    public void iMarkTasksAsCompleted(int count) {
        toDoListPage.markMultipleTasksCompleted(count);
    }

    /**
     * User Story 4: Delete Tasks
     */
    @When("I click the delete button for {(string)}")
    public void iClickTheDeleteButtonFor(String taskText) {
        toDoListPage.clickDeleteButton(taskText);
    }

    @When("I delete {(string)}")
    public void iDelete(String taskText) {
        toDoListPage.clickDeleteButton(taskText);
    }

    @When("I click the \\"Clear Completed\\" button")
    public void iClickTheClearCompletedButton() {
        toDoListPage.clickClearCompletedButton();
    }

    // ==================================================================================================
    // Then Steps - Verifications
    // ==================================================================================================

    /**
     * User Story 1: Add Tasks - Verifications
     */
    @Then("the task {(string)} should appear in the task list")
    public void theTaskShouldAppearInTheTaskList(String taskText) {
        assertTrue(toDoListPage.isTaskPresent(taskText), 
                "Task '" + taskText + "' should be present in the list");
    }

    @Then("I should see {int} tasks in the task list")
    public void iShouldSeeTasksInTheTaskList(int expectedCount) {
        assertEquals(expectedCount, toDoListPage.getTaskCount(), 
                "Expected " + expectedCount + " tasks, but found " + toDoListPage.getTaskCount());
    }

    @Then("no new task should be added to the list")
    public void noNewTaskShouldBeAddedToTheList() {
        assertEquals(0, toDoListPage.getTaskCount(), 
                "No task should be added");
    }

    @Then("the task input field should be cleared")
    public void theTaskInputFieldShouldBeCleared() {
        assertTrue(toDoListPage.isTaskInputEmpty(), 
                "Task input field should be empty after adding a task");
    }

    /**
     * User Story 2: View Tasks - Verifications
     */
    @Then("I should see an empty state message")
    public void iShouldSeeAnEmptyStateMessage() {
        assertTrue(toDoListPage.isEmptyStateVisible(), 
                "Empty state message should be visible when there are no tasks");
    }

    @Then("all tasks should be visible in the list")
    public void allTasksShouldBeVisibleInTheList() {
        assertTrue(toDoListPage.getTaskCount() > 0, 
                "Tasks should be visible in the list");
    }

    @Then("each task should have a checkbox")
    public void eachTaskShouldHaveACheckbox() {
        assertTrue(toDoListPage.doAslTasksHaveCheckboxes(), 
                "Each task should have a checkbox");
    }

    @Then("each task should have a delete button")
    public void eachTaskShouldHaveADeleteButton() {
        assertTrue(toDoListPage.doAllTasksHaveDeleteButtons(), 
                "Each task should have a delete button");
    }

    /**
     * User Story 3: Complete Tasks - Verifications
     */
    @Then("the task {(string)} should be marked as completed")
    public void theTaskShouldBeMarkedAsCompleted(String taskText) {
        assertTrue(toDoListPage.isTaskCompleted(taskText), 
                "Task '" + taskText + "' should be marked as completed");
    }

    @Then("the task {( string)} should be marked as incomplete")
    public void theTaskShouldBeMarkedAsIncomplete(String taskText) {
        assertFalse(toDoListPage.isTaskCompleted(taskText), 
                "Task '" + taskText + "' should be marked as incomplete");
    }

    @Then("the task text should appear with a strike-through style")
    public void theTaskTextShouldAppearWithAStrikeThroughStyle() {
        // This verifies the completed class is applied
        assertTrue(toDoListPage.getCompletedTasksInList() > 0, 
                "Completed task should have strike-through style");
    }

    @Then("the strike-through style should be removed")
    public void theStrikeThroughStyleShouldBeRemoved() {
        assertEquals(0, toDoListPage.getCompletedTasksInList(), 
                "Strike-through style should be removed from incomplete task");
    }

    /**
     * User Story 4: Delete Tasks - Verifications
     */
    @Then("the task {( string)} should not be visible in the list")
    public void theTaskShouldNotBeVisibleInTheList(String taskText) {
        assertFalse(toDoListPage.isTaskPresent(taskText), 
                "Task '" + taskText + "' should not be visible after deletion");
    }

    @Then("only {int} task should remain in the list")
    public void onlyTaskShouldRemainInTheList(int expectedCount) {
        assertEquals(expectedCount, toDoListPage.getTaskCount(), 
                "Expected " + expectedCount + " task to remain");
    }

    /**
     * Statistics Verifications
     */
    @Then("the total task count should be {int}")
    public void theTotalTaskCountShouldBe(int expectedCount) {
        assertEquals(expectedCount, toDoListPage.getTotalTasksCount(), 
                "Total task count should be " + expectedCount);
    }

    @Then("the active task count should be {int}")
    public void theActiveTaskCountShouldBe(int expectedCount) {
        assertEquals(expectedCount, toDoListPage.getActiveTasksCount(), 
                "Active task count should be " + expectedCount);
    }

    @Then("the completed task count should be {int}")
    public void theCompletedTaskCountShouldBe(int expectedCount) {
        assertEquals(expectedCount, toDoListPage.getCompletedTasksCount(), 
                "Completed task count should be " + expectedCount);
    }

    /**
     * User Story 5: UI-Friendly Interface - Verifications
     */
    @Then("I should see the page title")
    public void iShouldSeeThePageTitle() {
        assertTrue(toDoListPage.isPageTitleVisible(), 
                "Page title should be visible");
    }

    @Then("I should see the task input field")
    public void iShouldSeeTheTaskInputField() {
        assertTrue(toDoListPage.isTaskInputVisible(), 
                "Task input field should be visible");
    }

    @Then("I should see the \\"Add\\" button")
    public void iShouldSeeTheAddButton() {
        assertTrue(toDoListPage.isAddButtonVisible(), 
                "Add button should be visible");
    }

    @Then("I should see the task list container")
    public void iShouldSeeTheTaskListContainer() {
        assertTrue(toDoListPage.isTaskListContainerVisible(), 
                "Task list container should be visible");
    }

    @Then("I should see the statistics section")
    public void iShouldSeeTheStatisticsSection() {
        assertTrue(toDoListPage.isStatisticsSectionVisible(), 
                "Statistics section should be visible");
    }

    @Then("I should see the \\"Clear Completed\\" button")
    public void iShouldSeeTheClearCompletedButton() {
        assertTrue(toDoListPage.isClearCompletedButtonVisible(), 
                "Clear Completed button should be visible");
    }
}