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

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class _graph {

	private List<GraphItem> graphItems = new ArrayList<GraphItem>();
	private List<String> filters = new ArrayList<String>();

	public _graph add(String subject, String predicate, String object) {
		graphItems.add(new Triple(subject, predicate, object));
		return this;
	}

	public _graph addTransitive(String subject, String predicate, String object) {
		Triple transitiveTriple = new Triple(subject, predicate, object);
		transitiveTriple.setTransitive(true);
		graphItems.add(transitiveTriple);
		return this;
	}

	public _graph add(_union union) {
		graphItems.add(union);
		return this;
	}

	public _graph add(String predicate, String object) {
		return add(null, predicate, object);
	}

	public _graph add(Property predicate, String object) {
		return add(predicate.toString(), object);
	}

	public String getCSPARQL() throws MalformedQueryException {
		String graph = "";
		GraphItem current = null;
		for (GraphItem next : graphItems) {
			if (current != null) {
				graph += current.getCSPARQL()
						+ ((next instanceof Triple && ((Triple) next)
								.hasSubject()) ? ". " : "; ");
			}
			current = next;
		}
		if (current != null) {
			graph += current.getCSPARQL() + ". ";
		}
		for (String filter : filters) {
			graph += "FILTER ( " + filter + " ) . ";
		}
		return graph;
	}

	public _graph add(String subject, Property property, String object) {
		return add(subject, property.toString(), object);
	}

	public _graph add(String subject, Property property, Resource object) {
		return add(subject, property.toString(), object.toString());
	}

	public _graph addTransitive(String subject, Property property, String object) {
		return addTransitive(subject, property.toString(), object);
	}

	public _graph addTransitive(String subject, Property property,
			Resource object) {
		return addTransitive(subject, property.toString(), object.toString());
	}

	public _graph filter(String filter) {
		filters.add(filter);
		return this;
	}

}
