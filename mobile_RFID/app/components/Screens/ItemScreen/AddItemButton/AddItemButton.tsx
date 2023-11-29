import { useItem } from "../../../../providers/ItemProvider"
import Button from "../../../../shared/core/Button"

const AddItemButton = () => {
  const { addNewItem } = useItem()

  return <Button text="Add Item" px={10} py={10} onPress={addNewItem} borderRadius={10} filled />
}

export default AddItemButton
