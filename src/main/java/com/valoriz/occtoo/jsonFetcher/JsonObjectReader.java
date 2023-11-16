package com.valoriz.occtoo.jsonFetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valoriz.occtoo.model.Entity;
import com.valoriz.occtoo.model.Property;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
@Component
public class JsonObjectReader {


    public void processDataFromJson(String accessToken, String filePath, List<Entity> entities) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File(filePath);
            if (!jsonFile.exists()) {
                System.err.println("File not found: " + filePath);
                return;
            }
            JsonNode jsonNode = objectMapper.readTree(jsonFile);
            JsonNode dataNode = jsonNode.get("price");
            if (dataNode != null && dataNode.isArray()) {
                List<String> dataList = convertJsonNodeToList(dataNode);
                if (!dataList.isEmpty()) {
                    List<List<Entity>> allEntities = new ArrayList<>();
                    for (String data : dataList) {
                        JsonNode dataSetNode = objectMapper.readTree(data);
                        Iterator<Map.Entry<String, JsonNode>> fields = dataSetNode.fields();
                        while (fields.hasNext()) {
                            Map.Entry<String, JsonNode> entry = fields.next();
                            Entity entity = createEntity(fields, entry.getValue().toString());
                            entities.add(entity);
                        }
                        allEntities.add(entities);
                    }
                    System.out.println("List of Data: " + dataList);
                } else {
                    System.out.println("Data list is empty. No data to push to Occtoo.");
                }
            } else {
                System.err.println("No 'data' node found or it is not an array.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This  method to create an entity dynamically from the dataSetNode
     *
     */
    public  Entity createEntity(Iterator<Map.Entry<String, JsonNode>> fields, String entityKey) {
        List<Property> properties = new ArrayList<>();
        Entity entity = new Entity();
        entity.setKey(entityKey);
        entity.setDelete(false);
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            Property property = new Property();
            property.setId(entry.getKey());
            property.setValue(entry.getValue().asText());
            properties.add(property);
        }
        entity.setProperties(properties);
        return entity;
    }

    public  List<String> convertJsonNodeToList(JsonNode node) {
        List<String> resultList = new ArrayList<>();
        Iterator<JsonNode> elements = node.elements();
        while (elements.hasNext()) {
            JsonNode element = elements.next();
            resultList.add(element.toString());
        }
        return resultList;
    }

    public  JSONArray createEntitiesJSON(List<Entity> entities) {
        JSONArray entitiesArray = new JSONArray();
        for (Entity entity : entities) {
            JSONObject entityJSON = new JSONObject();
            entityJSON.put("key", entity.getKey());
            entityJSON.put("delete", entity.isDelete());
            JSONArray propertiesArray = new JSONArray();
            for (Property property : entity.getProperties()) {
                JSONObject propertyJSON = new JSONObject();
                propertyJSON.put("id", property.getId());
                propertyJSON.put("value", property.getValue());
                propertiesArray.put(propertyJSON);
            }
            entityJSON.put("properties", propertiesArray);
            entitiesArray.put(entityJSON);
        }
        return entitiesArray;
    }
}
