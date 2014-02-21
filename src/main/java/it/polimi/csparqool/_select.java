/**
 * Copyright 2014 deib-polimi
 * Contact: Marco Miglierina <marco.miglierina@polimi.it>
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

	public _select add(String outputName, String inputName, String aggregation)
			throws MalformedQueryException {
		if (!CSquery.isWellFormedVariableName(inputName))
			throw new MalformedQueryException("Input variable name '"
					+ inputName + "' is not well formed");
		if (!CSquery.isWellFormedVariableName(outputName))
			throw new MalformedQueryException("Output variable name '"
					+ outputName + "' is not well formed");

		String selectItem = "(";

		switch (aggregation) {
		case CSquery.AVERAGE:
			selectItem += "AVG";
			break;
		default:
			throw new MalformedQueryException(
					"There is no current implementation of aggregation "
							+ aggregation);
		}

		selectItem += "(" + inputName + ") AS " + outputName + ")";
		selectItems.add(selectItem);
		return this;
	}

	public _select add(String varName) throws MalformedQueryException {
		if (!CSquery.isWellFormedVariableName(varName))
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
		if (graph != null) throw new MalformedQueryException("Only either select or graph can be inside a where clause");
		this.select = select;
		return this;
	}

	public _select where(_graph graph) throws MalformedQueryException {
		if (select != null) throw new MalformedQueryException("Only either select or graph can be inside a where clause");
		this.graph = graph;
		return this;
	}

	@Override
	public String toString() {
		String selectString = "";

			selectString += "SELECT ";
			for (String selectItem : selectItems) {
				selectString += selectItem + " ";
			}

			selectString += "WHERE { " + (select != null ? select.toString() : graph.toString() ) + "} ";

			if (groupByVar != null) {
				selectString += "GROUP BY " + groupByVar + " ";
			}

			if (condition != null) {
				selectString += "HAVING (" + condition + ") ";
			}

		return selectString;
	}

}
