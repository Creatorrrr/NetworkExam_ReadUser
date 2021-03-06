package com.example.kosta.networkexam_readuser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class UserListActivity extends AppCompatActivity {

    private List<User> users;

    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ListView list = (ListView)findViewById(R.id.list);

        UsersLoadingTask task = new UsersLoadingTask();
        task.execute("http://10.0.2.2:8080/UserXMLExportExam/userlist.do");

        users = new ArrayList<>();
        adapter = new UserAdapter(this, users);

        list.setAdapter(adapter);
    }

    private class UsersLoadingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));

                NodeList nodeList = doc.getElementsByTagName("user");
                for(int i = 0 ; i < nodeList.getLength() ; i++) {
                    User user = new User();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;

                    user.setName(getTagValue("name", element));
                    user.setAddress(getTagValue("address", element));
                    user.setHobby(getTagValue("hobby", element));

                    users.add(user);
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

    private static String getTagValue(String tag, Element element) {
        NodeList nList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = nList.item(0);

        return nValue.getNodeValue();
    }
}
