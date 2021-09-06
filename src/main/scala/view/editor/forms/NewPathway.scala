package view.editor.forms

import controller.editor.EditorController
import controller.editor.util.FormBuilderUtil.BetterFormBuilder
import de.milchreis.uibooster.UiBooster
import de.milchreis.uibooster.model.Form

object NewPathway {

  def showNewPathwayForm(editorController: EditorController): Unit = {
    val form: Form = new UiBooster()
      .createForm("Add new pathway")
      .addInt("Which story node is the starting node? (id)")
      .addInt("Which story node is the ending node? (id)")
      .addTextArea("What description should the pathway show?")
      .show()
    editorController.addNewPathway(
      form.getByIndex(0).asInt(),
      form.getByIndex(1).asInt(),
      form.getByIndex(2).asString()
    )
  }

}
