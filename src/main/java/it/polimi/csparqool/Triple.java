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

class Triple extends GraphItem {

	private String subject;
	private String predicate;
	private String object;
	private boolean isTransitive = false;

	public Triple(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	public void setTransitive(boolean value) {
		this.isTransitive = value;
	}

	private String escape(String term) {
		if (isIriRef(term))
			return "<" + term + ">";
		if (isLiteral(term))
			return "\"" + term + "\"";
		return term;
	}

	private boolean isLiteral(String term) {
		return !isIriRef(term) && !isPrefixedIri(term)
				&& !isVariable(term) && !term.contains("[");
	}

	private boolean isVariable(String term) {
		return term.startsWith("?");
	}

	private boolean isPrefixedIri(String term) {
		return term.contains(":") && !term.contains("/");
	}

	private boolean isIriRef(String term) {
		return term.contains("/");
	}

	@Override
	public String getCSPARQL() throws MalformedQueryException {
		return (subject != null ? escape(subject) + " " : "")
				+ escape(predicate) + (isTransitive ? "+ " : " ")
				+ escape(object) + " ";
	}

	public boolean hasSubject() {
		return subject != null;
	}
}
