# ==================================================================================================
# Feature: Task Management in To-Do List Application
# Based on Business Requirements and User Stories
# Repository: https://github.com/rishitha62/to-do-list
# ==================================================================================================

@All
Feature: Task Management
  As a user
  I want to manage my tasks
  So that I can keep track of things I need to do

  # ==================================================================================================
  # User Story 1: Add Tasks
  # Acceptance Criteria:
  # - A user can enter a task and add it to their list
  # - The new task appears immediately in the list without a page reload
  # ==================================================================================================

  @AddTasks @Smoke
  Scenario: Successfully add a single task
    Given I am on the to-do list page
    When I enter "Buy groceries" in the task input field
    And I click the "Add" button
    Then the task "Buy groceries" should appear in the task list
    And the task input field should be cleared
    And the total task count should be 1
    And the active task count should be 1

  @AddTasks
  Scenario: Add multiple tasks
    Given I am on the to-do list page
    When I add the following tasks:
      | Buy groceries    |
      | Walk the dog    |
      | Finish homework |
    Then I should see 3 tasks in the task list
    And the total task count should be 3
    And the active task count should be 3

  @AddTasks @Validation
  Scenario: Cannot add empty task
    Given I am on the to-do list page
    When I click the "Add" button without entering a task
    Then no new task should be added to the list
    And the total task count should be 0

  @AddTasks @Validation
  Scenario: Cannot add whitespace-only task
    Given I am on the to-do list page
    When I enter "     " in the task input field
    And I click the "Add" button
    Then no new task should be added to the list
    And the total task count should be 0

  @AddTasks @Keyboard
  Scenario: Add task using Enter key
    Given I am on the to-do list page
    When I enter "Call mom" in the task input field
    And I press the Enter key
    Then the task "Call mom" should appear in the task list
    And the task input field should be cleared

  # ==================================================================================================
  # User Story 2: View Added Tasks
  # Acceptance Criteria:
  # - All tasks added are displayed in a clear, organized list
  # ==================================================================================================

  @ViewTasks @Smoke
  Scenario: View empty task list
    Given I am on the to-do list page
    Then I should see an empty state message
    And the total task count should be 0
    And the active task count should be 0
    And the completed task count should be 0

  @ViewTasks
  Scenario: View task list with multiple tasks
    Given I am on the to-do list page
    When I add the following tasks:
      | Task 1 |
      | Task 2 |
      | Task 3 |
    Then all tasks should be visible in the list
    And each task should have a checkbox
    And each task should have a delete button

  # ==================================================================================================
  # User Story 3: Mark Tasks as Completed
  # Acceptance Criteria:
  # - Selecting a task as completed updates its status instantly, without reloading the page
  # - Completed tasks are visually distinct from incomplete tasks
  # ==================================================================================================

  @CompleteTasks @Smoke
  Scenario: Mark a single task as completed
    Given I am on the to-do list page
    And I have added a task "Buy groceries"
    When I click the checkbox for "Buy groceries"
    Then the task "Buy groceries" should be marked as completed
    And the task text should appear with a strike-through style
    And the completed task count should be 1
    And the active task count should be 0

  @CompleteTasks
  Scenario: Unmark a completed task
    Given I am on the to-do list page
    And I have added a task "Walk the dog"
    And I have marked "Walk the dog" as completed
    When I click the checkbox for "Walk the dog" again
    Then the task "Walk the dog" should be marked as incomplete
    And the strike-through style should be removed
    And the completed task count should be 0
    And the active task count should be 1

  @CompleteTasks
  Scenario: Mark multiple tasks as completed
    Given I am on the to-do list page
    And I have added the following tasks:
      | Task 1 |
      | Task 2 |
      | Task 3 |
    When I mark 2 tasks as completed
    Then the completed task count should be 2
    And the active task count should be 1
    And the total task count should be 3

  # ==================================================================================================
  # User Story 4: Delete Tasks
  # Acceptance Criteria:
  # - A user can delete a task, and it is removed from the list immediately without a page reload
  # ==================================================================================================

  @DeleteTasks @Smoke
  Scenario: Delete a single task
    Given I am on the to-do list page
    And I have added a task "Buy groceries"
    When I click the delete button for "Buy groceries"
    Then the task "Buy groceries" should not be visible in the list
    And the total task count should be 0
    And I should see the empty state message

  @DeleteTasks
  Scenario: Delete multiple tasks
    Given I am on the to-do list page
    And I have added the following tasks:
      | Task 1 |
      | Task 2 |
      | Task 3 |
    When I delete "task 1"
    Then the total task count should be 2
    When I delete "task 2"
    Then the total task count should be 1

  @DeleteTasks
  Scenario: Delete a completed task
    Given I am on the to-do list page
    And I have added a task "Finish homework"
    And I have marked "Finish homework" as completed
    When I click the delete button for "Finish homework"
    Then the task "Finish homework" should not be visible in the list
    And the completed task count should be 0

  @DeleteTasks @Batch
  Scenario: Clear all completed tasks
    Given I am on the to-do list page
    And I have added the following tasks:
      | Task 1 |
      | Task 2 |
      | Task 3 |
      | Task 4 |
    And I have marked 3 tasks as completed
    When I click the "Clear Completed" button
    Then only 1 task should remain in the list
    And the completed task count should be 0
    And the active task count should be 1

  # ==================================================================================================
  # User Story 5: User-Friendly Interface
  # Acceptance Criteria:
  # - The design is clear, with obvious options for adding, viewing, marking, and deleting tasks
  # ==================================================================================================

  @UI @Smoke
  Scenario: Verify UI elements are present
    Given I am on the to-do list page
    Then I should see the page title
    And I should see the task input field
    And I should see the "Add" button
    And I should see the task list container
    And I should see the statistics section
    And I should see the "Clear Completed" button