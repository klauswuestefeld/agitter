package agitter.main.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.prevayler.foundation.serialization.XStreamSerializer;

import com.thoughtworks.xstream.converters.ConversionException;

import sneer.foundation.testsupport.CleanTestBase;
import agitter.main.XStreamSafeSerializer;

public class XStreamSafeSerializerTest extends CleanTestBase {

	private final String encoding = "UTF-8";
	private final String oldSchemaXml = 
			  "<agitter.main.tests.XStreamSafeSerializerTest_-XStreamSafeSerializerTestObject>"
			+ "		<string>stringValue</string>"
			+ "</agitter.main.tests.XStreamSafeSerializerTest_-XStreamSafeSerializerTestObject>";

	private static class XStreamSafeSerializerTestObject {
	}

	@Test(expected=ConversionException.class)
	public void testUnsafeXStreamSerializer() throws IOException,
			ClassNotFoundException {
		testXStreamSerializer(new XStreamSerializer(encoding));
	}

	@Test
	public void testSafeXStreamSerializer() throws IOException,
			ClassNotFoundException {
		testXStreamSerializer(new XStreamSafeSerializer(encoding));
	}

	private void testXStreamSerializer(XStreamSerializer serializer)
			throws IOException, ClassNotFoundException {
		InputStream stream = new ByteArrayInputStream(oldSchemaXml.getBytes());
		XStreamSafeSerializerTestObject obj = (XStreamSafeSerializerTestObject) serializer
				.readObject(stream);
		assertNotNull(obj);
	}

}
