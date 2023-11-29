import { Text, TouchableOpacity, View } from "react-native"
import { IBuilding } from "../../../../types/build"
import { FC } from "react"
import styles from "./styles"
import AntDesign from "react-native-vector-icons/AntDesign"
import Entypo from "react-native-vector-icons/Entypo"
import { useBuilding } from "../../../../providers/BuildingProvider"
import { useNavigation } from "@react-navigation/native"

interface BuildItemProps {
  data: IBuilding
}

const BuildItem: FC<BuildItemProps> = ({ data }) => {
  const { openEditMode, deleteBuild } = useBuilding()
  const navigation: any = useNavigation()

  return (
    <View style={styles.buildItem}>
      <TouchableOpacity
        onPress={() =>
          navigation.navigate("Area", {
            buildingId: data.id,
          })
        }
      >
        <Text style={styles.buildName}>{data.name}</Text>
      </TouchableOpacity>
      <View style={styles.icons}>
        <TouchableOpacity onPress={() => openEditMode(data)}>
          <Entypo name="edit" color="white" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => deleteBuild(data.id)}>
          <AntDesign name="delete" color="white" />
        </TouchableOpacity>
      </View>
    </View>
  )
}

export default BuildItem
