/**
 * Copyright 2014 deib-polimi
 * Contact: deib-polimi <marco.miglierina@polimi.it>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.polimi.csparqool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FunctionArgs {
	public static final String INPUT_VARIABLE = "inputVariable";
	public static final String SUBJECT = "subject";
	public static final String PREDICATE = "predicate";
	public static final String OBJECT = "object";
	public static final String TH_PERCENTILE = "thPercentile";
	
	private static Map<String,String[]> functionsParameters;
	
	static {
		functionsParameters = new HashMap<String, String[]>();
		functionsParameters.put(Function.AVERAGE, new String[]{INPUT_VARIABLE});
		functionsParameters.put(Function.TIMESTAMP, new String[]{SUBJECT,PREDICATE,OBJECT});
		functionsParameters.put(Function.PERCENTILE, new String[]{INPUT_VARIABLE,TH_PERCENTILE});
		functionsParameters.put(Function.MAX, new String[]{INPUT_VARIABLE});
		functionsParameters.put(Function.MAX, new String[]{INPUT_VARIABLE});
	}


	public static int getNumberOfArgs(String aggregateFunction) {
		return functionsParameters.get(aggregateFunction).length;
	}


	public static int getArgIdx(String aggregateFunction, String parameterName) {
		return Arrays.asList(functionsParameters.get(aggregateFunction)).indexOf(parameterName);
	}
}
