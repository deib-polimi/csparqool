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

public class _union extends GraphItem {

	private List<_graph> graphs = new ArrayList<_graph>();

	public _union add(_graph graph) {
		graphs.add(graph);
		return this;
	}

	@Override
	public String getCSPARQL() throws MalformedQueryException {
		String union = "";
		int elements = graphs.size();
		for (_graph g : graphs) {
			union += "{ " + g.getCSPARQL();
			if (--elements > 0)
				union += "} UNION ";				
		}
		union += "} ";
		return union;
	}

	

}
