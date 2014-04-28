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
import java.util.UUID;

public class CSquery {
	
	// sparql grammar: http://www.w3.org/TR/2013/REC-sparql11-query-20130321

	public static final String BLANK_NODE = "[]";

	private static final String functionsPrefix = "f";
	private static final String functionsURI = "http://larkc.eu/csparql/sparql/jena/ext#";

	private String name;
	private Map<String, String> nameSpaces;
	private List<_graph> constructGraphs;
	private List<Stream> streams;
	private List<String> dataSets;
	private _body selectBody;
	private _graph graph;
	private List<String> selectItems;

	/**
	 * Query name must contain only numbers and letters
	 * 
	 * @param queryName
	 * @return
	 * @throws MalformedQueryException
	 */
	public static CSquery createDefaultQuery(String queryName) throws MalformedQueryException {
		return new CSquery(queryName);
	}

	private CSquery(String queryName) throws MalformedQueryException {
		validateName(queryName);
		this.name = queryName;
		nameSpaces = new HashMap<String, String>();
		streams = new ArrayList<Stream>();
		dataSets = new ArrayList<String>();
		selectItems = new ArrayList<String>();
		constructGraphs = new ArrayList<_graph>();
	}

	public CSquery setNsPrefix(String prefix, String uri) {
		nameSpaces.put(uri, prefix);
		return this;
	}

	public CSquery construct(_graph constructGraph) {
		constructGraphs.add(constructGraph);
		return this;
	}
	
	public CSquery select(String... variables) {
		for (String var: variables)
			selectItems.add(var);
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

	public CSquery where(_body select) {
		this.selectBody = select;
		return this;
	}
	
	public CSquery where(_graph graph) {
		this.graph = graph;
		return this;
	}
	
	public String getCSPARQL() throws MalformedQueryException {
		String query = "";
		
		boolean isConstructQuery = constructGraphs != null && ! constructGraphs.isEmpty();
		
		if (isConstructQuery)
			query += "REGISTER STREAM " + name + " AS ";
		else
			query += "REGISTER QUERY " + name + " AS ";

		if (!nameSpaces.isEmpty()) {
			for (String uri : nameSpaces.keySet()) {
				query += "PREFIX " + nameSpaces.get(uri) + ": <" + uri + "> ";
			}
		}

		if (isConstructQuery) {
			query += "CONSTRUCT { ";
			for (_graph constructGraph: constructGraphs) {
				query += constructGraph.getCSPARQL();
			}
			query += "} ";
		} else {
			query += "SELECT ";
			for (String selectItem : selectItems) {
				query += selectItem + " ";
			}
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

		if (selectBody != null) {
			query += "WHERE { { " + selectBody.getCSPARQL() + " } } ";
		} else if (graph != null) {
			query += "WHERE { { " + graph.getCSPARQL() + "} } ";
		}

		return query;
	}


	public static void validateName(String name) throws MalformedQueryException {
		if (!name.matches("[a-zA-Z0-9]+")) throw new MalformedQueryException("Query name must contain only letters and numbers");
	}


	public String getName() {
		return this.name;
	}

	public static String escapeName(String name) {
		return name.replaceAll("[^a-zA-Z0-9]", "");
	}

	public static String generateRandomName() {
		return escapeName(UUID.randomUUID().toString());
	}

	

	public static String getFunctionsPrefix() {
		return functionsPrefix ;
	}

	public static String getFunctionsURI() {
		return functionsURI;
	}

}
