package com.simplesearchengine.application;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DocumentTests.class,
        IndexBaseImplTest.class,
        TfidfImplTests.class,
        SearchEngineImplTests.class,
        ApplicationTests.class
})

public class AllTests {
}
