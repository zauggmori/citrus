/*
 * Copyright 2006-2010 the original author or authors.
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

package com.consol.citrus.actions;

import com.consol.citrus.UnitTestSupport;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
public class EchoActionTest extends UnitTestSupport {

	@Test
	public void testEchoMessage() {
		EchoAction echo = new EchoAction.Builder().message("Hello Citrus!").build();
		echo.execute(context);
	}

	@Test
	public void testEchoMessageWithVariables() {
		EchoAction echo = new EchoAction.Builder().message("${greeting} Citrus!").build();
		context.setVariable("greeting", "Hello");

		echo.execute(context);
	}

	@Test
	public void testEchoMessageWithFunctions() {
		EchoAction echo = new EchoAction.Builder().message("Today is citrus:currentDate()").build();
		echo.execute(context);
	}

	@Test(expectedExceptions = {CitrusRuntimeException.class})
	public void testEchoMessageWithUnknownVariables() {
		EchoAction echo = new EchoAction.Builder().message("${greeting} Citrus!").build();
		echo.execute(context);
	}
}
