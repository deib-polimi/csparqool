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

import java.util.ArrayList;
import java.util.List;

public class _body {

	private List<String> selectItems = new ArrayList<String>();
	private String groupByVar;
	private String condition;
	private _body body;
	private _graph graph;

	public _body selectFunction(String outputVar, String aggregation,
			String... parameters) throws MalformedQueryException {
		// TODO parameters should be validated based on aggregation
		if (!Validator.checkVariable(outputVar))
			throw new MalformedQueryException("Output variable name '"
					+ outputVar + "' is not well formed");

		String selectItem = "(";

		if (aggregation != null) {

			switch (aggregation) {
			case Function.AVERAGE:
				selectItem += "AVG";
				break;
			case Function.SUM:
				selectItem += "SUM";
				break;
			case Function.TIMESTAMP:
				selectItem += "f:timestamp";
				break;
			case Function.PERCENTILE:
				selectItem += "PERCENTILE";
				break;
			case Function.MAX:
				selectItem += "MAX";
				break;
			case Function.MIN:
				selectItem += "MIN";
				break;
			case Function.COUNT:
				selectItem += "COUNT";
				break;
			default:
				throw new MalformedQueryException(
						"There is no current implementation of aggregation "
								+ aggregation);
			}
			selectItem += "(";
			int i;
			for (i = 0; i < parameters.length - 1; i++) {
				selectItem += parameters[i] + ", ";
			}
			selectItem += parameters[i] + ") ";
		} else {
			if (parameters==null || parameters.length!=1)
				throw new MalformedQueryException("Wrong number of parameters");
			selectItem += parameters[0] + " ";
		}
		selectItem += "AS " + outputVar + ") ";
		selectItems.add(selectItem);
		return this;
	}

	public _body select(String... variables) throws MalformedQueryException {
		for (String var : variables) {
			if (!Validator.checkVariable(var))
				throw new MalformedQueryException("Variable name '" + var
						+ "' is not well formed");
			selectItems.add(var);
		}
		return this;
	}

	public _body groupby(String variable) {
		this.groupByVar = variable;
		return this;
	}

	public _body having(String condition) {
		this.condition = condition;
		return this;
	}

	public _body where(_body body) throws MalformedQueryException {
		if (graph != null)
			throw new MalformedQueryException(
					"Only either body or graph can be inside a where clause");
		this.body = body;
		return this;
	}

	public _body where(_graph graph) throws MalformedQueryException {
		if (body != null)
			throw new MalformedQueryException(
					"Only either body or graph can be inside a where clause");
		this.graph = graph;
		return this;
	}

	public String getCSPARQL() throws MalformedQueryException {
		String bodyString = "";

		if (selectItems.isEmpty())
			throw new MalformedQueryException("No selection is specified");

		bodyString += "SELECT ";
		for (String selectItem : selectItems) {
			bodyString += selectItem + " ";
		}

		if (body == null && graph == null)
			throw new MalformedQueryException("Body of WHERE is missing");

		bodyString += "WHERE { "
				+ (body != null ? body.getCSPARQL() : graph.getCSPARQL())
				+ "} ";

		if (groupByVar != null) {
			bodyString += "GROUP BY " + groupByVar + " ";
		}

		if (condition != null) {
			bodyString += "HAVING (" + condition + ") ";
		}

		return bodyString;
	}

}
