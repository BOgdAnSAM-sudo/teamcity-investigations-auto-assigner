package jetbrains.buildServer.investigationsAutoAssigner.utils;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.STest;
import jetbrains.buildServer.serverSide.STestRun;
import jetbrains.buildServer.serverSide.problems.BuildProblem;
import jetbrains.buildServer.tests.TestName;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

@Test
public class ProblemTextExtractorTest extends BaseTestCase {

  private BuildProblem mockBuildProblem;

  private SBuild mockBuild;

  private ProblemTextExtractorExtension mockExtension1;

  private ProblemTextExtractor problemTextExtractor;

  @BeforeMethod
  @Override
  protected void setUp() {
    mockBuildProblem = Mockito.mock(BuildProblem.class);
    mockBuild = Mockito.mock(SBuild.class);
    mockExtension1 = Mockito.mock(ProblemTextExtractorExtension.class);
    problemTextExtractor = new ProblemTextExtractor();

    when(mockBuildProblem.getBuildProblemDescription()).thenReturn("Default problem description");
  }

  public void testGetBuildProblemTextReturnsDefaultDescriptionWhenNoExtensions() {
    String result = problemTextExtractor.getBuildProblemText(mockBuildProblem, mockBuild);

    assertEquals("Default problem description", result);
  }

  public void testGetBuildProblemTextUsesExtensionWhenSupported() {
    when(mockExtension1.supports(mockBuildProblem)).thenReturn(true);
    when(mockExtension1.extractText(mockBuildProblem, mockBuild)).thenReturn("Extended description");
    problemTextExtractor.registerExtension(mockExtension1);

    String result = problemTextExtractor.getBuildProblemText(mockBuildProblem, mockBuild);

    assertEquals("Extended description", result);
  }

  public void testGetBuildProblemTextReturnsDefaultWhenExtensionReturnsNull() {
    when(mockExtension1.supports(mockBuildProblem)).thenReturn(true);
    when(mockExtension1.extractText(mockBuildProblem, mockBuild)).thenReturn(null);
    problemTextExtractor.registerExtension(mockExtension1);

    String result = problemTextExtractor.getBuildProblemText(mockBuildProblem, mockBuild);

    assertEquals("Default problem description", result);
  }

  public void testGetBuildProblemTextForTestRun() {
    STest mockTest = Mockito.mock(STest.class);
    TestName mockTestName = Mockito.mock(TestName.class);
    STestRun mockTestRun = Mockito.mock(STestRun.class);
    when(mockTestRun.getTest()).thenReturn(mockTest);
    when(mockTest.getName()).thenReturn(mockTestName);
    when(mockTestName.getAsString()).thenReturn("TestName");
    when(mockTestRun.getFullText()).thenReturn("failed with error");

    String result = problemTextExtractor.getBuildProblemText(mockTestRun);

    assertEquals("TestName failed with error", result);
  }

  public void testRegisterAndUnregisterExtension() {
    assertEmpty(problemTextExtractor.getExtensions());

    problemTextExtractor.registerExtension(mockExtension1);
    assertEquals(mockExtension1, problemTextExtractor.getExtensions().get(0));

    problemTextExtractor.unregisterExtension(mockExtension1);
    assertEmpty(problemTextExtractor.getExtensions());
  }

}