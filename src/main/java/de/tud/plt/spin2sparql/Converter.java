/*******************************************************************************
 * Copyright (c) 2016 Markus Graube (TUD)
 * All rights reserved. 
 *******************************************************************************/
package de.tud.plt.spin2sparql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileUtils;
import org.apache.jena.vocabulary.RDF;
import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.model.SPINFactory;
import org.topbraid.spin.model.update.Update;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.vocabulary.SP;

public class Converter {
	public void convertSpin2Sparql(File inputFile) throws FileNotFoundException {
		SPINModuleRegistry.get().init();
		FileInputStream fis = new FileInputStream(inputFile);
		Model model = ModelFactory.createDefaultModel().read(fis, null,
				FileUtils.langTurtle);

		StmtIterator it = model.listStatements(null, RDF.type, SP.Modify);
		while (it.hasNext()) {
			Resource rsrc = it.next().getSubject();
			Update query = SPINFactory.asUpdate(rsrc);
			ARQFactory.get().createUpdateRequest(query)
					.output(IndentedWriter.stdout);
		}
		it = model.listStatements(null, RDF.type, SP.Select);
		while (it.hasNext()) {
			Resource rsrc = it.next().getSubject();
			org.topbraid.spin.model.Query query = SPINFactory.asQuery(rsrc);
			ARQFactory.get().createQuery(query).output(IndentedWriter.stdout);
		}
	}

	public void convertSparql2Spin(File inputFile) throws QueryParseException {
		StringBuffer buff = new StringBuffer();
		try {
			FileReader fr = new FileReader(inputFile);
			int c;
			while ((c = fr.read()) != -1) {
				buff.append((char) c);
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		String sparqlQuery = buff.toString();
		
		Model model = ModelFactory.createDefaultModel();
		QueryParseException e_update = null;
		QueryParseException e_query = null;
		try {
			ARQ2SPIN.parseUpdate(sparqlQuery, model);
		} catch (QueryParseException e) {
			e_update = e;
		}
		try {
			ARQ2SPIN.parseQuery(sparqlQuery, model);
		} catch (QueryParseException e) {
			e_query = e;
		}
		if ((e_update!=null) && (e_query!=null) ){
			System.err.println("Error in Query:");
			System.err.println(e_update.getMessage());
			System.err.println(e_query.getMessage());
			if (e_update.getMessage().startsWith("Encountered \" \"select\""))
				throw e_query;
			else
				throw e_update;
		}
		else{
			model.write(System.out, FileUtils.langTurtle);
		}
	}
}
