/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.cucumber.step.runner.core;

import com.consol.citrus.DefaultTestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import cucumber.api.java.en.Then;

import static com.consol.citrus.actions.SleepAction.Builder.sleep;

/**
 * @author Christoph Deppisch
 * @since 2.6
 */
public class SleepSteps {

    @CitrusResource
    private DefaultTestCaseRunner runner;

    @Then("^sleep$")
    public void doSleep() {
        runner.then(sleep());
    }

    @Then("^sleep (\\d+) ms$")
    public void doSleep(long milliseconds) {
        runner.then(sleep().milliseconds(milliseconds));
    }
}
