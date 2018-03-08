package net.consensys.orion.impl.http.handler.receive;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

public class ReceiveRequest implements Serializable {

  private final String key;
  private final Optional<String> to; // b64 encoded

  @JsonCreator
  public ReceiveRequest(@JsonProperty("key") String key, @JsonProperty("to") String to) {
    this.key = key;
    this.to = Optional.ofNullable(to);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ReceiveRequest)) {
      return false;
    }
    ReceiveRequest that = (ReceiveRequest) o;
    return Objects.equals(key, that.key) && Objects.equals(to, that.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, to);
  }

  @Override
  public String toString() {
    return "ReceiveRequest{" + "key='" + key + '\'' + ", to='" + to + '\'' + '}';
  }

  public String key() {
    return key;
  }

  public Optional<String> to() {
    return to;
  }
}
