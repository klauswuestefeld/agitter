package spikes.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;

public class LuceneBase {
	
	IndexWriter w;
	StandardAnalyzer analyzer;
	Directory index;
	
	private static final int MAX_EVENTS_TO_SHOW = 40;
	
	Events events;
	User owner;
	
	public void create(Events events, User owner) throws CorruptIndexException, LockObtainFailedException, IOException {
		this.events = events;
		this.owner = owner;
		
		analyzer = new StandardAnalyzer(Version.LUCENE_35);  
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
		
		index = new RAMDirectory();
		w = new IndexWriter(index, config);
		
		for (Event e : events.toHappen(owner)) {
			addEvent(e);
		}
		
		w.close();
	}
	
	public List<Event> search(String querystr) throws CorruptIndexException, IOException, ParseException {
		Query q = new QueryParser(Version.LUCENE_35, "fullSearchableText", analyzer).parse(querystr);
		
		IndexReader reader = IndexReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_EVENTS_TO_SHOW, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		List<Event> ret = new ArrayList<Event>();
		for (ScoreDoc hit : hits) {
			Document doc = searcher.doc(hit.doc);
			long eventId = Long.parseLong(doc.get("id"));
			ret.add(events.get(eventId));
		}
		
		return ret;
	}
	
	private void addEvent(Event e) throws IOException {
		
		String fullText = e.description() + " " 
						+ getIndexStringForUser(e.owner())  + " "
						+ getIndexStringDatetimes(e.datetimes())  + " "
						+ getIndexStringInvitees(e.allResultingInvitees()); 
		
	    Document doc = new Document();
	    doc.add(new Field("id", Long.toString(e.getId()), Field.Store.YES, Field.Index.NO));
	    doc.add(new Field("description", e.description(), Field.Store.YES, Field.Index.ANALYZED));
	    doc.add(new Field("owner", getIndexStringForUser(e.owner()), Field.Store.NO, Field.Index.ANALYZED));
	    doc.add(new Field("datetimes", getIndexStringDatetimes(e.datetimes()), Field.Store.NO, Field.Index.ANALYZED));
	    doc.add(new Field("invitees", getIndexStringInvitees(e.allResultingInvitees()), Field.Store.NO, Field.Index.ANALYZED));
	    doc.add(new Field("fullSearchableText", fullText,  Field.Store.NO, Field.Index.ANALYZED));
	    w.addDocument(doc);
	}

	private String getIndexStringForUser(User u) {
		return u.email() + " " + u.name();
	}
	
	private String getIndexStringDatetimes(long[] datetimes) {
		StringBuilder b = new StringBuilder(); 
		for (long l : datetimes) {
			b.append(DateTools.timeToString(l, DateTools.Resolution.MINUTE));  
		}
		return b.toString();
	}
	
	private String getIndexStringInvitees(User[] users) {
		StringBuilder b = new StringBuilder(); 
		for (User u : users) {
			b.append(getIndexStringForUser(u));  
		}
		return b.toString();
	}
}
