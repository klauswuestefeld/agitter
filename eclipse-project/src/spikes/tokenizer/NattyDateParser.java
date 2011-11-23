package spikes.tokenizer;

import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class NattyDateParser {
	public Date parsePost(String text) {

		Parser parser = new Parser();

		List<DateGroup> groups = parser.parse(text);

		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			// int line = group.getLine();
			// int column = group.getPosition();
			// String matchingValue = group.getText();
			// String syntaxTree = group.getSyntaxTree().toStringTree();
			// Map> parseMap = group.getParseLocations();
			// boolean isRecurreing = group.isRecurring();
			// Date recursUntil = group.getRecursUntil();

			if (dates.size() > 0)
				return dates.get(0);
		}

		return null;
	}
}
