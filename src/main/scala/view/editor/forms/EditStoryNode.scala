package view.editor.forms

import controller.editor.EditorController
import controller.editor.util.FormBuilderUtil.BetterFormBuilder
import de.milchreis.uibooster.UiBooster
import de.milchreis.uibooster.model.Form

object EditStoryNode {

  def showEditStoryNodeForm(editorController: EditorController): Unit = {

    def showEditStoryNodeFormFields(id: Int, oldNarrative: String): Unit = {
      val form: Form = new UiBooster()
        .createForm("Edit story node")
        .addTextArea("What narrative should the story node show?", oldNarrative)
        .show()
      editorController.editExistingStoryNode(id, form.getByIndex(0).asString())
    }

    val form: Form = new UiBooster()
      .createForm("Edit story node")
      .addInt("Which story node would you like to edit? (id)")
      .show()
    showEditStoryNodeFormFields(
      form.getByIndex(0).asInt(),
      editorController.getStoryNode(form.getByIndex(0).asInt()).narrative
    )
  }

}
