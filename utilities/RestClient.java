package gr.deddie.pfr.utilities;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

public class RestClient {

    public static <T> T doPut(String url, T data, GenericType<T> type) {
        return doPut(url, data, type, new MultivaluedHashMap<String, Object>());
    }

    public static <T> T doPut(String url, T data, GenericType<T> type, MultivaluedMap<String, Object> headers) {
        Client client = ClientBuilder.newClient();
        try {
            T result = client.target(url).request(MediaType.APPLICATION_JSON).headers(headers).put(Entity.json(data), type);
            return result;
        } finally {
            client.close();
        }
    }

    public static <T, K> K doPost(String url, T data, Class<K> clazz) {
        final Client client = ClientBuilder.newClient();
        try {
            return client.target(url).request(MediaType.TEXT_PLAIN).post(Entity.json(data), clazz);
        } finally {
            client.close();
        }
    }

    public static <T, K> K doPost(String url, T data, GenericType<K> type) {
        return doPost(url, data, type, new MultivaluedHashMap<String, Object>());
    }

    public static <T, K> K doPost(String url, T data, GenericType<K> type, MultivaluedMap<String, Object> headers) {
        Client client = ClientBuilder.newClient();
        try {
            K result = client.target(url).request(MediaType.APPLICATION_JSON).headers(headers).post(Entity.json(data), type);
            return result;
        } finally {
            client.close();
        }
    }

    public static <T> T doGet(String url, GenericType<T> type) {
        Client client = ClientBuilder.newClient();
        try {
            T result = client.target(url).request(MediaType.APPLICATION_JSON).get(type);
            return result;
        } finally {
            client.close();
        }
    }

    public static <T> T doGet(String url, Map<String, Object[]> queryParams, GenericType<T> type) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(url);
            if (Util.isNotEmpty(queryParams)) {
                for (final Map.Entry<String, Object[]> e : queryParams.entrySet()) {
                    target  = target.queryParam(e.getKey(), e.getValue());
                }
            }
            T result = target.request(MediaType.APPLICATION_JSON).get(type);
            return result;
        } finally {
            client.close();
        }
    }

    public static <T> T doGet(String url, Map<String, Object> queryParams, MultivaluedMap<String, Object> headers, GenericType<T> type) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(url);
            if (Util.isNotEmpty(queryParams)) {
                for (final Map.Entry<String, Object> e : queryParams.entrySet()) {
                    target  = target.queryParam(e.getKey(), e.getValue());
                }
            }
            T result = target.request(MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .get(type);
            return result;
        } finally {
            client.close();
        }
    }

    public static <T> List<T> doGet(String url, MultivaluedMap<String, Object> headers) {
        Client client = ClientBuilder.newClient();
        try {
            List<T> result = client.target(url).request(MediaType.APPLICATION_JSON).headers(headers).get(new GenericType<List<T>>() {
            });
            return result;
        } finally {
            client.close();
        }
    }

}
