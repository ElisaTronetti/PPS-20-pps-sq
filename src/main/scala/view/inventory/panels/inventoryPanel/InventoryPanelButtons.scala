package view.inventory.panels.inventoryPanel

import controller.game.subcontroller.InventoryController
import model.characters.{Character}
import model.items.{EquipItem, Item, KeyItem}
import view.inventory.panels.TargetChooser
import view.util.scalaQuestSwingComponents.SqSwingButton.SqSwingButton
import view.util.scalaQuestSwingComponents.SqSwingDialog.YesNoSqSwingDialog

import java.awt.event.{ActionEvent, ActionListener}

private[inventoryPanel] object InventoryPanelButtons {

  def useButton(inventoryController: InventoryController, item: Item): SqSwingButton = {

    val useButtonName = item match {
      case equipItem: EquipItem if inventoryController.isEquipped(equipItem) => "Unequip"
      case _: EquipItem => "Equip"
      case _ => "Use"
    }

    val useButtonActionListener: ActionListener = {
      (_: ActionEvent) => {
        val targets: Set[Character] = inventoryController.targets()
        if (inventoryController.targets().size == 1 || item.isInstanceOf[EquipItem]) {
          inventoryController.use(item)(targets.head)
        } else {
          TargetChooser(inventoryController, item, targets)
        }
      }
    }

    new SqSwingButton(
      useButtonName,
      useButtonActionListener, item match {
        case _: KeyItem => false
        case _ => true
      })
    }

    def discardButton(inventoryController: InventoryController, item: Item): SqSwingButton =
    new SqSwingButton(
      "Discard",
      (_: ActionEvent) => YesNoSqSwingDialog("Really discard?",
        "Do you really want to discard this Item?\n There's no coming back.",
        (_: ActionEvent) => inventoryController.discard(item)
      ), true)

  def goBackButton(): SqSwingButton =
    new SqSwingButton(
      "Go Back",
      (_: ActionEvent) => {},
      true
    )
}
