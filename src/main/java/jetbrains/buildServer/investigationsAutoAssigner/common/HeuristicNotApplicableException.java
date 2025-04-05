

package jetbrains.buildServer.investigationsAutoAssigner.common;

public class HeuristicNotApplicableException extends RuntimeException {
  private static final long serialVersionUID = 5366802885829409848L;

  public HeuristicNotApplicableException(String message) {
    super(message);
  }
}