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

public class _select {

	private List<String> selectItems = new ArrayList<String>();
	private String groupByVar;
	private String condition;
	private _select select;
	private _graph graph;

	public _select add(String outputVar, String[] parameters, String aggregation)
			throws MalformedQueryException {
		// TODO parameters should be validated based on aggregation
		if (!Validator.checkVariable(outputVar))
			throw new MalformedQueryException("Output variable name '"
					+ outputVar + "' is not well formed");

		String selectItem = "(";

		switch (aggregation) {
		case Aggregation.AVERAGE:
			selectItem += "AVG";
			break;
		case Aggregation.TIMESTAMP:
			selectItem += "f:timestamp";
			break;
		default:
			throw new MalformedQueryException(
					"There is no current implementation of aggregation "
							+ aggregation);
		}
		selectItem += "( ";
		int i;
		for (i = 0; i<parameters.length-1; i++) {
			selectItem += parameters[i] + ", ";
		}
		selectItem += parameters[i] + ") AS " + outputVar + ") ";
		selectItems.add(selectItem);
		return this;
	}

	public _select add(String varName) throws MalformedQueryException {
		if (!Validator.checkVariable(varName))
			throw new MalformedQueryException("Variable name '" + varName
					+ "' is not well formed");
		selectItems.add(varName);
		return this;
	}

	public _select groupby(String variable) {
		this.groupByVar = variable;
		return this;
	}

	public _select having(String condition) {
		this.condition = condition;
		return this;
	}

	public _select where(_select select) throws MalformedQueryException {
		if (graph != null)
			throw new MalformedQueryException(
					"Only either select or graph can be inside a where clause");
		this.select = select;
		return this;
	}

	public _select where(_graph graph) throws MalformedQueryException {
		if (select != null)
			throw new MalformedQueryException(
					"Only either select or graph can be inside a where clause");
		this.graph = graph;
		return this;
	}

	public String getCSPARQL() throws MalformedQueryException {
		String selectString = "";

		if (selectItems.isEmpty()) throw new MalformedQueryException("No selection is specified");
		
		selectString += "SELECT ";
		for (String selectItem : selectItems) {
			selectString += selectItem + " ";
		}
		
		if (select == null && graph==null) throw new MalformedQueryException("Body of WHERE is missing");

		selectString += "WHERE { "
				+ (select != null ? select.getCSPARQL() : graph.getCSPARQL())
				+ "} ";

		if (groupByVar != null) {
			selectString += "GROUP BY " + groupByVar + " ";
		}

		if (condition != null) {
			selectString += "HAVING (" + condition + ") ";
		}

		return selectString;
	}

}
