import { Text, TouchableOpacity, View } from "react-native"
import { FC } from "react"
import styles from "./styles"
import AntDesign from "react-native-vector-icons/AntDesign"
import Entypo from "react-native-vector-icons/Entypo"
import { useArea } from "../../../../providers/AreaProvider"
import { IArea } from "../../../../types/area"
import { useNavigation } from "@react-navigation/native"

interface AreaItemProps {
  data: IArea
}

const AreaItem: FC<AreaItemProps> = ({ data }) => {
  const { openEditMode, deleteAreaById, buildingId } = useArea()
  const navigation: any = useNavigation()

  return (
    <View style={styles.areaItem}>
      <TouchableOpacity
        onPress={() =>
          navigation.navigate("Floor", {
            areaId: data.id,
            buildingId 
          })
        }
      >
        <Text style={styles.areaName}>{data.name}</Text>
      </TouchableOpacity>
      <View style={styles.icons}>
        <TouchableOpacity onPress={() => openEditMode(data)}>
          <Entypo name="edit" color="white" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => deleteAreaById(data.id)}>
          <AntDesign name="delete" color="white" />
        </TouchableOpacity>
      </View>
    </View>
  )
}

export default AreaItem
