package net.consensys.orion.impl.network;

import java.util.Objects;
import net.consensys.orion.api.config.Config;
import net.consensys.orion.api.network.NetworkNodes;
import net.consensys.orion.impl.enclave.sodium.SodiumPublicKey;
import net.consensys.orion.impl.enclave.sodium.SodiumPublicKeyDeserializer;

import java.io.Serializable;
import java.net.URL;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class MemoryNetworkNodes implements NetworkNodes, Serializable {

  private final URL url;
  private final CopyOnWriteArraySet<URL> nodeURLs;
  private final ConcurrentHashMap<PublicKey, URL> nodePKs;

  public MemoryNetworkNodes(Config config, PublicKey[] publicKeys) {
    url = config.url();
    if (config.otherNodes().length > 0) {
      nodeURLs = new CopyOnWriteArraySet<>(Arrays.asList(config.otherNodes()));
    } else {
      nodeURLs = new CopyOnWriteArraySet<>();
    }

    nodePKs = new ConcurrentHashMap<>();

    // adding my publickey(s) so /partyinfo returns my info when called.
    for (int i = 0; i < publicKeys.length; i++) {
      nodePKs.put(publicKeys[i], url);
    }
  }

  @JsonCreator
  public MemoryNetworkNodes(
      @JsonProperty("url") URL url,
      @JsonProperty("nodeURLs") Set<URL> nodeURLs,
      @JsonProperty("nodePKs") @JsonDeserialize(keyUsing = SodiumPublicKeyDeserializer.class)
          Map<SodiumPublicKey, URL> nodePKs) {
    this.url = url;
    this.nodeURLs = new CopyOnWriteArraySet<>(nodeURLs);
    this.nodePKs = new ConcurrentHashMap<>(nodePKs);
  }

  public MemoryNetworkNodes(URL url) {
    this(url, new CopyOnWriteArraySet<>(), new ConcurrentHashMap<>());
  }

  /**
   * Add a node's URL and PublcKey to the nodeURLs and nodePKs lists
   *
   * @param nodePk PublicKey of new node
   * @param node URL of new node
   */
  public void addNode(PublicKey nodePk, URL node) {
    this.nodeURLs.add(node);
    this.nodePKs.put(nodePk, node);
  }

  @Override
  @JsonProperty("url")
  public URL url() {
    return url;
  }

  @Override
  @JsonProperty("nodeURLs")
  public Set<URL> nodeURLs() {
    return nodeURLs;
  }

  @Override
  public URL urlForRecipient(PublicKey recipient) {
    return nodePKs.get(recipient);
  }

  @Override
  @JsonProperty("nodePKs")
  public Map<PublicKey, URL> nodePKs() {
    return nodePKs;
  }

  @Override
  public boolean merge(NetworkNodes other) {
    // note; not using map.putAll() as we don't want a malicious peer to overwrite ours nodes.
    boolean thisChanged = false;

    for (Map.Entry<PublicKey, URL> entry : other.nodePKs().entrySet()) {
      if (nodePKs.putIfAbsent(entry.getKey(), entry.getValue()) == null) {
        // putIfAbsent returns null if there was no mapping associated with the provided key
        thisChanged = true;
        nodeURLs.add(entry.getValue());
      }
    }

    return thisChanged;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !getClass().equals(o.getClass())) {
      return false;
    }

    MemoryNetworkNodes that = (MemoryNetworkNodes) o;

    return Objects.equals(url, that.url) && Objects.equals(nodeURLs, that.nodeURLs) && Objects.equals(nodePKs, that.nodePKs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, nodeURLs, nodePKs);
  }
}
