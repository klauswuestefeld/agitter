package infra.simploy;

class FiveMinuteTrigger extends Trigger {

	private static final int FIVE_MINUTES = 1000 * 60 * 5;
	
		
	@Override
	protected long millisToWait() {
		return FIVE_MINUTES;
	}
		
}
