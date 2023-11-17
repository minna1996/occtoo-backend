package com.valoriz.occtoo.service;

import com.valoriz.occtoo.config.OcctooConfiguration;
import com.valoriz.occtoo.config.TokenGenerator;
import com.valoriz.occtoo.jsonFetcher.JsonObjectReader;
import com.valoriz.occtoo.model.Entity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OcctooDataImportServiceImpl implements OcctooDataImportService {

    public static final String BASE_ADDRESS = "https://ingest.occtoo.com";

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    JsonObjectReader jsonObjectReader;

    /**
     * This function is employed for the purpose of importing data into Occtoo.
     *
     */

    @Override
    public void importDataToOcctoo() {
        try {
            String accessToken = tokenGenerator.getToken();
            String filePath = "C:\\Users\\Minna\\Desktop\\Occtoo\\Inventory.json";
            String dataSource= OcctooConfiguration.DATA_SOURCE;
            List<Entity> entities = new ArrayList<>();
            jsonObjectReader.processDataFromJson(accessToken, filePath, entities);
            importData(accessToken, entities,dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to import data
     *
     */
    public  void importData(String accessToken, List<Entity> entitiesToImport,String dataSource) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(BASE_ADDRESS + "/import/" + dataSource);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + accessToken);

        JSONObject payload = new JSONObject();
        payload.put("entities",jsonObjectReader.createEntitiesJSON(entitiesToImport));
        httpPost.setEntity(new StringEntity(payload.toString(), "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() != 202) {
            throw new Exception("Response not ok: " + response.getStatusLine().getStatusCode());
        }
        displayData(response);
    }

    /**
     * This method is used to display data
     *
     */
    public static void displayData(HttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            System.out.println("Response from server:\n" + responseString);
        } else {
            throw new Exception("Empty response from the server.");
        }
    }
}
