package eu.comvantage.tud.spin2sparql;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple Sparql2spin.
 */
public class Sparql2spinTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Sparql2spinTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( Sparql2spinTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testConvert()
    {
    	Sparql2spin s2s = new Sparql2spin();
    	s2s.convert("SELECT * WHERE {?s ?p ?o}");
    	assertTrue(true);
    }
    
    public void testConvertUpdate()
    {
        Sparql2spin s2s = new Sparql2spin();
        s2s.convert("INSERT {?p ?s ?o} * WHERE {?s ?p ?o}");
        assertTrue(true);
    }
    
    public void testSpin2Sparql() throws FileNotFoundException
    {
    	Sparql2spin s2s = new Sparql2spin();
    	URL resourceUrl = getClass().getResource("/test.spin.ttl");
    	File inputFile = new File(resourceUrl.getFile());
		s2s.convertSpin2Sparql(inputFile );
		assert(true);
    }
    
}
