package agitter.shared.rpc;


import agitter.shared.Application;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc")
public interface RemoteApplication extends Application, RemoteService {

}
 