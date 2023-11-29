import { View } from "react-native"
import styles from "./styles"
import Input from "../../../../shared/core/Input"
import AddLocationButton from "../AddLocationButton"
import UpdateLocationButton from "../UpdateLocationButton"
import { useDetailLocation } from "../../../../providers/DetailLocationProvider"

const InputLocation = () => {
  const { locationName, setLocationName, isEditMode } = useDetailLocation()

  return (
    <View style={styles.container}>
      <Input
        placeholder="Enter a location name."
        value={locationName}
        setValue={setLocationName}
        width={200}
      />
      {isEditMode ? <UpdateLocationButton /> : <AddLocationButton />}
    </View>
  )
}

export default InputLocation
