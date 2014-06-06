package eu.comvantage.tud.spin2sparql;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.hp.hpl.jena.query.QueryParseException;

/**
 * Unit test for simple Sparql2spin.
 */
@RunWith(JUnit4.class)
public class Sparql2spinTest 
{	
    
	@Test
    public void testSelect2Spin()
    {
    	Sparql2spin s2s = new Sparql2spin();
    	URL resourceUrl = getClass().getResource("/test_select.rq");
    	File inputFile = new File(resourceUrl.getFile());
    	s2s.convertSparql2Spin(inputFile);
    	assertTrue(true);
    }
    
    @Test(expected=QueryParseException.class)
    public void testSelect2Spin_incorrect()
    {
    	Sparql2spin s2s = new Sparql2spin();
    	URL resourceUrl = getClass().getResource("/test_select_incorrect.rq");
    	File inputFile = new File(resourceUrl.getFile());
    	s2s.convertSparql2Spin(inputFile);
    	assertTrue(true);
    }
    
    @Test
    public void testUpdate2Spin()
    {
    	Sparql2spin s2s = new Sparql2spin();
    	URL resourceUrl = getClass().getResource("/test_update.rq");
    	File inputFile = new File(resourceUrl.getFile());
    	s2s.convertSparql2Spin(inputFile);
    }
    
    @Test(expected=QueryParseException.class)
    public void testUpdate2Spin_incorrect()
    {
    	Sparql2spin s2s = new Sparql2spin();
    	URL resourceUrl = getClass().getResource("/test_update_incorrect.rq");
    	File inputFile = new File(resourceUrl.getFile());
    	s2s.convertSparql2Spin(inputFile);
    	assertTrue(true);
    }
    
    @Test
    public void testSpin2Sparql() throws FileNotFoundException
    {
    	Sparql2spin s2s = new Sparql2spin();
    	URL resourceUrl = getClass().getResource("/test.spin.ttl");
    	File inputFile = new File(resourceUrl.getFile());
		s2s.convertSpin2Sparql(inputFile );
		assert(true);
    }
    
}
