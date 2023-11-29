import { View } from "react-native"
import styles from "./styles"
import FloorItem from "../FloorItem"
import { useFloor } from "../../../../providers/FloorProvider"
import { IFloor } from "../../../../types/floor"

const FloorList = () => {
  const { floors } = useFloor()

  return (
    <View style={styles.container}>
      {floors.map((floor: IFloor) => (
        <FloorItem key={floor.id} data={floor} />
      ))}
    </View>
  )
}

export default FloorList
