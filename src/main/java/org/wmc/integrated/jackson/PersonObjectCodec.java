package org.wmc.integrated.jackson;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Iterator;

public class PersonObjectCodec extends ObjectCodec {

    @Override
    public Version version() {
        return null;
    }

    @SneakyThrows
    @Override
    public <T> T readValue(JsonParser jsonParser, Class<T> valueType) throws IOException {
        Person person = (Person) valueType.newInstance();

        // 只要还没结束"}"，就一直读
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = jsonParser.getCurrentName();
            if ("name".equals(fieldname)) {
                jsonParser.nextToken();
                person.setName(jsonParser.getText());
            } else if ("age".equals(fieldname)) {
                jsonParser.nextToken();
                person.setAge(jsonParser.getIntValue());
            }
        }
        return (T) person;
    }

    @Override
    public <T> T readValue(JsonParser jsonParser, TypeReference<T> typeReference) throws IOException {
        return null;
    }

    @Override
    public <T> T readValue(JsonParser jsonParser, ResolvedType resolvedType) throws IOException {
        return null;
    }

    @Override
    public <T> Iterator<T> readValues(JsonParser jsonParser, Class<T> aClass) throws IOException {
        return null;
    }

    @Override
    public <T> Iterator<T> readValues(JsonParser jsonParser, TypeReference<T> typeReference) throws IOException {
        return null;
    }

    @Override
    public <T> Iterator<T> readValues(JsonParser jsonParser, ResolvedType resolvedType) throws IOException {
        return null;
    }

    @Override
    public void writeValue(JsonGenerator jsonGenerator, Object o) throws IOException {

    }

    @Override
    public <T extends TreeNode> T readTree(JsonParser jsonParser) throws IOException {
        return null;
    }

    @Override
    public void writeTree(JsonGenerator jsonGenerator, TreeNode treeNode) throws IOException {

    }

    @Override
    public TreeNode createObjectNode() {
        return null;
    }

    @Override
    public TreeNode createArrayNode() {
        return null;
    }

    @Override
    public JsonParser treeAsTokens(TreeNode treeNode) {
        return null;
    }

    @Override
    public <T> T treeToValue(TreeNode treeNode, Class<T> aClass) throws JsonProcessingException {
        return null;
    }
}