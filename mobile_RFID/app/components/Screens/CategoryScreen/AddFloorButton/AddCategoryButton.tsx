import { useCategory } from "../../../../providers/CategoryProvider"
import Button from "../../../../shared/core/Button"

const AddCategoryButton = () => {
  const { addNewCategory } = useCategory()

  return <Button text="Add Floor" px={10} py={10} onPress={addNewCategory} borderRadius={10} filled />
}

export default AddCategoryButton
