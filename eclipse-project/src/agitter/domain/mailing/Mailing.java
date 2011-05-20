package agitter.domain.mailing;

public interface Mailing {

	boolean shouldSendScheduleNow();

	void markScheduleSent();

}
