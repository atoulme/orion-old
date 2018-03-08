package net.consensys.orion.impl.http.server;

import java.util.NoSuchElementException;

import io.netty.handler.codec.http.HttpHeaderValues;

public enum HttpContentType {
  JSON(HttpHeaderValues.APPLICATION_JSON.toString()),
  BINARY(HttpHeaderValues.BINARY.toString()),
  TEXT(HttpHeaderValues.TEXT_PLAIN.toString() + "; charset=utf-8"),
  HASKELL_ENCODED("application/haskell-stream"),
  CBOR("application/cbor");

  public final String httpHeaderValue;

  HttpContentType(String httpHeaderValue) {
    this.httpHeaderValue = httpHeaderValue;
  }

  @Override
  public String toString() {
    return httpHeaderValue;
  }
}
