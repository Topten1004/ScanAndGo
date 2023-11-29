import { View } from "react-native"
import { useBuilding } from "../../../../providers/BuildingProvider"
import Button from "../../../../shared/core/Button"
import styles from "./styles"

const UpdateBuildButton = () => {
  const { updateBuild, closeEditMode } = useBuilding()

  return (
    <View style={styles.container}>
      <Button text="Update" px={10} py={10} onPress={updateBuild} borderRadius={10} filled />
      <Button text="Close" px={10} py={10} onPress={closeEditMode} borderRadius={10} filled />
    </View>
  )
}

export default UpdateBuildButton
