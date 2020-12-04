package com.moustaka.katerina.backend_application_moustaka.Repositories;

import com.moustaka.katerina.backend_application_moustaka.Models.Book;
import com.moustaka.katerina.backend_application_moustaka.Models.People;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class PeopleRepository {
  private MySQLPool client;

  public PeopleRepository(MySQLPool client) {
    this.client = client;
  }

  public Handler<RoutingContext> listPeopleHandler() {
    return routingContext -> {

      List<People> people = new ArrayList<>();
      client.query("SELECT * FROM people").execute(ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          for (Row row : rows) {
            people.add(new People(row.getString("id"), row.getString("name"), row.getString("lastName"), row.getString("occupation")));
          }
        }
        routingContext.response().setStatusCode(200)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(new JsonArray(people).encode()
          );
      });
    };
  }

  public Handler<RoutingContext> addPeopleHandler() {
    return routingContext -> {
      JsonObject newP = routingContext.getBodyAsJson();
      client.preparedQuery("INSERT INTO people (id,name,lastName,occupation) VALUES (?,?,?,?)")
        .execute(Tuple.of(newP.getString("id"), newP.getString("name"), newP.getString("lastName"), newP.getString("occupation")),
          ar -> {
            if (ar.succeeded()) {
              routingContext.response().setStatusCode(200)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end();
            }
          });
    };
  }

  public Handler<RoutingContext> searchPeopleById() {
    return routingContext -> {
      RequestParameters parameters = routingContext.get("parsedParameters");
      String idp = parameters.pathParameter("id").getString();
      List<People> pp = new ArrayList<>();
      client.preparedQuery("SELECT * FROM people WHERE id=?").execute(Tuple.of(idp), ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          for (Row row : rows) {
            pp.add(new People(row.getString("id"), row.getString("name"), row.getString("lastName"), row.getString("occupation")));
          }
        }
        routingContext.response().setStatusCode(200)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(new JsonArray(pp).encode()
          );

      });
    };
  }
}
