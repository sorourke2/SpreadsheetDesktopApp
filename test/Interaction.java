/**
 * An interaction with the user consists of some input to send the program
 * and some output to expect.  We represent it as an object that takes in two
 * StringBuilders and produces the intended effects on them
 */
public interface Interaction {
  void apply(StringBuilder in, StringBuilder out);


  /**
   * Represents the printing of a sequence of lines to output.
   * @param lines The lines to output.
   * @return The Interaction of printing.
   */
  static Interaction prints(String... lines) {
    return (input, output) -> {
      for (String line : lines) {
        output.append(line);
      }
    };
  }

  /**
   * Represents a user providing a program with an input.
   * @param in The input
   * @return The Interaction of inputting
   */
  static Interaction inputs(String in) {
    return (input, output) -> {
      input.append(in);
    };
  }

}