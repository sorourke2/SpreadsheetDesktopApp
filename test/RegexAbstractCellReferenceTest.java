import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 * Tests the regex formula used to determine if a String is a valid cell or region reference.
 */
public class RegexAbstractCellReferenceTest {

  private Pattern pattern;

  /**
   * Initializes the pattern and matcher with the given regular expression.
   * @param regex the regular expression
   */
  private void initPattern(String regex) {
    this.pattern = Pattern.compile(regex);
  }

  @Test
  public void testCellRegex() {
    final String cell = "^([A-Z]+)([1-9]\\d*)$";

    this.initPattern(cell);

    assertTrue(this.pattern.matcher("A1").matches());
    assertTrue(this.pattern.matcher("A2").matches());
    assertTrue(this.pattern.matcher("A999").matches());
    assertTrue(this.pattern.matcher("XYZ1").matches());
    assertTrue(this.pattern.matcher("XYZA1230").matches());

    assertFalse(this.pattern.matcher("A").matches());
    assertFalse(this.pattern.matcher("2").matches());
    assertFalse(this.pattern.matcher("A0").matches());
    assertFalse(this.pattern.matcher("A012").matches());
    assertFalse(this.pattern.matcher("").matches());

    Matcher matcher = this.pattern.matcher("XYZ123");
    assertTrue(matcher.matches());
    assertEquals("XYZ", matcher.group(1));
    assertEquals("123", matcher.group(2));
  }
}
