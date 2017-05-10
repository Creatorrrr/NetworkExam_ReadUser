package com.example.kosta.networkexam_readuser;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class UserNameActivity extends AppCompatActivity {

    private List<User> nameList;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        nameList = new ArrayList<>();

        UsersLoadingTask task = new UsersLoadingTask();
        task.execute("http://10.0.2.2:8080/UserXMLExportExam/usernamelist.do?name=" + getIntent().getExtras().get("name"));

        adapter = new UserAdapter(this, nameList);

        ListView list = (ListView)findViewById(R.id.nameList);

        list.setAdapter(adapter);
    }

    private class UsersLoadingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(url.openStream()));

                NodeList nodeList = document.getElementsByTagName("user");
                for(int i = 0 ; i < nodeList.getLength() ; i++) {
                    User user = new User();
                    Node node = nodeList.item(i);

                    user.setName(getTagValue("name", (Element)node));
                    user.setAddress(getTagValue("address", (Element)node));
                    user.setHobby(getTagValue("hobby", (Element)node));

                    nameList.add(user);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        return nodeList.item(0).getNodeValue();
    }
}
