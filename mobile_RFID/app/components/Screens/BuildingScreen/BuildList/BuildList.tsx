import { View } from "react-native"
import { useBuilding } from "../../../../providers/BuildingProvider"
import { IBuilding } from "../../../../types/build"
import styles from "./styles"
import BuildItem from "../BuildItem"

const BuildList = () => {
  const { buildings } = useBuilding()

  return (
    <View style={styles.container}>
      {buildings.map((build: IBuilding) => (
        <BuildItem key={build.id} data={build} />
      ))}
    </View>
  )
}

export default BuildList
