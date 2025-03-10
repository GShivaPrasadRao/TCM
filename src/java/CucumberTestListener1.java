package com.myproject.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CucumberTestListener1 implements ITestListener {

    public CucumberTestListener() {
        System.out.println("🔹 CucumberTestListener: Constructor called");
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("🚀 CucumberTestListener: Test Execution Started");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("🔵 Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("❌ Test Failed: " + result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("🏁 CucumberTestListener: Test Execution Completed.");
    }
}
