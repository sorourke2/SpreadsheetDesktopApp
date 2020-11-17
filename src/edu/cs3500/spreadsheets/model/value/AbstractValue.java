package edu.cs3500.spreadsheets.model.value;

/**
 * Provides default implementations to methods and a generalized constructor.
 * @param <T> the type of the value
 */
public abstract class AbstractValue<T> implements Value<T> {

  private T value;

  public AbstractValue(T value) {
    this.value = value;
  }

  @Override
  public T getValue() {
    return this.value;
  }

  @Override
  public boolean isString() {
    return false;
  }

  @Override
  public boolean isNumber() {
    return false;
  }

  @Override
  public boolean isBoolean() {
    return false;
  }

  @Override
  public String toString() {
    return this.value.toString();
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof AbstractValue<?>
        && ((AbstractValue) obj).value.equals(this.value)
        && obj.getClass().toString().equals(this.getClass().toString());
  }
}
