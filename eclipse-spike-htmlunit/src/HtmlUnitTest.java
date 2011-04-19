


import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 *  Para que funcione a "navegação" é necessário que os componentes tenham ids determinísticos.
 *  No Vaadin fazemos isto chamando o setDebugId 
 */
public class HtmlUnitTest {

	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		final WebClient webClient = new WebClient();
		webClient.setCssEnabled( false );
		
//		System.err.println( "carregando" );
		final HtmlPage page = webClient.getPage( "http://localhost:8888" );
//		System.err.println( "carregou" );
		
		final HtmlElement agitarBtn = getElementByIdWaiting( page, "AgitarBtn", 15 );
		agitarBtn.dblClick();
//		System.err.println( "Agitei" );
		
		final HtmlElement fecharBtn = getElementByIdWaiting( page, "CloseBtn", 15 );
		fecharBtn.dblClick();
//		System.err.println( "Fechei" );
		
//		System.err.println( page.getWebClient().waitForBackgroundJavaScript( 1000 ) );
		page.getWebClient().waitForBackgroundJavaScript( 1000 );
					    
		webClient.closeAllWindows();
	}

	private static HtmlElement getElementByIdWaiting(final HtmlPage page, final String id, final long timeoutSeconds) throws InterruptedException {
		final long now = System.currentTimeMillis();
		final long waitUntil = now + timeoutSeconds * 1000;
		HtmlElement ret = page.getElementById( id );
		while( ret == null && System.currentTimeMillis() < waitUntil ) {
			page.getWebClient().waitForBackgroundJavaScript( 50 );
			ret = page.getElementById( id );
		}
		return ret;
	}

}
