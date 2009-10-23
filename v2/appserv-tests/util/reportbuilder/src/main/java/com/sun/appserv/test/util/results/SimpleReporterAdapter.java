package com.sun.appserv.test.util.results;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class SimpleReporterAdapter implements Serializable {
    public static final String PASS = "pass";
    public static final String DID_NOT_RUN = "did_not_run";
    public static final String FAIL = "fail";
    private static final Pattern TOKENIZER;

    private final boolean debug = true;
    private final Map<String, String> testCaseStatus = new TreeMap<String, String>();
    private final String testSuiteName = getTestSuiteName();
    private final String testSuiteID = testSuiteName + "ID";
    private String testSuiteDescription;
    private String ws_home = "appserv-tests";

    static {
        String pattern = or(
            split("x", "X"),     // AbcDef -> Abc|Def
            split("X", "Xx"),    // USArmy -> US|Army
            //split("\\D","\\d"), // SSL2 -> SSL|2
            split("\\d", "\\D")  // SSL2Connector -> SSL|2|Connector
        );
        pattern = pattern.replace("x", "\\p{Lower}").replace("X", "\\p{Upper}");
        TOKENIZER = Pattern.compile(pattern);
    }

    public SimpleReporterAdapter() {
    }

    public SimpleReporterAdapter(String ws_root) {
        ws_home = ws_root;
    }

    public void addStatus(String test, String status) {
        int blankIndex = test.indexOf(" ");
        String key = test;
        if (blankIndex != -1) {
            key = test.substring(test.indexOf(" "));
        }
        key = key.trim();
        if (debug) {
            System.out.println("Value of key is:" + key);
        }
        if (!testCaseStatus.containsKey(key)) {
            testCaseStatus.put(key, status.toLowerCase());
        }
    }

    public void addDescription(String s) {
        testSuiteDescription = s;
    }

    public void printStatus() {
        try {
            final Reporter reporter = Reporter.getInstance(ws_home);
            if (debug) {
                System.out.println("Generating report at " + reporter.getResultFile());
            }
            reporter.setTestSuite(testSuiteID, testSuiteName, testSuiteDescription);
            reporter.addTest(testSuiteID, testSuiteID, testSuiteName);
            int pass = 0;
            int fail = 0;
            int d_n_r = 0;
            System.out.println("\n\n-----------------------------------------");
            for (String testCaseName : testCaseStatus.keySet()) {
                String status = testCaseStatus.get(testCaseName);
                if (status.equalsIgnoreCase(PASS)) {
                    pass++;
                } else if (status.equalsIgnoreCase(DID_NOT_RUN)) {
                    d_n_r++;
                } else {
                    fail++;
                }
                System.out.println(String.format("- %-37s -", testCaseName + ": " + status.toUpperCase()));
                reporter.addTestCase(testSuiteID, testSuiteID, testCaseName + "ID", testCaseName);
                reporter.setTestCaseStatus(testSuiteID, testSuiteID, testCaseName + "ID", status);
            }
            System.out.println("-----------------------------------------");
            result("PASS", pass);
            result("FAIL", fail);
            result("DID NOT RUN", d_n_r);
            System.out.println("-----------------------------------------");
            reporter.flushAll();
            createConfirmationFile();
        }
        catch (Throwable ex) {
            System.out.println("Reporter exception occurred!");
            if (debug) {
                ex.printStackTrace();
            }
        }
    }

    private void result(final String label, final int count) {
        System.out.println(String.format("- Total %-12s: %-17d -", label, count));
    }

    public void createConfirmationFile() {
        try {
            FileOutputStream fout = new FileOutputStream("RepRunConf.txt");
            try {
                fout.write("Test has been reported".getBytes());
            } finally {
                fout.close();
            }
        } catch (Exception e) {
            System.out.println("Exception while creating confirmation file!");
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    public void printSummary(String s) {
        printStatus();
    }

    public void printSummary() {
        printStatus();
    }

    public void run() {
        printSummary();
    }

    private String getTestSuiteName() {
        final File file = new File("").getAbsoluteFile();
        StringBuilder buf = new StringBuilder(file.getName().length());
        for (String t : TOKENIZER.split(file.getName())) {
            if (buf.length() > 0)
                buf.append('-');
            buf.append(t.toLowerCase());
        }
        return buf.toString().trim();
    }

    public void clearStatus() {
        testCaseStatus.clear();
    }

    private static String or(String... tokens) {
        StringBuilder buf = new StringBuilder();
        for (String t : tokens) {
            if (buf.length() > 0)
                buf.append('|');
            buf.append(t);
        }
        return buf.toString();
    }

    private static String split(String lookback, String lookahead) {
        return "((?<=" + lookback + ")(?=" + lookahead + "))";
    }
}