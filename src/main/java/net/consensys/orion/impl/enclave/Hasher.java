package net.consensys.orion.impl.enclave;

import net.consensys.orion.api.enclave.EnclaveException;
import net.consensys.orion.api.enclave.HashAlgorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class Hasher {
  //TODO consider interface/implementation split
  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  public byte[] digest(HashAlgorithm algorithm, byte[] input) {
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance(algorithm.getName());
      digest.update(input);
      return digest.digest();
    } catch (NoSuchAlgorithmException e) {
      throw new EnclaveException(e);
    }
  }
}
