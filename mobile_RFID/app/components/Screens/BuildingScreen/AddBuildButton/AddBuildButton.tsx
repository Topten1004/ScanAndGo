import { useBuilding } from "../../../../providers/BuildingProvider"
import Button from "../../../../shared/core/Button"

const AddBuildButton = () => {
  const { addNewBuild } = useBuilding()

  return (
    <Button text="Add Building" px={10} py={10} onPress={addNewBuild} borderRadius={10} filled />
  )
}

export default AddBuildButton
