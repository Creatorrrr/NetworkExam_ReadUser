package com.example.kosta.networkexam_readuser;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class UserDetailActivity extends AppCompatActivity {

    private TextView name;
    private TextView address;
    private TextView hobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        name = (TextView)findViewById(R.id.detailNameText);
        address = (TextView)findViewById(R.id.detailAddressText);
        hobby = (TextView)findViewById(R.id.detailHobbyText);

        Intent intent = getIntent();
        String sName = intent.getExtras().get("name").toString();

        UserLoadingTask task = new UserLoadingTask();
        task.execute("http://10.0.2.2:8080/UserXMLExportExam/user.do?name=" + sName);
    }

    private class UserLoadingTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... params) {
            User user = null;

            try {
                URL url = new URL(params[0]);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));

                NodeList nodeList = doc.getElementsByTagName("user");
                for(int i = 0 ; i < nodeList.getLength() ; i++) {
                    user = new User();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;

                    user.setName(getTagValue("name", element));
                    user.setAddress(getTagValue("address", element));
                    user.setHobby(getTagValue("hobby", element));
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

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            name.setText(user.getName());
            address.setText(user.getAddress());
            hobby.setText(user.getHobby());
        }
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        return nodeList.item(0).getNodeValue();
    }
}
