import { View } from "react-native"
import styles from "./styles"
import Input from "../../../../shared/core/Input"
import UpdateAreaButton from "../UpdateAreaButton"
import { useArea } from "../../../../providers/AreaProvider"
import AddAreaButton from "../AddAreaButton"

const InputArea = () => {
  const { areaName, setAreaName, isEditMode } = useArea()

  return (
    <View style={styles.container}>
      <Input placeholder="Enter a area name." value={areaName} setValue={setAreaName} width={200} />
      {isEditMode ? <UpdateAreaButton /> : <AddAreaButton />}
    </View>
  )
}

export default InputArea
