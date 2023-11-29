import { Text, TouchableOpacity, View } from "react-native"
import { FC } from "react"
import styles from "./styles"
import AntDesign from "react-native-vector-icons/AntDesign"
import Entypo from "react-native-vector-icons/Entypo"
import { IItem } from "../../../../types/item"
import { useItem } from "../../../../providers/ItemProvider"

interface ItemProps {
  data: IItem
}

const Item: FC<ItemProps> = ({ data }) => {
  const { openEditMode, deleteItemById } = useItem()

  return (
    <View style={styles.floorItem}>
      <TouchableOpacity>
        <Text style={styles.floorName}>{data.name}</Text>
      </TouchableOpacity>
      <View style={styles.icons}>
        <TouchableOpacity onPress={() => openEditMode(data)}>
          <Entypo name="edit" color="white" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => deleteItemById(data.id)}>
          <AntDesign name="delete" color="white" />
        </TouchableOpacity>
      </View>
    </View>
  )
}

export default Item
