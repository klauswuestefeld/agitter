package agitter.ui.view.session.events;

import java.util.List;

import sneer.foundation.lang.Pair;
import vaadinutils.AutoCompleteChooser.AutoCompleteItem;


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

	void displayEditting(String description, long[] datetimes, List<AutoCompleteItem> invitees, int totalInviteesCount);
	void refreshInviteesToChoose(List<AutoCompleteItem> inviteesToChoose);
	void refreshInvitationsHeader(int totalInviteesCount);

	void displayReadOnly(Pair<String,String> owner, String description, long[] datetimes, List<AutoCompleteItem> knownInvitees, int totalInviteesCount);
	
	CommentsView commentsView();
}
