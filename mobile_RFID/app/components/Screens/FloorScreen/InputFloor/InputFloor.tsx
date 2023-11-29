import { View } from "react-native"
import styles from "./styles"
import Input from "../../../../shared/core/Input"
import { useFloor } from "../../../../providers/FloorProvider"
import AddFloorButton from "../AddFloorButton"
import UpdateFloorButton from "../UpdateFloorButton"

const InputFloor = () => {
  const { areaName, setAreaName, isEditMode } = useFloor()

  return (
    <View style={styles.container}>
      <Input
        placeholder="Enter a floor name."
        value={areaName}
        setValue={setAreaName}
        width={200}
      />
      {isEditMode ? <UpdateFloorButton /> : <AddFloorButton />}
    </View>
  )
}

export default InputFloor
