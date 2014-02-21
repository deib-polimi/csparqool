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
package it.polimi.modaclouds.monitoring.monitoring_rules_lib;

import static org.junit.Assert.fail;
import it.polimi.csparqool.Aggregation;
import it.polimi.csparqool.CSquery;
import it.polimi.csparqool.graph;
import it.polimi.csparqool.select;
import it.polimi.modaclouds.monitoring.commons.vocabulary.MC;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang.WordUtils;
import org.junit.Test;

import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import eu.larkc.csparql.core.parser.CSparqlLexer;
import eu.larkc.csparql.core.parser.CSparqlParser;

public class CsparqlQueryTest {

	@Test
	public void test() {
		String timeWindow = "60";
		String timeStep = "60";
		String kbURI = "http://localhost:8175";
		CSquery query = CSquery.createDefaultQuery("CPU Utilization Rule");
		String streamURI = MC.getURI() + "/streams/cpu_utilization";

		try {
			query.setNsPrefix("xsd", XSD.getURI())
					.setNsPrefix("rdf", RDF.getURI())
					.setNsPrefix("rdfs", RDFS.getURI())
					.setNsPrefix("mc", MC.getURI())
					.construct(
							graph.add(CSquery.BLANK_NODE, RDF.type.toString(),
									MC.Violation.toString()))
					.fromStream(streamURI, timeWindow, timeStep)
					.from(kbURI)
					.where(select
							.add("?avgCpu", "?cpuValue", Aggregation.AVERAGE)
							.where(graph
									.add("?datum", MC.hasMetric.toString(),
											"mc:cpu_utilization")
									.add(MC.hasValue.toString(), "?cpuValue")
									.add(MC.isAbout.toString(), "?resource")
									.add("?resouce", MC.isIn.toString(),
											"?region")
									.add(RDF.type.toString(), "mc:tr_1")
									.add("?region", RDF.type.toString(),
											MC.Region.toString()))
							.groupby("?region")
							.having("?avgCpu >= \"0.6\"^^xsd:double"));
			System.out.println(WordUtils.wrap(query.toString(), 100));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		try {
			ANTLRStringStream input = new ANTLRStringStream(query.toString());
			System.out.println("1");
			Lexer lexer = new CSparqlLexer(input);
			System.out.println("2");
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			System.out.println("3");
			CSparqlParser parser = new CSparqlParser(tokens);
			System.out.println("4");
			parser.expression();
		} catch (RecognitionException e) {
			e.printStackTrace();
			fail();
		}
	}
}
