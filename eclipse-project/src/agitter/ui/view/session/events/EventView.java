package agitter.ui.view.session.events;

import java.util.List;

import sneer.foundation.lang.Pair;
import vaadinutils.ProfileListItem;


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
		void onUpdateContacts();
	}

	
	void startReportingTo(Boss boss);
	
	void clear();

	void displayEditting(String description, long[] datetimes, List<ProfileListItem> invitees, int totalInviteesCount, boolean isPublicEvent);
	void refreshInviteesToChoose(List<ProfileListItem> inviteesToChoose);
	void refreshInvitationsHeader(int totalInviteesCount);

	void displayReadOnly(Pair<String,String> owner, String description, long[] datetimes, List<ProfileListItem> knownInvitees, int totalInviteesCount, boolean isPublicEvent);
	
	CommentsView commentsView();
}
