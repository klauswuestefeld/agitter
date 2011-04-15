package org.prevayler.gwt.rpcservlet;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.bubble.Bubble;
import org.prevayler.bubble.PrevalentContext;
import org.prevayler.foundation.serialization.XStreamSerializer;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PrevalentRemoteServiceServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 1L;
	private static Prevayler _prevayler;
	private static SnapshotTakerThread snapshotTaker;

	class SnapshotTakerThread extends Thread {

		private static final int SNAPSHOT_RATE = 1000 * 60 * 10;
		boolean running = true;

		{
			setDaemon(true);
			start();
		}

		public void stopSnapshots() {
			running = false;
			interrupt();
		}

		@Override
		public void run() {
			while (running)
				takeSnapshot();

			try {
				_prevayler.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void takeSnapshot() {
			try {
				Thread.sleep(SNAPSHOT_RATE);
				_prevayler.takeSnapshot();
			} catch (InterruptedException e) {
				System.out.println("PrevaylerSnapshotThread stopped");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String processCall(String arg0) throws SerializationException {
		System.out.println(">> RPC " + arg0);
		String ret = super.processCall(arg0);
		System.out.println("<< RPC " + ret);
		return ret;
	}

	public PrevalentRemoteServiceServlet(RemoteService remoteService)
			throws Exception {
		super(prevalent(remoteService));
//		startSnapshotTaker();
	}

	private static Object prevalent(RemoteService remoteService)
			throws IOException, ClassNotFoundException {

		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(remoteService);
		factory.configureTransactionFiltering(false);
		factory.configureSnapshotSerializer(new XStreamSerializer("UTF-8"));
	
		PrevalentContext.startSession(factory);
		
		_prevayler = PrevalentContext.prevayler();
		return Bubble.wrapped(_prevayler.prevalentSystem(), null);
	}

	private void startSnapshotTaker() {
		snapshotTaker = new SnapshotTakerThread();
	}

	@Override
	public void destroy() {
		super.destroy();
		snapshotTaker.stopSnapshots();
		try {
			snapshotTaker.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
