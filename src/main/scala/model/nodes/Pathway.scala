package model.nodes

import model.nodes.StoryNode.MutableStoryNode

/**
 * Trait that represents a pathway, which is the data structure of a possible choice that the player can make to
 * navigate the story nodes.
 * Optionally, the possibility to choose a pathway is dictated by a prerequisite.
 */
trait Pathway extends Serializable {
  def description: String
  def destinationNode: StoryNode
  def prerequisite: Option[Prerequisite]
}

object Pathway {

  /**
   * Implementation of the pathway.
   * @param description is the string showed to the player, which describe the action that the player can make.
   * @param destinationNode is the story node that will be the current one if the player chooses this pathway.
   * @param prerequisite is the condition to satisfy to make this pathway available (None if no prerequisite required)
   * @return the pathway.
   */
  def apply(description: String, destinationNode: StoryNode, prerequisite: Option[Prerequisite]): Pathway =
    new PathwayImpl(description, destinationNode, prerequisite)

  private class PathwayImpl(override val description: String,
                            override val destinationNode: StoryNode,
                            override val prerequisite: Option[Prerequisite]) extends Pathway {
    ArgsChecker.check(description, destinationNode, prerequisite)
  }
}

trait MutablePathway extends Pathway {
  var description: String
  var destinationNode: MutableStoryNode
  var prerequisite: Option[Prerequisite]
}

object MutablePathway {

  def apply(description: String,
            destinationNode: MutableStoryNode,
            prerequisite: Option[Prerequisite]): MutablePathway =
    new MutablePathwayImpl(description, destinationNode, prerequisite)

  private class MutablePathwayImpl(override var description: String,
                                   override var destinationNode: MutableStoryNode,
                                   override var prerequisite: Option[Prerequisite]) extends MutablePathway {
    ArgsChecker.check(description, destinationNode, prerequisite)
  }
}

private object ArgsChecker {
  def check(description: String, destinationNode: StoryNode, prerequisite: Option[Prerequisite]): Unit =
    require(
        Option(description).nonEmpty
        && description.trim.nonEmpty
        && Option(destinationNode).nonEmpty
        && Option(prerequisite).nonEmpty
    )
}
