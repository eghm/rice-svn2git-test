/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.test;

/**
 * Base module test case that allows overriding of the test harness spring beans
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class BaseModuleTestCase extends RiceTestCase {
    protected final String moduleName;
    protected final boolean overrideTestHarness;

    /**
     * @param moduleName module name
     */
    public BaseModuleTestCase(String moduleName) {
        this(moduleName, false);
    }

    /**
     * @param moduleName module name
     * @param overrideTestHarness whether to override the test harness spring beans location
     *                            with a module-specific (MODULE>TestHarnessSpringBeans.xml)
     */
    public BaseModuleTestCase(String moduleName, boolean overrideTestHarness) {
        this.moduleName = moduleName;
        this.overrideTestHarness = overrideTestHarness;
    }

    /**
     * @see org.kuali.rice.test.RiceTestCase#getModuleName()
     */
    @Override
    protected String getModuleName() {
        return moduleName;
    }

    /**
     * Overrides to allow (enforce) per-module (MODULE)TestHarnessSpringBeans.xml 
     * @see org.kuali.rice.test.RiceTestCase#getTestHarnessSpringBeansLocation()
     */
    @Override
    protected String getTestHarnessSpringBeansLocation() {
        if (overrideTestHarness) {
            return "classpath:" + moduleName.toUpperCase() + "TestHarnessSpringBeans.xml";    
        } else {
            return DEFAULT_TEST_HARNESS_SPRING_BEANS;
        }
    }
}