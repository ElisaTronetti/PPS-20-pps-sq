package controller

import alice.tuprolog.Var
import controller.prolog.SqPrologEngine
import controller.prolog.structs._
import controller.prolog.util.PrologImplicits._
import model.nodes.StoryNode
import view.info.InfoView

/**
 * The Info Controller is used to get information about a certain story, starting from its route node.
 */
sealed trait InfoController extends Controller {

  /**
   * Check if at least a path exists from a [[model.nodes.StoryNode]] with id 'startId' and another with id 'endId'.
   *
   * @param startId the id of the starting [[model.nodes.StoryNode]].
   * @param endId   the id of the ending [[model.nodes.StoryNode]].
   * @return true if at least a path exists.
   */
  def pathExists(startId: Int, endId: Int): Boolean

  /**
   * Return all the possible paths  from a [[model.nodes.StoryNode]] with id 'startId' and another with id 'endId'.
   *
   * @param startId the id of the starting [[model.nodes.StoryNode]].
   * @param endId   the id of the ending [[model.nodes.StoryNode]].
   * @return A stream of Sequence of Ids where the first is always 'startId' and the last is always 'endId'
   */
  def paths(startId: Int, endId: Int): Stream[Seq[Int]]

  /**
   * Calculate the number of possible outcome of a story starting from the [[model.nodes.StoryNode]] with id 'startId'.
   *
   * @param startId the id of the starting [[model.nodes.StoryNode]].
   * @return the number of possible outcome.
   */
  def numberOfOutcomes(startId: Int): Int

  /**
   * Calculate the number of possible outcome of a story starting from the route node.
   *
   * @return the number of possible outcome.
   */
  def numberOfAllStoryOutcomes: Int

  /**
   * Calculate all the possible outcome of a story starting from the [[model.nodes.StoryNode]] with id 'startId'.
   *
   * @return A stream of Sequence of Ids where the first is always the route node's id and the last is always a final
   *         node.
   */
  def storyOutcomes(startId: Int): Stream[Seq[Int]]

  /**
   * Calculate all the possible outcome of a story starting from the route node.
   *
   * @return A stream of Sequence of Ids where the first is always the route node's id and the last is always a final
   *         node.
   */
  def allStoryOutcomes: Seq[Seq[Int]]

  /**
   * Calculate all the possible outcome of a story starting from the [[model.nodes.StoryNode]] with id 'startId' and
   * narrate what happens.
   *
   * @return A stream of Sequence strings representing the [[model.nodes.StoryNode]] narratives and the
   *         [[model.nodes.Pathway]] descriptions encountered in order.
   */
  def storyWalkthrough(startId: Int): Stream[Seq[String]]

  /**
   * Calculate all the possible outcome of a story starting from the route node and narrate what happens.
   *
   * @return A stream of Sequence strings representing the [[model.nodes.StoryNode]] narratives and the
   *         [[model.nodes.Pathway]] descriptions encountered in order.
   */
  def allStoryWalkthrough: Seq[Seq[String]]
}

object InfoController {

  private class InfoControllerImpl(private val previousController: Controller,
                                   private val routeNode: StoryNode
                                  )extends InfoController {

    private val prologEngine = SqPrologEngine(routeNode)
    private val infoView: InfoView = InfoView(this)

    /**
     * Start the Controller.
     */
    override def execute(): Unit = infoView.render()

    /**
     * Defines the actions to do when the Controller execution is over.
     */
    override def close(): Unit = previousController.execute()

    private def pathStructResult(startId: Int, endId: Int): Stream[PathStruct] =
      prologEngine.resolve(PathStruct(startId,endId, new Var()))

    override def pathExists(startId: Int, endId: Int): Boolean =
      pathStructResult(startId, endId).nonEmpty

    override def paths(startId: Int, endId: Int): Stream[Seq[Int]] = {
      pathStructResult(startId, endId).map(s => s.crossedIds)
    }

    private def reachAllFinalNodesStructResult(startId: Int): Stream[ReachAllFinalNodesStruct] =
      prologEngine.resolve(ReachAllFinalNodesStruct(startId, new Var()))

    override def numberOfOutcomes(startId: Int): Int =
      reachAllFinalNodesStructResult(startId).size

    override def storyOutcomes(startId: Int): Stream[Seq[Int]] =
      reachAllFinalNodesStructResult(startId).map(s => s.crossedNodes)

    private def allFinalNodesSolutionsStructResult: Stream[AllFinalNodeSolutionsStruct] =
      prologEngine.resolve(AllFinalNodeSolutionsStruct(routeNode.id, new Var()))

    override def numberOfAllStoryOutcomes: Int =
      allFinalNodesSolutionsStructResult.size

    override def allStoryOutcomes: Seq[Seq[Int]] =
      allFinalNodesSolutionsStructResult.head.allCrossedNodes

    override def storyWalkthrough(startId: Int): Stream[Seq[String]] =
      prologEngine.resolve(WalkthroughStruct(startId, new Var())).map(s => s.walkthrough)

    override def allStoryWalkthrough: Seq[Seq[String]] =
      prologEngine.resolve(AllStoryWalkthroughStruct(routeNode.id, new Var())).head.result
  }

  def apply(previousController: Controller, routeNode: StoryNode): InfoController =
    new InfoControllerImpl(previousController, routeNode)
}