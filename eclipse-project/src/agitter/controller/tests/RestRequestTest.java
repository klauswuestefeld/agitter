package agitter.controller.tests;

import org.junit.Assert;
import org.junit.Test;

import utils.RestRequest;


public class RestRequestTest extends Assert {

	@Test
	public void uriIsAuthentic() {
		RestRequest req = new RestRequest() {
			{
				addParamToUri("a", "40");
				addParamToUri("b", "2");
			}

			@Override
			protected String command() {
				return "addNumbers";
			}
		};
		
		String regression = "addNumbers?a=40&b=2&code=4E3374A200D36246C82DBC81A371FB4F08EBCB1A2649B4EAF9C05481EE434EAC";
		assertEquals(regression, req.asSecureURI());
	}
	
}
