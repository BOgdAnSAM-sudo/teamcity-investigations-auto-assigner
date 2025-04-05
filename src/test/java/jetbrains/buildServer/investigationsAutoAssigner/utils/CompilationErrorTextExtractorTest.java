package jetbrains.buildServer.investigationsAutoAssigner.utils;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.BuildProblemData;
import jetbrains.buildServer.BuildProblemTypes;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.problems.BuildProblem;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

@Test
public class CompilationErrorTextExtractorTest extends BaseTestCase {

  private BuildProblem buildProblem;

  private BuildProblemData buildProblemData;

  private SBuild build;

  private CompilationErrorTextExtractor extractor;

  @BeforeMethod
  @Override
  protected void setUp() throws Exception {
    extractor = new CompilationErrorTextExtractor();
    build = Mockito.mock(SBuild.class);
    buildProblem = Mockito.mock(BuildProblem.class);
    buildProblemData = Mockito.mock(BuildProblemData.class);
    when(buildProblem.getBuildProblemData()).thenReturn(buildProblemData);
  }

  
  public void shouldSupportCompilationErrorTypeTest() {
    when(buildProblemData.getType()).thenReturn(BuildProblemTypes.TC_COMPILATION_ERROR_TYPE);

    boolean result = extractor.supports(buildProblem);

    assertTrue(result);
  }

  
  public void shouldNotSupportNonCompilationErrorType() {
    when(buildProblemData.getType()).thenReturn("some.other.type");

    boolean result = extractor.supports(buildProblem);

    assertFalse(result);
  }

  
  public void shouldReturnNullForNonCompilationError() {
    when(buildProblemData.getType()).thenReturn("some.other.type");

    String result = extractor.extractText(buildProblem, build);

    assertNull(result);
  }

  
  public void shouldReturnNullWhenNoCompileBlockIndex() {
    when(buildProblemData.getType()).thenReturn(BuildProblemTypes.TC_COMPILATION_ERROR_TYPE);
    when(buildProblemData.getAdditionalData()).thenReturn(null);

    String result = extractor.extractText(buildProblem, build);

    assertEquals(" null",result);
  }

  
  public void shouldReturnNullWhenCompileBlockIndexIsInvalid() {
    when(buildProblemData.getType()).thenReturn(BuildProblemTypes.TC_COMPILATION_ERROR_TYPE);
    when(buildProblemData.getAdditionalData()).thenReturn("invalid_data");

    String result = extractor.extractText(buildProblem, build);

    assertEquals(" null",result);
  }

  public void shouldReturnBuildProblemText() {
    when(buildProblemData.getType()).thenReturn(BuildProblemTypes.TC_COMPILATION_ERROR_TYPE);
    when(buildProblemData.getAdditionalData()).thenReturn("build_problem");
    when(buildProblem.getBuildProblemDescription()).thenReturn("build_problem description");
    String result = extractor.extractText(buildProblem, build);

    assertEquals(" build_problem description", result);
  }
}