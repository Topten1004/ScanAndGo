import { useDetailLocation } from "../../../../providers/DetailLocationProvider"
import Button from "../../../../shared/core/Button"

const AddLocationButton = () => {
  const { addNewLocation } = useDetailLocation()

  return (
    <Button text="Add Location" px={10} py={10} onPress={addNewLocation} borderRadius={10} filled />
  )
}

export default AddLocationButton
