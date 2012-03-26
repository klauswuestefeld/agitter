package agitter.ui.view.session.events;

import java.util.List;

import sneer.foundation.lang.Pair;


public interface EventView {

	interface Boss {
		void onDescriptionEdit(String newText);
		boolean approveInviteesAdd(String invitee);
		void onInviteeRemoved(String invitee);
		void onDateRemoved(Long date);
		void onDateAdded(Long date);
		void onEventRemoved();
		void onDateChanged(Long from, Long to);
	}

	
	void startReportingTo(Boss boss);
	
	void clear();

	void displayEditting(String description, long[] datetimes, List<Pair<String,String>> invitees, int totalInviteesCount);
	void refreshInviteesToChoose(List<Pair<String,String>> inviteesToChoose);
	void refreshInvitationsHeader(int totalInviteesCount);

	void displayReadOnly(Pair<String,String> owner, String description, long[] datetimes, List<Pair<String,String>> knownInvitees, int totalInviteesCount);
	
	CommentsView commentsView();
}
