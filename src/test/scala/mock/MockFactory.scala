package mock

import model.StoryModel
import model.characters.{Character, Enemy, Player}
import model.characters.properties.stats.{Stat, StatModifier, StatName}
import model.characters.properties.stats.StatName.StatName
import model.items.EquipItemType.EquipItemType
import model.items.{ConsumableItem, EquipItem, KeyItem}
import model.nodes.{Pathway, StoryNode}

object MockFactory {

  object StatFactory {
    val defaultValue: Int = 10
    var undefinedStatName: StatName = _

    def strengthStat(value: Int = defaultValue): Stat = Stat(value, StatName.Strength)
    def wisdomStat(value: Int = defaultValue): Stat = Stat(value, StatName.Wisdom)
    def dexterityStat(value: Int = defaultValue): Stat = Stat(value, StatName.Dexterity)
  }

  object StatModifierFactory {
    var undefinedOnApply: Int => Int = _
    val onApply: Int => Int = value => value * 2

    def statModifier(statName: StatName): StatModifier = StatModifier(statName, onApply)

    def statModifiers(statNames: StatName*): List[StatModifier] = {
      var modifiers: List[StatModifier] = List()
      for (stat <- statNames){
        modifiers = modifiers :+ statModifier(stat)
      }
      modifiers
    }
  }

  object CharacterFactory {

    val playerName: String = "player"
    val enemyName: String = "enemy"
    val negativeMaxPs: Int = -1
    val maxPs: Int = 100

    def mockSetOfStats(): Set[Stat] = {
      Set(
        Stat(1, StatName.Strength),
        Stat(1, StatName.Constitution),
        Stat(1, StatName.Wisdom),
        Stat(1, StatName.Charisma),
        Stat(1, StatName.Intelligence),
        Stat(1, StatName.Dexterity)
      )
    }

    def mockPlayer(maxPS: Int = maxPs): Player = Player("player", maxPS, mockSetOfStats())

    def mockEnemy(maxPs: Int = maxPs): Enemy = Enemy("enemy", maxPs, mockSetOfStats())
  }

  object ItemFactory {
    var undefinedEquipItemType: EquipItemType = _
    var undefinedItemName: String = _
    var undefinedItemDescription: String = _
    val itemName: String = "name"
    val itemDescription: String = "description"

    val onConsume: Character => Unit = c => c.properties.health.currentPS += 10
    val superOnConsume: Character => Unit = c => c.properties.health.currentPS += 50 // 💪

    val mockKeyItem: KeyItem = KeyItem(itemName, itemDescription)
    val mockConsumableItem: ConsumableItem = ConsumableItem(itemName, itemDescription, onConsume)
    val mockSuperConsumableItem: ConsumableItem = ConsumableItem(itemName, itemDescription, superOnConsume)

    def mockEquipItem(equipType: EquipItemType): EquipItem =
      EquipItem(itemName, itemDescription, List(), equipType)

  }

  import mock.MockFactory.CharacterFactory.mockPlayer

  def mockStoryNode(): StoryNode = {
    val destinationNode: StoryNode = StoryNode(1, "narrative", None, Set.empty, List())
    val pathway: Pathway = Pathway("description", destinationNode, None)
    StoryNode(0, "narrative", None, Set(pathway), List())
  }

  def mockStoryModel(playerMaxPS: Int): StoryModel = {
    StoryModel("s", mockPlayer(playerMaxPS), mockStoryNode())
  }

}
