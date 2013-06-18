/*******************************************************************************
 * Copyright (c) 2013 Markus Graube (TUD)
 * All rights reserved. 
 *******************************************************************************/
package eu.comvantage.tud.spin2sparql;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.system.SPINModuleRegistry;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.ReificationStyle;
import com.hp.hpl.jena.util.FileUtils;

/**
 * Converts between textual SPARQL representation and SPIN RDF model.
 * 
 * @author Markus Graube
 */
public class Sparql2spin {

	public static void main(String[] args) {

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
			
			if (!inputFile.canRead() || !inputFile.isFile() || !inputFile.getName().toLowerCase().endsWith(".rq")) {
				System.err.println("Wrong input file: There was no .rq-file found at " + fileName + "!");
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
		        System.out.println("Input query " + inputFile.getPath() + " was read.");
		    } catch (IOException e) { 
		    	e.printStackTrace(); 
		    	System.exit(0);
		    } 
		    String query = buff.toString();
		    Sparql2spin s = new Sparql2spin();
		    s.convert(query);
		}
		
	}

	public void convert(String sparqlQuery) {
		// Register system functions (such as sp:gt (>))
		SPINModuleRegistry.get().init();

		// Create an empty OntModel importing SP
		Model model = ModelFactory.createDefaultModel(ReificationStyle.Minimal);

		Query arqQuery = ARQFactory.get().createQuery(model, sparqlQuery);
		ARQ2SPIN arq2SPIN = new ARQ2SPIN(model);
		arq2SPIN.createQuery(arqQuery, null);
		model.write(System.out, FileUtils.langTurtle);
	}
}
