import org.junit.Assert;
import org.junit.Test;
import org.server.status.ServerStatusViewer;

import java.io.IOException;

/**
 * Created by Manikandan on 2/27/2016.
 */
public class ServerStatusViewerTest {

@Test
public void testAppVersion(){
  try {
    String app[]=new String[1];
    Assert.assertNotNull(ServerStatusViewer.getAppVersion());
    Assert.assertNotNull(ServerStatusViewer.getsncrdrbbundlefromDb("select * from SNF_B2B.SNF_DRB_BUNDLE WHERE BUNDLE='LOAD_BALANCER_SWITCH'","KEY_NAME","VALUE"));
    ServerStatusViewer.main(app);

  }catch (IOException e){
    Assert.fail(e.getMessage());
  }

}


}
