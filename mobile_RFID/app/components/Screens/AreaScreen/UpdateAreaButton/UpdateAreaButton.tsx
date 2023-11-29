import { View } from "react-native"
import Button from "../../../../shared/core/Button"
import styles from "./styles"
import { useArea } from "../../../../providers/AreaProvider"

const UpdateAreaButton = () => {
  const { updateAreaById, closeEditMode } = useArea()

  return (
    <View style={styles.container}>
      <Button text="Update" px={10} py={10} onPress={updateAreaById} borderRadius={10} filled />
      <Button text="Close" px={10} py={10} onPress={closeEditMode} borderRadius={10} filled />
    </View>
  )
}

export default UpdateAreaButton
