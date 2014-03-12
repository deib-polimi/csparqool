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


public class _graph {

	private List<Triple> triples = new ArrayList<Triple>();
	

	public _graph add(String subject, String predicate, String object) {
		triples.add(new Triple(subject, predicate, object));
		return this;
	}
	
	public _graph add(String predicate, String object) {
		return add(triples.get(triples.size()-1).getSubject(), predicate, object);
	}

	public _graph add(Property predicate, String object) {
		return add(predicate.toString(), object);
	}
	
	
	@Override
	public String toString() {
		String graph = "";
		for (Triple t: triples) {
			graph += escape(t.getSubject()) + " " + escape(t.getPredicate()) + " " + escape(t.getObject()) + " . ";
		}
		return graph;
	}

	private String escape(String term) {
		if (isIriRef(term)) return "<"+term+">";
		return term;
	}

	private boolean isIriRef(String term) {
		return term.contains("/");
	}

	

}
