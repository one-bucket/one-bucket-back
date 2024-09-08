package com.onebucket.connectionTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <br>package name   : com.onebucket.connectionTest
 * <br>file name      : MongoConnectTest
 * <br>date           : 2024-08-12
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-08-12        SeungHoon              init create
 * </pre>
 */

@SpringBootTest
public class MongoConnectTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void testMongoConnection() {
        // Given
        String collectionName = "testCollection";
        TestDocument document = new TestDocument("testId", "testValue");

        // When
        mongoTemplate.save(document, collectionName);
        TestDocument retrievedDocument = mongoTemplate.findById("testId", TestDocument.class, collectionName);

        // Then
        assertEquals(document.getValue(), retrievedDocument.getValue());
    }


    static class TestDocument {

        private String id;
        private String value;

        public TestDocument(String id, String value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
