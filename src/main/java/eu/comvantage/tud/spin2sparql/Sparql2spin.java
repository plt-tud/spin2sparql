/*******************************************************************************
 * Copyright (c) 2013 Markus Graube (TUD)
 * All rights reserved. 
 *******************************************************************************/
package eu.comvantage.tud.spin2sparql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.jena.atlas.io.IndentedWriter;
import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.model.SPINFactory;
import org.topbraid.spin.model.update.Update;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.vocabulary.SP;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Converts between textual SPARQL representation and SPIN RDF model.
 * 
 * @author Markus Graube
 */
public class Sparql2spin {

	public static void main(String[] args) throws FileNotFoundException {

		// command-line-parser: JCommander-1.29
		JCommanderImpl jci = new JCommanderImpl();
		JCommander jc = new JCommander(jci);

		// wrong input => display usage
		try {
			jc.parse(args);
		} catch (ParameterException e) {
			System.err.println(e.toString());
			System.exit(0);
		}

		for (String fileName : jci.inputFileName) {
			File inputFile = new File(fileName);

			if (!inputFile.canRead() || !inputFile.isFile()) {
				System.err
						.println("Wrong input file: There was no .rq-file found at "
								+ fileName + "!");
				System.exit(0);
			}

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
			String query = buff.toString();
			Sparql2spin s = new Sparql2spin();
			if (inputFile.getName().toLowerCase().endsWith(".rq"))
				s.convert(query);
			else if (inputFile.getName().toLowerCase().endsWith(".ttl"))
				s.convertSpin2Sparql(inputFile);
		}

	}

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

	public void convert(String sparqlQuery) {
		Model model = ModelFactory.createDefaultModel();
		try {
			ARQ2SPIN.parseUpdate(sparqlQuery, model);
		} catch (Exception e) {
		}
		try {
			ARQ2SPIN.parseQuery(sparqlQuery, model);
		} catch (Exception e) {
		}
		model.write(System.out, FileUtils.langTurtle);
	}
}
