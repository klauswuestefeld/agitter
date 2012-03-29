package agitter.ui.view.session.events;

import java.util.List;

import sneer.foundation.lang.Pair;
import vaadinutils.AutoCompleteChooser.FullFeaturedItem;


public interface EventView {

	interface Boss {
		void onDescriptionEdit(String newText);
		boolean approveInviteesAdd(String invitee);
		void onInviteeRemoved(String invitee);
		void onDateRemoved(Long date);
		void onDateAdded(Long date);
		void onEventRemoved();
		void onDateChanged(Long from, Long to);
		void onOpennessChanged(boolean publicEvent);
	}

	
	void startReportingTo(Boss boss);
	
	void clear();

	void displayEditting(String description, long[] datetimes, List<FullFeaturedItem> invitees, int totalInviteesCount, boolean isPublicEvent);
	void refreshInviteesToChoose(List<FullFeaturedItem> inviteesToChoose);
	void refreshInvitationsHeader(int totalInviteesCount);

	void displayReadOnly(Pair<String,String> owner, String description, long[] datetimes, List<FullFeaturedItem> knownInvitees, int totalInviteesCount, boolean isPublicEvent);
	
	CommentsView commentsView();
}
