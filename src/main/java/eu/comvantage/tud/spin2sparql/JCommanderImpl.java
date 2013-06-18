package eu.comvantage.tud.spin2sparql;

import java.util.List;

import com.beust.jcommander.Parameter;

public class JCommanderImpl {
  
  @Parameter(required=true, description = "SPARQL input file")
  public List<String> inputFileName;


}