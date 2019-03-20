import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Mode;
import org.jdiameter.api.Network;
import org.jdiameter.api.NetworkReqListener;
import org.jdiameter.api.Peer;
import org.jdiameter.api.PeerTable;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class Main {
    private SessionFactory clientSessionFactory;
    private StackImpl server;
    private StackImpl client;
    private Request actualRequest;
    private Answer actualAnswer;



    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.initClientAndServer();
        main.onePeerIsConnected();
        main.sendRequest();
        main.destroy();
    }


    //for logging ************************
    private static final Logger LOG = Logger.getLogger(Main.class);
    static{
        InputStream inStreamLog4j;
        try {
            inStreamLog4j = new FileInputStream("log4j.properties");
            Properties propertiesLog4j = new Properties();
            try {
                propertiesLog4j.load(inStreamLog4j);
                PropertyConfigurator.configure(propertiesLog4j);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LOG.debug("log4j configured");
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    //for logging ***********************



    private void initClientAndServer() throws Exception {
        //launch server
        server = new StackImpl();
        server.init(new XMLConfiguration("server-jdiameter-config.xml"));
        server.unwrap(Network.class).addNetworkReqListener(new NetworkReqListener() {

            public Answer processRequest(Request request) {
                actualRequest = request;
                return request.createAnswer();
            }
        }, ApplicationId.createByAuthAppId(33333));
        server.start();

        System.out.println("\n\n************************\nServer has started\n************************\n\n");
        Thread.sleep(7000);


        //launch client
        client = new StackImpl();
        clientSessionFactory = client.init(new XMLConfiguration("client-jdiameter-config.xml"));
        client.unwrap(Network.class).addNetworkReqListener(new NetworkReqListener() {
            public Answer processRequest(Request request) {
                return null;
            }
        }, ApplicationId.createByAuthAppId(33333));
        client.start(Mode.ALL_PEERS,7000,TimeUnit.MILLISECONDS);

        System.out.println("\n\n************************\nClient has started\n************************\n\n");
        Thread.sleep(7000);
    }

    private void onePeerIsConnected() throws Exception {
        List<Peer> peers = server.unwrap(PeerTable.class).getPeerTable();
        assertEquals(1,peers.size());
    }

    private void sendRequest() throws Exception {

        Session session = clientSessionFactory.getNewSession();
        Request request = session.createRequest(7,
                ApplicationId.createByAuthAppId(33333),
                "exchange.example.org",
                "127.0.0.1");

        session.send(request, new EventListener<Request, Answer>(){



            @Override
            public void receivedSuccessMessage(Request request, Answer arg1) {
                actualAnswer = arg1;

            }

            @Override
            public void timeoutExpired(Request request) {


            }

        });
        Thread.sleep(1000);
        System.out.println("Request should rich the server.");


        assertNotNull("Request wasn't sent to server sent.",  actualRequest);
        assertEquals(7,actualRequest.getCommandCode());
        assertEquals(6, actualRequest.getAvps().size());

        assertNotNull(actualAnswer);
        assertEquals(7,actualAnswer.getCommandCode());
        assertEquals(2,actualAnswer.getAvps().size());
    }

    private void destroy(){
        client.destroy();
        server.destroy();
    }
}
