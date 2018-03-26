package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.ExecuteResult;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjAutoDjStatus;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjOptCommand;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GetAutoDjStateHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        super.handle(t);

        try {

            RadiodjOptCommand cmd = new RadiodjAutoDjStatus();
            ExecuteResult res = cmd.execute();

            if(res.getResult() == ExecuteResult.SUCCESS) {
                sendResponse(parseAutoDjResponse(res.getResponse().getResponse()).getBytes(), t, RESPONSE_SUCCESS);
            } else {
                sendResponse(res.getMessage().getBytes(), t, RESPONSE_INTERNAL_ERROR);
            }



        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, RESPONSE_EXCEPTION);
        }
    }

    public static String parseAutoDjResponse(String s) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc =  builder.parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
        return doc.getElementsByTagName("string").item(0).getTextContent().toLowerCase();
    }
}
