package net.consensys.orion.impl.cmd;

import net.consensys.orion.api.cmd.Orion;

import java.util.Optional;

public class OrionArguments {
  private boolean argumentExit = false;

  private Optional<String> configFileName = Optional.empty();
  private Optional<String[]> keysToGenerate = Optional.empty();
  private boolean displayVersion = false;

  public OrionArguments(String[] args) {

    // Process Arguments
    // Usage Orion [--generatekeys|-g names] [config]
    // names - comma seperated list of key file prefixes (can include directory information) to
    // generate key(s) for
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--generatekeys":
        case "-g":
          if (++i >= args.length) {
            System.out.println("Error: Missing key names to generate.");
            argumentExit = true;
            break;
          }
          String keys = args[i];
          keysToGenerate = Optional.of(keys.split(","));
          break;
        case "--help":
        case "-h":
          argumentExit = true;
          break;
        case "--version":
        case "-v":
          displayVersion = true;
          break;
        default:
          if (args[i].startsWith("-")) {
            System.out.printf("Invalid option: %s\n", args[i]);
            argumentExit = true;
          } else {
            configFileName = Optional.of(args[i]);
          }
      }
    }

    if (argumentExit) {
      displayHelp();
    }
  }

  private void displayHelp() {
    System.out.println("Usage: " + Orion.name + " [options] [config file]");
    System.out.println("where options include:");
    System.out.println("\t-g");
    System.out.println("\t--generatekeys <names>");
    System.out.println("\t\tgenerate key pairs for each of the names supplied.");
    System.out.println("\t\twhere <names> are a comma-seperated list");
    System.out.println("\t-h");
    System.out.println("\t--help\tprint this help message");
    System.out.println("\t-v");
    System.out.println("\t--version\tprint version information");
  }

  public boolean argumentExit() {
    return argumentExit;
  }

  public Optional<String> configFileName() {
    return configFileName;
  }

  public Optional<String[]> keysToGenerate() {
    return keysToGenerate;
  }

  public boolean displayVersion() {
    return displayVersion;
  }
}
