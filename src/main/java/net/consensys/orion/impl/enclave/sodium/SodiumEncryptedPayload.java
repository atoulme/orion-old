package net.consensys.orion.impl.enclave.sodium;

import net.consensys.orion.api.enclave.CombinedKey;
import net.consensys.orion.api.enclave.EnclaveException;
import net.consensys.orion.api.enclave.EncryptedPayload;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SodiumEncryptedPayload implements EncryptedPayload, Serializable {

  private final byte[] combinedKeyNonce;
  private final SodiumPublicKey sender;
  private final byte[] cipherText;
  private final byte[] nonce;
  private final SodiumCombinedKey[] combinedKeys;

  private Optional<Map<SodiumPublicKey, Integer>> combinedKeysOwners;

  public SodiumEncryptedPayload(
      SodiumPublicKey sender,
      byte[] nonce,
      byte[] combinedKeyNonce,
      SodiumCombinedKey[] combinedKeys,
      byte[] cipherText) {
    this(sender, nonce, combinedKeyNonce, combinedKeys, cipherText, Optional.empty());
  }

  @JsonCreator
  public SodiumEncryptedPayload(
      @JsonProperty("sender") SodiumPublicKey sender,
      @JsonProperty("nonce") byte[] nonce,
      @JsonProperty("combinedKeyNonce") byte[] combinedKeyNonce,
      @JsonProperty("combinedKeys") SodiumCombinedKey[] combinedKeys,
      @JsonProperty("cipherText") byte[] cipherText,
      @JsonProperty("combinedKeysOwners")
          Optional<Map<SodiumPublicKey, Integer>> combinedKeysOwners) {
    this.combinedKeyNonce = combinedKeyNonce;
    this.sender = sender;
    this.cipherText = cipherText;
    this.nonce = nonce;
    this.combinedKeys = combinedKeys;
    this.combinedKeysOwners = combinedKeysOwners;
  }

  @Override
  @JsonProperty("sender")
  public PublicKey sender() {
    return sender;
  }

  @Override
  @JsonProperty("cipherText")
  public byte[] cipherText() {
    return cipherText;
  }

  @Override
  @JsonProperty("nonce")
  public byte[] nonce() {
    return nonce;
  }

  @Override
  @JsonProperty("combinedKeys")
  public CombinedKey[] combinedKeys() {
    return combinedKeys;
  }

  @Override
  @JsonProperty("combinedKeyNonce")
  public byte[] combinedKeyNonce() {
    return combinedKeyNonce;
  }

  @Override
  public EncryptedPayload stripFor(PublicKey key) {
    Integer toKeepIdx = combinedKeysOwners.get().get(key);
    if (toKeepIdx == null || toKeepIdx < 0 || toKeepIdx >= combinedKeys.length) {
      throw new EnclaveException("can't strip encrypted payload for provided key");
    }

    return new SodiumEncryptedPayload(
        sender,
        nonce,
        combinedKeyNonce,
        new SodiumCombinedKey[] {combinedKeys[toKeepIdx]},
        cipherText);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SodiumEncryptedPayload that = (SodiumEncryptedPayload) o;
    return Arrays.equals(combinedKeyNonce, that.combinedKeyNonce)
        && Objects.equals(sender, that.sender)
        && Arrays.equals(cipherText, that.cipherText)
        && Arrays.equals(nonce, that.nonce)
        && Arrays.equals(combinedKeys, that.combinedKeys);
  }

  @Override
  public int hashCode() {
    return Objects.hash(combinedKeyNonce, sender.hashCode(), Arrays.hashCode(cipherText), Arrays.hashCode(nonce), Arrays.hashCode(combinedKeys));
  }
}
