import { useArea } from "../../../../providers/AreaProvider"
import Button from "../../../../shared/core/Button"

const AddAreaButton = () => {
  const { addNewArea } = useArea()

  return <Button text="Add Area" px={10} py={10} onPress={addNewArea} borderRadius={10} filled />
}

export default AddAreaButton
