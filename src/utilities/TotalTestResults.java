public class TotalTestResults {
private void incrementTagTotal(List<String> tags) {
    for (String tag : tags) {
        String cleanTag = tag.replace("@", "");
        tagResults.compute(cleanTag, (key, value) -> {
            if (value == null) {
                return new int[]{1, 0, 0};
            } else {
                value[0]++;
                return value;
            }
        });
    }
}

private void incrementTagPassed(List<String> tags) {
    for (String tag : tags) {
        String cleanTag = tag.replace("@", "");
        tagResults.compute(cleanTag, (key, value) -> {
            if (value == null) {
                return new int[]{0, 1, 0};
            } else {
                value[1]++;
                return value;
            }
        });
    }
}

private void incrementTagFailed(List<String> tags) {
    for (String tag : tags) {
        String cleanTag = tag.replace("@", "");
        tagResults.compute(cleanTag, (key, value) -> {
            if (value == null) {
                return new int[]{0, 0, 1};
            } else {
                value[2]++;
                return value;
            }
        });
    }
}



private void onTestCaseStart(TestCaseStarted event) {
    totalTests++;
    List<String> tags = event.getTestCase().getTags();
    incrementTagTotal(tags); // Increment total count on test start
}

private void onTestCaseFinish(TestCaseFinished event) {
    Result.Type status = event.getResult().getStatus();
    List<String> tags = event.getTestCase().getTags();
    if (status == Result.Type.PASSED) {
        passedTests++;
        incrementTagPassed(tags); // Increment passed count on test pass
    } else if (status == Result.Type.FAILED) {
        failedTests++;
        incrementTagFailed(tags); // Increment failed count on test fail
    }
}

}