/*
 * Copyright 2006-2015 the original author or authors.
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

package com.consol.citrus.dsl.runner;

import com.consol.citrus.TestCase;
import com.consol.citrus.actions.EchoAction;
import com.consol.citrus.container.IteratingConditionExpression;
import com.consol.citrus.container.RepeatOnErrorUntilTrue;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.builder.BuilderSupport;
import com.consol.citrus.dsl.builder.RepeatOnErrorBuilder;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RepeatOnErrorTestRunnerTest extends AbstractTestNGUnitTest {
    @Test
    public void testRepeatOnErrorBuilder() {
        MockTestRunner builder = new MockTestRunner(getClass().getSimpleName(), applicationContext) {
            @Override
            public void execute() {
                variable("var", "foo");

                repeatOnError(new BuilderSupport<RepeatOnErrorBuilder>() {
                    @Override
                    public void configure(RepeatOnErrorBuilder builder) {
                        builder.autoSleep(250)
                                .until("i gt 5");
                    }
                }).actions(echo("${var}"), sleep(50), echo("${var}"));

                repeatOnError(new BuilderSupport<RepeatOnErrorBuilder>() {
                    @Override
                    public void configure(RepeatOnErrorBuilder builder) {
                        builder.autoSleep(200)
                                .index("k")
                                .startsWith(2)
                                .until("k gt= 5");
                    }
                }).actions(echo("${var}"));
            }
        };

        TestContext context = builder.createTestContext();
        Assert.assertNotNull(context.getVariable("i"));
        Assert.assertEquals(context.getVariable("i"), "1");
        Assert.assertNotNull(context.getVariable("k"));
        Assert.assertEquals(context.getVariable("k"), "2");

        TestCase test = builder.getTestCase();
        assertEquals(test.getActionCount(), 2);
        assertEquals(test.getActions().get(0).getClass(), RepeatOnErrorUntilTrue.class);
        assertEquals(test.getActions().get(0).getName(), "repeat-on-error");
        
        RepeatOnErrorUntilTrue container = (RepeatOnErrorUntilTrue)test.getActions().get(0);
        assertEquals(container.getActionCount(), 3);
        assertEquals(container.getAutoSleep(), Long.valueOf(250L));
        assertEquals(container.getCondition(), "i gt 5");
        assertEquals(container.getStart(), 1);
        assertEquals(container.getIndexName(), "i");
        assertEquals(container.getTestAction(0).getClass(), EchoAction.class);

        container = (RepeatOnErrorUntilTrue)test.getActions().get(1);
        assertEquals(container.getActionCount(), 1);
        assertEquals(container.getAutoSleep(), Long.valueOf(200L));
        assertEquals(container.getCondition(), "k gt= 5");
        assertEquals(container.getStart(), 2);
        assertEquals(container.getIndexName(), "k");
        assertEquals(container.getTestAction(0).getClass(), EchoAction.class);
    }

    @Test
    public void testRepeatOnErrorBuilderWithConditionExpression() {
        MockTestRunner builder = new MockTestRunner(getClass().getSimpleName(), applicationContext) {
            @Override
            public void execute() {
                variable("var", "foo");

                repeatOnError(new BuilderSupport<RepeatOnErrorBuilder>() {
                    @Override
                    public void configure(RepeatOnErrorBuilder builder) {
                        builder.autoSleep(250)
                                .until("i gt 5");
                    }
                }).actions(echo("${var}"), sleep(50), echo("${var}"));

                repeatOnError(new BuilderSupport<RepeatOnErrorBuilder>() {
                    @Override
                    public void configure(RepeatOnErrorBuilder builder) {
                        builder.autoSleep(200)
                                .index("k")
                                .startsWith(2)
                                .until(new IteratingConditionExpression() {
                                    @Override
                                    public boolean evaluate(int index, TestContext context) {
                                        return index >= 5;
                                    }
                                });
                    }
                }).actions(echo("${var}"));
            }
        };

        TestContext context = builder.createTestContext();
        Assert.assertNotNull(context.getVariable("i"));
        Assert.assertEquals(context.getVariable("i"), "1");
        Assert.assertNotNull(context.getVariable("k"));
        Assert.assertEquals(context.getVariable("k"), "2");

        TestCase test = builder.getTestCase();
        assertEquals(test.getActionCount(), 2);
        assertEquals(test.getActions().get(0).getClass(), RepeatOnErrorUntilTrue.class);
        assertEquals(test.getActions().get(0).getName(), "repeat-on-error");

        RepeatOnErrorUntilTrue container = (RepeatOnErrorUntilTrue)test.getActions().get(0);
        assertEquals(container.getActionCount(), 3);
        assertEquals(container.getAutoSleep(), Long.valueOf(250L));
        assertEquals(container.getCondition(), "i gt 5");
        assertEquals(container.getStart(), 1);
        assertEquals(container.getIndexName(), "i");
        assertEquals(container.getTestAction(0).getClass(), EchoAction.class);

        container = (RepeatOnErrorUntilTrue)test.getActions().get(1);
        assertEquals(container.getActionCount(), 1);
        assertEquals(container.getAutoSleep(), Long.valueOf(200L));
        assertEquals(container.getStart(), 2);
        assertEquals(container.getIndexName(), "k");
        assertEquals(container.getTestAction(0).getClass(), EchoAction.class);
    }
}
