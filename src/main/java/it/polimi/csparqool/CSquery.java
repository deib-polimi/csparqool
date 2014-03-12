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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CSquery {
	
	// sparql grammar: http://www.w3.org/TR/2013/REC-sparql11-query-20130321

	public static final String BLANK_NODE = "[]";
	public static final String AVERAGE = "AVERAGE";

	private String queryName;
	private Map<String, String> nameSpaces;
	private List<_graph> constructGraphs;
	private List<Stream> streams;
	private List<String> dataSets;
	private _select select;
	private _graph graph;

	public static CSquery createDefaultQuery(String queryName) {
		return new CSquery(queryName);
	}


	private CSquery() {
	}

	private CSquery(String queryName) {
		this.queryName = queryName;
		nameSpaces = new HashMap<String, String>();
		streams = new ArrayList<Stream>();
		dataSets = new ArrayList<String>();
	}

	public CSquery setNsPrefix(String prefix, String uri) {
		nameSpaces.put(uri, prefix);
		return this;
	}

	public CSquery construct(_graph constructGraph) {
		if (this.constructGraphs == null)
			constructGraphs = new ArrayList<_graph>();
		constructGraphs.add(constructGraph);
		return this;
	}

	public CSquery fromStream(String uri, String range, String step) {
		streams.add(new Stream(uri, range, step));
		return this;
	}

	public CSquery from(String dataSet) {
		dataSets.add(dataSet);
		return this;
	}

	public CSquery where(_select select) {
		this.select = select;
		return this;
	}
	
	public CSquery where(_graph graph) {
		this.graph = graph;
		return this;
	}

	@Override
	public String toString() {
		String query = "";
		query += "REGISTER STREAM " + escapeName(queryName) + " AS ";

		if (!nameSpaces.isEmpty()) {
			for (String uri : nameSpaces.keySet()) {
				query += "PREFIX " + nameSpaces.get(uri) + ": <" + uri + "> ";
			}
		}

		if (constructGraphs != null && ! constructGraphs.isEmpty()) {
			query += "CONSTRUCT { ";
			for (_graph constructGraph: constructGraphs) {
				query += constructGraph.toString();
			}
			query += "} ";
		}

		if (!streams.isEmpty()) {
			for (Stream stream : streams) {
				query += "FROM STREAM <" + stream.getURI() + "> [RANGE "
						+ stream.getRange() + " STEP " + stream.getStep()
						+ "] ";
			}
		}

		if (!dataSets.isEmpty()) {
			for (String dataSet : dataSets) {
				query += "FROM <" + dataSet + "> ";
			}
		}

		if (select != null) {
			query += "WHERE { { " + select.toString() + " } } ";
		} else if (graph != null) {
			query += "WHERE { { " + graph.toString() + "} } ";
		}

		return query;
	}


	private String escapeName(String name) {
		name = StringUtils.replaceEach(name, new String[] { " " },
				new String[] { "_" });
		return name;
	}

}
