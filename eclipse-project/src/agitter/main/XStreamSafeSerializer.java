package agitter.main;

import org.prevayler.foundation.serialization.XStreamSerializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XStreamSafeSerializer extends XStreamSerializer {
	
	public XStreamSafeSerializer(String encoding) {
		super(encoding);
	}

	@Override
	protected XStream createXStream() {
		return new XStream() {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {

					@Override
					public boolean shouldSerializeMember(@SuppressWarnings("rawtypes") Class definedIn,
							String fieldName) {
						if (definedIn == Object.class) {
							return false;
						}
						return super
								.shouldSerializeMember(definedIn, fieldName);
					}
				};
			}
		};
	}
}
