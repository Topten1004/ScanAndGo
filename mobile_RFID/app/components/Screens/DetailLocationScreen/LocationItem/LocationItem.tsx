import { Text, TouchableOpacity, View } from "react-native"
import { FC } from "react"
import styles from "./styles"
import AntDesign from "react-native-vector-icons/AntDesign"
import Entypo from "react-native-vector-icons/Entypo"
import { ILocation } from "../../../../types/location"
import { useDetailLocation } from "../../../../providers/DetailLocationProvider"

interface LocationItemProps {
  data: ILocation
}

const LocationItem: FC<LocationItemProps> = ({ data }) => {
  const { openEditMode, deleteLocationById } = useDetailLocation()

  return (
    <View style={styles.buildItem}>
      <Text style={styles.buildName}>{data.name}</Text>
      <View style={styles.icons}>
        <TouchableOpacity onPress={() => openEditMode(data)}>
          <Entypo name="edit" color="white" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => deleteLocationById(data.id)}>
          <AntDesign name="delete" color="white" />
        </TouchableOpacity>
      </View>
    </View>
  )
}

export default LocationItem
