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

class Stream {

	private String uri;
	private String range;
	private String step;

	public Stream(String uri, String range, String step) {
		this.setURI(uri);
		this.setRange(range);
		this.setStep(step);
	}

	public String getURI() {
		return uri;
	}

	public void setURI(String name) {
		this.uri = name;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String timeStep) {
		this.step = timeStep;
	}

}
