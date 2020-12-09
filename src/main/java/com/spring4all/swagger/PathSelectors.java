package com.spring4all.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.util.AntPathMatcher;

public class PathSelectors {
  private PathSelectors() {
    throw new UnsupportedOperationException();
  }

  /**
   * Any path satisfies this condition
   *
   * @return predicate that is always true
   */
  public static Predicate<String> any() {
    return Predicates.alwaysTrue();
  }

  /**
   * No path satisfies this condition
   *
   * @return predicate that is always false
   */
  public static Predicate<String> none() {
    return Predicates.alwaysFalse();
  }

  /**
   * Predicate that evaluates the supplied regular expression
   *
   * @param pathRegex - regex
   * @return predicate that matches a particular regex
   */
  public static Predicate<String> regex(final String pathRegex) {
    return new Predicate<String>() {
      @Override
      public boolean apply(String input) {
        return input.matches(pathRegex);
      }
    };
  }

  /**
   * Predicate that evaluates the supplied ant pattern
   *
   * @param antPattern - ant Pattern
   * @return predicate that matches a particular ant pattern
   */
  public static Predicate<String> ant(final String antPattern) {
    return new Predicate<String>() {
      @Override
      public boolean apply(String input) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(antPattern, input);
      }
    };
  }
}
