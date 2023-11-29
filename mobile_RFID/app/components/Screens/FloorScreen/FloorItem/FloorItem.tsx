import { Text, TouchableOpacity, View } from "react-native"
import { FC } from "react"
import styles from "./styles"
import AntDesign from "react-native-vector-icons/AntDesign"
import Entypo from "react-native-vector-icons/Entypo"
import { IFloor } from "../../../../types/floor"
import { useFloor } from "../../../../providers/FloorProvider"
import { useNavigation } from "@react-navigation/native"

interface FloorItemProps {
  data: IFloor
}

const FloorItem: FC<FloorItemProps> = ({ data }) => {
  const { openEditMode, deleteFloorById, areaId, buildingId } = useFloor()
  const navigation: any = useNavigation()

  return (
    <View style={styles.floorItem}>
      <TouchableOpacity
        onPress={() =>
          navigation.navigate("DetailLocation", {
            floorId: data.id,
            areaId,
            buildingId,
          })
        }
      >
        <Text style={styles.floorName}>{data.name}</Text>
      </TouchableOpacity>
      <View style={styles.icons}>
        <TouchableOpacity onPress={() => openEditMode(data)}>
          <Entypo name="edit" color="white" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => deleteFloorById(data.id)}>
          <AntDesign name="delete" color="white" />
        </TouchableOpacity>
      </View>
    </View>
  )
}

export default FloorItem
