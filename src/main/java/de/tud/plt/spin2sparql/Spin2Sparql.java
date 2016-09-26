/*******************************************************************************
 * Copyright (c) 2016 Markus Graube (TUD)
 * All rights reserved. 
 *******************************************************************************/
package de.tud.plt.spin2sparql;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

/**
 * Converts between textual SPARQL representation and SPIN RDF model.
 * 
 * @author Markus Graube
 */
public class Spin2Sparql {
	
  @Parameter(required=true, description = "SPIN rdf input file(s)")
  public List<String> inputFileName;
  
  @Parameter(names = "--help", help = true)
  public boolean help;

	public static void main(String[] args) throws FileNotFoundException {

		// command-line-parser: JCommander-1.29
		Spin2Sparql jci = new Spin2Sparql();
		JCommander jc = new JCommander(jci);

		// wrong input => display usage
		try {
			jc.parse(args);
			if (jci.help){
				jc.usage();
				System.exit(0);
			}
		} catch (ParameterException e) {
			System.err.println(e.toString());
			System.exit(0);
			jc.usage();
		}
		

		Converter s = new Converter();
		for (String fileName : jci.inputFileName) {
			File inputFile = new File(fileName);

			if (!inputFile.canRead() || !inputFile.isFile()) {
				System.err
						.println("Wrong input file: There was no file found at "
								+ fileName + "!");
				System.exit(0);
			}
		
			s.convertSpin2Sparql(inputFile);
		}

	}


}
