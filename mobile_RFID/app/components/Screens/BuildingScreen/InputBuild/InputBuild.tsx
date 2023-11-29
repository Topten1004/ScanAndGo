import { View } from "react-native"
import styles from "./styles"
import Input from "../../../../shared/core/Input"
import { useBuilding } from "../../../../providers/BuildingProvider"
import AddBuildButton from "../AddBuildButton"
import UpdateBuildButton from "../UpdateBuildButton"

const InputBuild = () => {
  const { buildingName, setBuildingName, isEditMode } = useBuilding()

  return (
    <View style={styles.container}>
      <Input
        placeholder="Enter a building name."
        value={buildingName}
        setValue={setBuildingName}
        width={200}
      />
      {isEditMode ? <UpdateBuildButton /> : <AddBuildButton />}
    </View>
  )
}

export default InputBuild
