import { View } from "react-native"
import styles from "./styles"
import AreaItem from "../AreaItem"
import { useArea } from "../../../../providers/AreaProvider"
import { IArea } from "../../../../types/area"

const AreaList = () => {
  const { areas } = useArea()

  return (
    <View style={styles.container}>
      {areas.map((area: IArea) => (
        <AreaItem key={area.id} data={area} />
      ))}
    </View>
  )
}

export default AreaList
