import { useFloor } from "../../../../providers/FloorProvider"
import Button from "../../../../shared/core/Button"

const AddFloorButton = () => {
  const { addNewFloor } = useFloor()

  return <Button text="Add Floor" px={10} py={10} onPress={addNewFloor} borderRadius={10} filled />
}

export default AddFloorButton
