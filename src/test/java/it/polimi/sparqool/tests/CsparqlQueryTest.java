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
package it.polimi.sparqool.tests;

import static org.junit.Assert.fail;
import it.polimi.csparqool.Aggregation;
import it.polimi.csparqool.CSquery;
import it.polimi.csparqool.graph;
import it.polimi.csparqool.select;

import org.apache.commons.lang.WordUtils;
import org.junit.Test;

import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class CsparqlQueryTest {

	@Test
	public void test() {
		String timeWindow = "60s";
		String timeStep = "60s";
		String kbURI = "http://localhost:8175";
		CSquery query = CSquery.createDefaultQuery("CPU Utilization Rule");
		String streamURI = "http://ex.org/streams/cpu_utilization";

		try {
			query.setNsPrefix("xsd", XSD.getURI())
					.setNsPrefix("rdf", RDF.getURI())
					.setNsPrefix("rdfs", RDFS.getURI())
					.setNsPrefix("mc", "http://ex.org/")
					.construct(
							graph.add(CSquery.BLANK_NODE, RDF.type.toString(),
									"mc:Violation"))
					.fromStream(streamURI, timeWindow, timeStep)
					.from(kbURI)
					.where(select
							.add("?avgCpu", "?cpuValue", Aggregation.AVERAGE)
							.where(graph
									.add("?datum", "mc:hasMetric",
											"mc:cpu_utilization")
									.add("mc:hasValue", "?cpuValue")
									.add("mc:isAbout", "?resource")
									.add("?resouce", "mc:isIn",
											"?region")
									.add(RDF.type.toString(), "mc:tr_1")
									.add("?region", RDF.type.toString(),
											"mc:Region"))
							.groupby("?region")
							.having("?avgCpu >= \"0.6\"^^xsd:double"));
			System.out.println(WordUtils.wrap(query.toString(), 100));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		//VALIDATE QUERY
//		try {
//			Translator t = new CSparqlTranslator();
//			t.translate(query.toString());
//		} catch (TranslationException e) {
//			e.printStackTrace();
//			fail();
//		} catch (Exception e) {
//			System.err
//					.println("Parsing was successful, the following exception was raised after parsing: "
//							+ e.getClass().getName());
//		}
	}
}
