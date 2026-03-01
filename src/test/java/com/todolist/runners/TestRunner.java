package com.todolist.runners;

/**
 * ==================================================================================================
 * Cucumber Test Runner - Main entry point for executing BDD tests
 *
 * This class configures and runs all Cucumber feature files.
 * It specifies the location of feature files, step definitions, and reporting options.
 *
 * Repository: https://github.com/rishitha62/to-do-list
 * ==================================================================================================
 */

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Test Runner Class for Cucumber BDD Tests
 * 
 * This class uses JUnit to run Cucumber tests.
 * It provides configuration for:
 * - Feature file locations
 * - Step definition packages
 * - Report generation
 * - Tags for selective test execution
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        // Location of feature files
        features = "src/test/resources/features",
        
        // Location of step definitions
        glue = {"com.todolist.stepdefinitions"},
        
        // Pretty print output to console
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml"
        },
        
        // Run tests with these tags (comment out to run all tests)
        // tags = "@Smoke",
        
        // Print detailed output
        monochrome = false,
        
        // Exit with non-zero status if there are pending or undefined steps
        strict = true,
        
        // Dry run - set to true to validate steps without executing them
        dryRun = false
)
public class TestRunner {
    // This class is empty, it is used only as an invocation point for JUnit
}