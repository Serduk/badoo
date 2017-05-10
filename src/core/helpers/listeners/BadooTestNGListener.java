package core.helpers.listeners;

import core.random.RandomUtils;
import org.testng.*;

/**
 * Listener for TestNG Test Annotations e.g. (@Test, @BeforeClass, @Method, etc.)
 * Created by serdyuk on 9/18/16.
 */

public class BadooTestNGListener extends TestListenerAdapter implements IRetryAnalyzer {


    private static final String TYPE_SUITE = "[SUITE]";
    private static final String TYPE_TEST = "[TEST]";

    private static final String STATUS_START = "\033[1;36m [START] \033[0m"; // cyan
    private static final String STATUS_SUCCESS = "\033[1;32m [SUCCESS] \033[0m"; // green
    private static final String STATUS_FAILED = "\033[1;31m [FAILED] \033[0m"; // red
    private static final String STATUS_SKIP = "\033[1;33m [SKIP] \033[0m"; // yellow
    private static final String STATUS_RETRY = "\033[1;35m [RETRY] \033[0m"; // magneta;
    private static final String STATUS_FINISH = "\033[1;36m [FINISH] \033[0m"; // cyan


    private static Runnable doOnTestStart;
    private static Runnable doOnTestSuccess;
    private static Runnable doOnTestFailed;
    private static Runnable doOnTestRetry;

    private static int retryCount = 0;
    private static int currentRetries = 0;
    private static String failedScreenShot;


    private static ITestResult currentTestResult;


    /**
     * @param result ITestResult
     * @return String имя выполняемого метода.
     */
    public static String getMethodName(ITestResult result) {
        return result.getMethod().getConstructorOrMethod().getName();
    }

    /**
     * пишит в консоль что указано.
     *
     * @param type
     * @param testName
     * @param status
     */
    public static void printTestStatus(String type, String testName, String status) {

        RandomUtils random = new RandomUtils();

        switch (status) {
            case STATUS_START:
                System.out.println("\033[34m " + random.getTime(":") + " \033[0m \033[36m" + type + " : " + testName + " \033[0m " + status);
                break;
            case STATUS_SUCCESS:
                System.out.println("\033[34m " + random.getTime(":") + " \033[0m \033[32m" + type + " : " + testName + " \033[0m " + status);
                break;
            case STATUS_SKIP:
                System.out.println("\033[34m " + random.getTime(":") + " \033[0m \033[33m " + type + " : " + testName + " \033[0m " + status);
                break;
            case STATUS_FAILED:
                System.out.println("\033[34m " + random.getTime(":") + " \033[0m \033[31m" + type + " : " + testName + " \033[0m " + status);
                break;
            default:
                System.out.println("\033[34m " + random.getTime(":") + " " + type + " : " + testName + " \033[0m " + status);
        }

    }

    public static ITestResult getCurrentTestResult() {
        return currentTestResult;
    }

    /**
     * Повторять ли тесты при фейле 0 - значит нет. > 0  - кол-во повторений.
     *
     * @param count
     */
    public static void setRetryTestCount(int count) {
        retryCount = count;
    }

    private boolean isRetryTestAllow() {
        return retryCount > 0;
    }

    /**
     * Лямбда метод(а-ля маппер)
     * Сетится, если надо выполнить функцию(executedMethod) при успешном выполнении теста.
     *
     * @param executeMethod Callable some method.
     */
    public static void setMethodOnTestSuccess(Runnable executeMethod) {
        doOnTestSuccess = executeMethod;
    }

    /**
     * Лямбда метод(а-ля маппер)
     * Сетится, если надо выполнить функцию(executedMethod) при старте теста.
     *
     * @param executeMethod Callable some method.
     */
    public static void setMethodOnTestStart(Runnable executeMethod) {
        doOnTestStart = executeMethod;
    }


    /**
     * Лямбда метод(а-ля маппер)
     * Сетится, если надо выполнить функцию(executedMethod) при фейле теста.
     *
     * @param executeMethod Callable some method.
     */
    public static void setMethodOnTestFailed(Runnable executeMethod) {
        doOnTestFailed = executeMethod;
    }

    /**
     * Лямбда метод(а-ля маппер)
     * Сетится, если надо выполнить функцию(executedMethod) при фейле теста.
     *
     * @param executeMethod Callable some method.
     */
    public static void setMethodOnTestRetry(Runnable executeMethod) {
        doOnTestRetry = executeMethod;
    }


    /**
     * тут выполняется все действия перед стартом теста (@Test).
     *
     * @param result ITestResult
     */
    @Override
    public void onTestStart(ITestResult result) {
        currentTestResult = result;

        if (isRetryTestAllow()) {
            result.getMethod().setRetryAnalyzer(new BadooTestNGListener());
        }
        if (doOnTestStart != null) {
            try {
                doOnTestStart.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        printTestStatus(TYPE_TEST, getMethodName(result), STATUS_START);
    }

    /**
     * тут выполняются действия если тест(@Test) успешно пройден
     *
     * @param result ITestResult
     */
    @Override
    public void onTestSuccess(ITestResult result) {

        if (doOnTestSuccess != null) {
            try {
                doOnTestSuccess.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        printTestStatus(TYPE_TEST, getMethodName(result), STATUS_SUCCESS);
    }

    @Override
    public void onTestFailure(ITestResult result) {

        if (doOnTestFailed != null) {
            try {
                doOnTestFailed.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (failedScreenShot != null) {
            printTestStatus(TYPE_TEST, getMethodName(result) + "\n[SCREENSHOT] : " + failedScreenShot, STATUS_FAILED);
        } else {
            printTestStatus(TYPE_TEST, getMethodName(result), STATUS_FAILED);
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {

        printTestStatus(TYPE_TEST, getMethodName(result), STATUS_SKIP);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        printTestStatus(TYPE_SUITE, context.getName(), STATUS_START);

    }

    @Override
    public void onFinish(ITestContext context) {

        printTestStatus(TYPE_SUITE, context.getName(), STATUS_FINISH);

    }

    @Override
    public boolean retry(ITestResult result) {
        if (isRetryTestAllow() && retryCount > currentRetries) {
            if (doOnTestRetry != null) {
                try {
                    doOnTestRetry.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            printTestStatus(TYPE_TEST, getMethodName(result), STATUS_RETRY);
            currentRetries++;
            return true;
        }
        return false;
    }


    public static void attachScreenShot(String link) {
        BadooTestNGListener.failedScreenShot = link;
    }
}
