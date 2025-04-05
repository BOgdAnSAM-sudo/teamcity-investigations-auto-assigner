package jetbrains.buildServer.investigationsAutoAssigner.utils;

import java.util.List;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.STest;
import jetbrains.buildServer.serverSide.STestRun;
import jetbrains.buildServer.serverSide.problems.BuildProblem;
import jetbrains.buildServer.tests.TestName;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.CopyOnWriteArrayList;


public class ProblemTextExtractor {
  private final List<ProblemTextExtractorExtension> extensions = new CopyOnWriteArrayList<>();

  public ProblemTextExtractor() {
  }

  public ProblemTextExtractor(@NotNull List<ProblemTextExtractorExtension> extensions) {
    this.extensions.addAll(extensions);
  }

  public List<ProblemTextExtractorExtension> getExtensions() {
    return extensions;
  }

  public void registerExtension(@NotNull ProblemTextExtractorExtension extension) {
    extensions.add(extension);
  }

  public void unregisterExtension(@NotNull ProblemTextExtractorExtension extension) {
    extensions.remove(extension);
  }

  public String getBuildProblemText(@NotNull final BuildProblem problem, @NotNull final SBuild build) {
    for (ProblemTextExtractorExtension extension : extensions) {
      if (extension.supports(problem)) {
        String text = extension.extractText(problem, build);
        if (text != null) {
          return text;
        }
        break;
      }
    }

    return problem.getBuildProblemDescription();
  }

  public String getBuildProblemText(STestRun sTestRun) {
    final STest test = sTestRun.getTest();
    final TestName testName = test.getName();
    return testName.getAsString() + " " + sTestRun.getFullText();
  }

}