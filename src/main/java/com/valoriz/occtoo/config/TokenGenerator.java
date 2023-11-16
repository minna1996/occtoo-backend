package com.valoriz.occtoo.config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

    public  final String BASE_ADDRESS = "https://ingest.occtoo.com";

    public String getToken() throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        String dataProviderId = OcctooConfiguration.DATA_PROVIDER_ID;
        String dataProviderSecret = OcctooConfiguration.DATA_PROVIDER_SECRET;
        HttpPost httpPost = new HttpPost(BASE_ADDRESS + "/dataProviders/tokens");
        httpPost.setHeader("Content-Type", "application/json");
        JSONObject json = new JSONObject();
        json.put("id", dataProviderId);
        json.put("secret", dataProviderSecret);
        httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            JSONObject jsonResponse = new JSONObject(responseString);

            if (response.getStatusLine().getStatusCode() == 200) {
                return jsonResponse.getJSONObject("result").getString("accessToken");
            } else {
                throw new Exception("Response not ok: " + response.getStatusLine()
                        .getStatusCode() + ", " + jsonResponse.toString());
            }
        } else {
            throw new Exception("Empty response from the server.");
        }
    }

}
