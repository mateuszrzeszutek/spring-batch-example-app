package com.splunk.example.springbatch;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.batch.item.ItemProcessor;

public class TodoDownloadProcessor implements ItemProcessor<Integer, Record> {
  private final OkHttpClient httpClient = new OkHttpClient();
  private final Gson gson = new Gson();

  @Override
  public Record process(Integer id) throws Exception {
    var rq =
        new Request.Builder().get().url("https://jsonplaceholder.typicode.com/todos/" + id).build();
    var rs = httpClient.newCall(rq).execute();
    try (var body = rs.body()) {
      return gson.fromJson(body.string(), Record.class);
    }
  }
}
