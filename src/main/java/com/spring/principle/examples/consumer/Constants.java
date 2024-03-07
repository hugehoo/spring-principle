package com.spring.principle.examples.consumer;

public class Constants {

    public static final String BOOTSTRAP_SERVERS = "localhost:19092"
        // + ",localhost:9092,localhost:9093"
        + "";
    public static final String TOPIC_TEST = "local-two-partition";
    public static final String TOPIC_AVRO = "avro-iter";
    public static final String GROUP_ID = "parti-group";

    public static final String SCHEMA_REGISTRY_URL = "http://127.0.0.1:8081";

}
