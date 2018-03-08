package net.consensys.orion.impl.http.server;

import java.util.Objects;

public final class HttpError {
  private final String error;

  public HttpError(String error) {
    this.error = error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !getClass().equals(o.getClass())) {
      return false;
    }
    HttpError httpError = (HttpError) o;
    return Objects.equals(error, httpError.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error);
  }

  @Override
  public String toString() {
    return "HttpError{" + "error='" + error + '\'' + '}';
  }
}
