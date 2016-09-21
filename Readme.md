/*******************************************************************************
 * Copyright (c) 2013 Markus Graube (TUD)
 * All rights reserved. 
 *******************************************************************************/
 
 Spin2Sparql
 ===========
 
 This project works with Eclipse and Maven. It converts SPARQL queries into SPIN models using http://topbraid.org/spin/api/.
 
 ## Building
 
 Build JAR with 
 ```
 mvn assembly:assembly
 ```
 
 ## Running
 ```
 java -cp target/spin2sparql-0.3.0-jar-with-dependencies.jar de.tud.plt.spin2sparql.Sparql2Spin query.rq > query.ttl
 java -cp target/spin2sparql-0.3.0-jar-with-dependencies.jar de.tud.plt.spin2sparql.Spin2Sparql query.ttl > query.rq
 ```
 
 
 
