package view.util.scalaQuestSwingComponents.dialog

import view.util.scalaQuestSwingComponents.SqSwingButton

import java.awt.event.ActionListener

case class SqOkSwingDialog(titleText: String, phrase: String, okActionListener: ActionListener)
  extends SqAbstractSwingDialog(
    titleText,
    phrase,
    List(SqSwingButton("ok", okActionListener))
  )
